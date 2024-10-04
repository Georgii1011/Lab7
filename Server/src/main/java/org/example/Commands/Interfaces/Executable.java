package org.example.Commands.Interfaces;

import org.example.Connection.Request;
import org.example.Connection.Response;
import org.example.Exceptions.CommandRuntimeError;
import org.example.Exceptions.ExitProgram;
import org.example.Exceptions.InvalidArguments;

/**
 * Интерфейс для исполнения команд
 */
public interface Executable {
    Response execute(Request request) throws InvalidArguments;
}
