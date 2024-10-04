package org.example.Commands;

import org.example.CommandSpace.DatabaseHandler;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.Objects;

/**
 * Класс команды для обновления элемента коллекции по его id
 */
public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update", "id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (request.getArgs().isBlank()) throw new InvalidArguments();
        try {
            long id = Long.parseLong(request.getArgs().trim());
            if (collectionManager.checkNonExistById(id)) throw new NoSuchId();
            if (!Objects.equals(collectionManager.getById(id).getUserLogin(), request.getUser().getLogin())) {
                return new Response(ResponseStatus.ERROR, "Убедитесь, что элемент точно ваш");
            }
            if (Objects.isNull(request.getHumanBeing())) {
                return new Response(ResponseStatus.ASKING_OBJECT, "Для выполнения команды " + getName() + " нужен объект");
            }
            if (DatabaseHandler.getDatabaseManager().updateHumanBeing(id, request.getHumanBeing(), request.getUser())) {
                commandLogger.debug("Обновить в бд получилось");
                collectionManager.editById(id, request.getHumanBeing());
                commandLogger.debug("Обновить в коллекции получилось");
                return new Response(ResponseStatus.OK, "Объект успешно обновлен");
            } else {
                return new Response(ResponseStatus.ERROR, "Не удалось обновить данный элемент");
            }
        } catch (NoSuchId e) {
            return new Response(ResponseStatus.ERROR, "Нет элемента с таким id");
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.WRONG_ARGS, "id должен быть числом типа long");
        }
    }
}