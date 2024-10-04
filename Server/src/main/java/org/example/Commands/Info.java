package org.example.Commands;

import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

/**
 * Класс команды для вывода текущей информации о коллекции
 */
public class Info extends Command {
    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        String lastInitTime = (collectionManager.getLastInitTime() == null)
                ? "коллекция в сессии не инициализирована"
                : collectionManager.getLastInitTime();
        String lastSaveTime = (collectionManager.getLastSaveTime() == null)
                ? "-"
                : collectionManager.getLastSaveTime();
        StringBuilder resultInfo = new StringBuilder();
        resultInfo.append("*** Сведения о коллекции ***" + "\n")
                .append("Тип: " + collectionManager.getCollectionType() + "\n")
                .append("Количество элементов: " + collectionManager.getSize() + "\n")
                .append("Дата последней инициализации: " +  lastInitTime + "\n")
                .append("Дата последнего изменения: " + lastSaveTime + "\n");
        return new Response(ResponseStatus.OK, resultInfo.toString());
    }
}
