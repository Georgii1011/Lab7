package org.example.Commands;

import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Connection.ResponseStatus;
import org.example.Exceptions.InvalidArguments;

public class Ping extends Command {
    public Ping() {
        super("ping", "проверить доступность сервера");
    }

    @Override
    public Response execute(Request request) throws InvalidArguments {
        return new Response(ResponseStatus.OK, "Сервер доступен");
    }
}
