package org.example.CommandSpace;

import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.CommandRuntimeError;
import org.example.Exceptions.ExitProgram;
import org.example.Exceptions.InvalidArguments;
import org.example.Exceptions.NoSuchCommand;
import org.example.Managers.CommandManager;
import org.example.Managers.ConnectionManagerPool;

import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<ConnectionManagerPool> {
    private final CommandManager commandManager;
    private Request request;
    private ObjectOutputStream objectOutputStream;

    public RequestHandler(CommandManager commandManager, Request request, ObjectOutputStream objectOutputStream) {
        this.commandManager = commandManager;
        this.request = request;
        this.objectOutputStream = objectOutputStream;
    }


    @Override
    public ConnectionManagerPool call() {
        try {
            commandManager.addToHistory(request.getUser(), request.getCommandName());
            return new ConnectionManagerPool(commandManager.execute(request), objectOutputStream);
        } catch (InvalidArguments e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.WRONG_ARGS, "Неверно использованы аргументы команды"), objectOutputStream);
        } catch (CommandRuntimeError e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.ERROR, "Ошибка исполнения команды"), objectOutputStream);
        } catch (NoSuchCommand e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.ERROR, "Такой команды нет"), objectOutputStream);
        } catch (ExitProgram e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.EXIT), objectOutputStream);
        }
    }
}
