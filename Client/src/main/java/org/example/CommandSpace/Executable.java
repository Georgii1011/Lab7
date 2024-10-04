package org.example.CommandSpace;

import org.example.Exceptions.CommandRuntimeError;
import org.example.Exceptions.ExitProgram;
import org.example.Exceptions.InvalidArguments;

/**
 * Интерфейс для исполнения команд
 */
public interface Executable {
    void execute(String args) throws InvalidArguments, ExitProgram, CommandRuntimeError;
}
