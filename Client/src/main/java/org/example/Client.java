package org.example;

import org.example.CommandSpace.Printable;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private Printable console;
    private Socket socket;
    private ObjectOutputStream serverOut;
    private ObjectInputStream serverIn;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, Printable console) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.console = console;
    }

    public Response getResponse(Request request) {
        while (true) {
            try {
                if (Objects.isNull(serverOut) || Objects.isNull(serverIn)) throw new IOException();
                if (request.isEmpty()) return new Response(ResponseStatus.WRONG_ARGS, "Пустой запрос");
                serverOut.writeObject(request);
                serverOut.flush();
                Response response = (Response) serverIn.readObject();
                disconnectServer();
                reconnectionAttempts = 0;
                return response;
            } catch (IOException e) {
                if (reconnectionAttempts == 0) {
                    reconnectionAttempts++;
                    connectServer();
                    continue;
                } else console.printError("Разорвано соединение с сервером");
                try {
                    reconnectionAttempts++;
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        console.printError("Превышено максимальное количество попыток соединения с сервером");
                        return new Response(ResponseStatus.EXIT);
                    }
                    console.println("Повторная попытка подключения через " + reconnectionTimeout / 1000 + " секунд");
                    Thread.sleep(reconnectionTimeout);
                    connectServer();
                } catch (Exception exc) {
                    console.printError("Неудачная попытка соединения с сервером");
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void connectServer() {
        try {
            if (reconnectionAttempts > 1) console.println("Производится попытка повторного подключения к серверу");
            socket = new Socket(host, port);
            this.serverOut = new ObjectOutputStream(socket.getOutputStream());
            this.serverIn = new ObjectInputStream(socket.getInputStream());
        } catch (IllegalArgumentException e) {
            console.printError("Введен некорректный адрес сервера");
        } catch (IOException e) {
            console.printError("Ошибка соединения с сервером");
        }
    }

    public void disconnectServer() {
        try {
            socket.close();
            serverOut.close();
            serverIn.close();
        } catch (IOException e) {
            console.printError("Отключено от сервера");
        }
    }
}
