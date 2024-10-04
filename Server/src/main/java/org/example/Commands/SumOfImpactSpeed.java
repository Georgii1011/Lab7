package org.example.Commands;

import org.example.CollectionModel.HumanBeing;
import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.CollectionManager;

import java.util.Objects;

/**
 * Класс команды для вывода суммы скоростей всех героев коллекции
 */
public class SumOfImpactSpeed extends Command {
    private final CollectionManager collectionManager;

    public SumOfImpactSpeed(CollectionManager collectionManager) {
        super("sum_of_impact_speed", "вывести сумму значений поля impactSpeed для всех элементов коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        if (!request.getArgs().isBlank()) throw new InvalidArguments();
        if (collectionManager.getCollection().isEmpty()) {
            return new Response(ResponseStatus.ERROR, "Коллекция пуста");
        }
        return new Response(ResponseStatus.OK, "Сумма значений поля impactSpeed: " +
                collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .mapToInt(HumanBeing::getImpactSpeed).sum());
    }
}