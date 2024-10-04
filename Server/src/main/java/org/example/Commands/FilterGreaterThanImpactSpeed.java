package org.example.Commands;

import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс команды для вывода элементов коллекции, скорость героя которых больше заданной
 */
public class FilterGreaterThanImpactSpeed extends Command {
    private final CollectionManager collectionManager;

    public FilterGreaterThanImpactSpeed(CollectionManager collectionManager) {
        super("filter_greater_than_impact_speed", "impactSpeed",
                "вывести элементы значение поля impactSpeed (int) которых больше заданного");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (request.getArgs().isBlank()) throw new InvalidArguments();
        try {
            int valueMark = Integer.parseInt(request.getArgs().trim());
            return new Response(ResponseStatus.OK, "Элементы со значением поля impactSpeed, большим " + valueMark + ":\n" +
                    collectionManager.getCollection().stream()
                            .filter(Objects::nonNull)
                            .filter(i -> i.getImpactSpeed() > valueMark)
                            .map(Object::toString)
                            .collect(Collectors.joining("\n")));
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.WRONG_ARGS, "Аргумент должен быть числом типа int");
        }
    }
}
