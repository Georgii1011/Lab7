package org.example.Managers;

import org.example.Connection.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FutureManager {
    private static final Collection<Future<ConnectionManagerPool>> threadFutures = new ArrayList<>();
    static final Logger futureManagerLogger = LoggerFactory.getLogger(FutureManager.class);

    public static void addNewThreadFuture(Future<ConnectionManagerPool> future) {
        threadFutures.add(future);
    }

    public static void checkAllFutures() {
        if (!threadFutures.isEmpty()) {
            threadFutures.forEach(s -> futureManagerLogger.debug(s.toString()));
        }
        threadFutures.stream()
                .filter(Future::isDone)
                .forEach(s -> {
                    try {
                        ConnectionManager.submitNewResponse(s.get());
                    } catch (InterruptedException | ExecutionException e) {
                        futureManagerLogger.debug(e.toString());
                    }
                });
        threadFutures.removeIf(Future::isDone);
    }
}
