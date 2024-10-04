package org.example.Commands;

import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.Database.DatabaseManager;

import java.sql.SQLException;

public class Register extends Command {
    DatabaseManager databaseManager;

    public Register(DatabaseManager databaseManager) {
        super("register", "регистрация пользователя");
        this.databaseManager = databaseManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        this.commandLogger.debug("Получен пользователь: " + request.getUser());
        try {
            databaseManager.addUser(request.getUser());
        } catch (SQLException e) {
            commandLogger.error("Не удалось добавить пользователя в таблицу");
            return new Response(ResponseStatus.FAIL_LOGIN, "Проверьте валидность пароля");
        }
        return new Response(ResponseStatus.OK, "Регистрация прошла успешно");
    }
}
