package org.example;

import org.example.CommandSpace.BlankConsole;
import org.example.CommandSpace.Printable;
import org.example.CommandSpace.RequestHandler;
import org.example.Exceptions.ConnectionErrorException;
import org.example.Exceptions.OpenServerException;
import org.example.Managers.CommandManager;
import org.example.Managers.ConnectionManager;
import org.example.Managers.Database.DatabaseManager;
import org.example.Managers.FutureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private int port;
    private int socketTimeout;
    private Printable console;
    private ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    private final DatabaseManager databaseManager;
    private CommandManager commandManager;
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    static final Logger serverLogger = LoggerFactory.getLogger(Server.class);

    BufferedReader scanner = new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in)));

    public Server(CommandManager commandManager, DatabaseManager databaseManager) {
        this.port = Main.port;
        this.socketTimeout = Main.connectionTimeout;
        this.console = new BlankConsole();
        this.commandManager = commandManager;
        this.databaseManager = databaseManager;
    }

    public void run() {
        try {
            openServerSocket();
            serverLogger.info("Сервер успешно запущен");
            while (true) {
                FutureManager.checkAllFutures();
                try {
                    forkJoinPool.submit(new ConnectionManager(commandManager, connectToClient(), databaseManager));
                } catch (ConnectionErrorException | SocketTimeoutException e) {
                }
            }
        } catch (OpenServerException e) {
            serverLogger.error("Невозможно запустить сервер");
        }
        stop();
    }

    private void openServerSocket() throws OpenServerException {
        try {
            SocketAddress socketAddress = new InetSocketAddress(port);
            serverLogger.info("Создан соккет");
            serverSocketChannel = ServerSocketChannel.open();
            serverLogger.info("Создан канал");
            serverSocketChannel.bind(socketAddress);
            serverLogger.info("Открыт канал-соккет");
        } catch (IllegalArgumentException e) {
            console.printError("Порт находится за пределами возможных значений");
            serverLogger.info("Порт находится за пределами возможных значений");
            throw new OpenServerException();
        } catch (IOException e) {
            console.printError("При попытке использовать порт " + port + " возникла ошибка");
            serverLogger.error("При попытке использовать порт " + port + " возникла ошибка");
            throw new OpenServerException();
        }
    }

    private SocketChannel connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        try {
            serverSocketChannel.socket().setSoTimeout(100);
            socketChannel = serverSocketChannel.socket().accept().getChannel();
            console.println("Соединение с клиентом установлено");
            serverLogger.info("Соединение с клиентом установлено");
            return socketChannel;
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException();
        } catch (IOException e) {
            serverLogger.error("Ошибка соединения с клиентом");
            throw new ConnectionErrorException();
        }
    }

    private void stop() {
        class ClosedSocketException extends Exception {}
        try {
            if (socketChannel == null) throw new ClosedSocketException();
            socketChannel.close();
            serverSocketChannel.close();
            serverLogger.info("Все соединения закрыты");
        } catch (ClosedSocketException e) {
            serverLogger.error("Сервер еще не запущен => невозможно завершить его работу");
        } catch (IOException e) {
            serverLogger.error("Ошибка завершения работы сервера");
        }
    }
}
