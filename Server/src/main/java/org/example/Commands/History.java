package org.example.Commands;

import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CommandManager;

import java.util.List;

/**
 * Класс команды для вывода 9 последних команд из истории
 */
public class History extends Command {
    private final CommandManager commandManager;

    public History(CommandManager commandManager) {
        super("history", "вывести последние 9 команд (без их аргументов)");
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        List<String> history = commandManager.getCommandHistory(request.getUser());
        return new Response(ResponseStatus.OK,
                String.join("\n", history.subList(Math.max(history.size() - 9, 0), history.size())));
    }
}
