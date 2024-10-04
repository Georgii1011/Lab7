package org.example.Commands;

import org.example.CollectionModel.HumanBeing;
import org.example.CommandSpace.DatabaseHandler;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.List;

/**
 * Класс команды очистки коллекции
 */
public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        List<Long> deletedId = collectionManager.getCollection().stream()
                        .filter(humanBeing -> humanBeing.getUserLogin().equals(request.getUser().getLogin()))
                                .map(HumanBeing::getId).toList();
        if (DatabaseHandler.getDatabaseManager().deleteAllHumanBeing(request.getUser(), deletedId)) {
            collectionManager.removeElements(deletedId);
            return new Response(ResponseStatus.OK, "Ваши элементы успешно удалены");
        }
        return new Response(ResponseStatus.ERROR, "Элементы не удалось удалить");
    }
}
