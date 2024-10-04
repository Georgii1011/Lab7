package org.example.Managers;

import org.example.CommandSpace.RequestHandler;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Managers.Database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ConnectionManager implements Runnable {
    private final CommandManager commandManager;
    private final DatabaseManager databaseManager;
    private static final ExecutorService thread = Executors.newSingleThreadExecutor();
    private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private final SocketChannel clientSocket;

    static final Logger connectionManagerLogger = LoggerFactory.getLogger(ConnectionManager.class);

    public ConnectionManager(CommandManager commandManager, SocketChannel clientSocket, DatabaseManager databaseManager) {
        this.commandManager = commandManager;
        this.clientSocket = clientSocket;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {
        Request userRequest = null;
        Response responseToUser = null;
        try {
            ObjectInputStream clientReader = new ObjectInputStream(clientSocket.socket().getInputStream());
            ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.socket().getOutputStream());
            while (true) {
                userRequest = (Request) clientReader.readObject();
                connectionManagerLogger.info("Получен запрос с командой " + userRequest.getCommandName(), userRequest);
                if (!databaseManager.confirmUser(userRequest.getUser()) && !userRequest.getCommandName().equals("register")) {
                    connectionManagerLogger.info("Пользователь не идентифицирован");
                    responseToUser = new Response(ResponseStatus.FAIL_LOGIN, "Неопознанный пользователь");
                    submitNewResponse(new ConnectionManagerPool(responseToUser, clientWriter));
                } else {
                    FutureManager.addNewThreadFuture(thread.submit(new RequestHandler(commandManager, userRequest, clientWriter)));
                }
            }
        } catch (ClassNotFoundException e) {
            connectionManagerLogger.error("Ошибка чтения полученных данных");
        } catch (CancellationException e) {
            connectionManagerLogger.error("Возникла ошибка многопоточности");
        } catch (InvalidClassException | NotSerializableException e) {
            connectionManagerLogger.error("Ошибка отправки данных пользователю");
        } catch (IOException e) {
            if (userRequest == null) {
                connectionManagerLogger.error("Разрыв соединения с пользователем");
            } else {
                connectionManagerLogger.info("Клиент успешно отключен от сервера");
            }
        }
    }

    public static void submitNewResponse(ConnectionManagerPool connectionManagerPool) {
        cachedThreadPool.submit(() -> {
            try {

                connectionManagerPool.objectOutputStream().writeObject(connectionManagerPool.response());
                connectionManagerPool.objectOutputStream().flush();
            } catch (IOException e) {
                connectionManagerLogger.error("Отправить ответ пользователю не удалось");
            }
        });
    }
}
