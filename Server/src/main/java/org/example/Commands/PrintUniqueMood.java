package org.example.Commands;

import org.example.CollectionModel.HumanBeing;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.Objects;

/**
 * Класс команды для вывода уникальных значений настроения героев
 */
public class PrintUniqueMood extends Command {
    private final CollectionManager collectionManager;

    public PrintUniqueMood(CollectionManager collectionManager) {
        super("print_unique_mood", "вывести уникальные значения поля mood всех элементов коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        if (collectionManager.getSize() == 0) {
            return new Response(ResponseStatus.ERROR, "Коллекция пуста");
        }
        StringBuilder result = new StringBuilder();
        result.append("Уникальные значения поля mood элементов коллекции:\n"); //добавить вариант на пустоту
        collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .map(HumanBeing::getMood)
                .distinct()
                .forEach(x -> result.append(String.valueOf(x) + "\n"));
        return new Response(ResponseStatus.OK, result.toString());
    }
}
