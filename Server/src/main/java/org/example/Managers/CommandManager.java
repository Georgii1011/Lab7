package org.example.Managers;

import org.example.Commands.Command;
import org.example.Commands.Interfaces.Editor;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Connection.User;
import org.example.Exceptions.CommandRuntimeError;
import org.example.Exceptions.ExitProgram;
import org.example.Exceptions.InvalidArguments;
import org.example.Exceptions.NoSuchCommand;
import org.example.Managers.Database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

record HistoryCommand(String userLogin, String command) {
}

/**
 * Класс менеджера комманд, содержащий информацию о них и их историю
 */
public class CommandManager {
    private final HashMap<String, Command> commandsHashMap = new HashMap<>();
    private final List<HistoryCommand> commandHistory = new ArrayList<>();
    static final Logger commandManagerLogger = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager() {
    }

    public void addCommandToHashMap(Command command) {
        this.commandsHashMap.put(command.getName(), command);
        commandManagerLogger.info("Новая команда в списке доступных");
    }

    public void addCommandToHashMap(Collection<Command> commands) {
        this.commandsHashMap.putAll(commands.stream().collect((Collectors.toMap(Command::getName, s -> s))));
        commandManagerLogger.info("Новые команды в списке доступных");
    }

    public Collection<Command> getCommandsHashMap() {
        return commandsHashMap.values();
    }

    public void addToHistory(User user, String line) {
        if (line.isBlank()) return;
        if (commandsHashMap.get(line) != null) {
            commandHistory.add(new HistoryCommand(user.getLogin(), line));
        }
    }

    public List<String> getCommandHistory(User user) {
        return commandHistory.stream()
                .filter(historyCommand -> historyCommand.userLogin().equals(user.getLogin()))
                .map(HistoryCommand::command)
                .toList();
    }

    public Response execute(Request request) throws ExitProgram, InvalidArguments, NoSuchCommand, CommandRuntimeError {
        Command command = commandsHashMap.get(request.getCommandName());
        if (command == null) {
            throw new NoSuchCommand();
        }
        Response response = command.execute(request);
        commandManagerLogger.debug("ответ сервера получен");
        commandManagerLogger.info("Выполнение команды: " + command);
        return response;
    }
}
