package org.example.Exceptions;

import java.io.IOException;

/**
 * Исключение ввода недопустимых аргументов
 */
public class InvalidArguments extends IOException {
    public InvalidArguments() {}

    public InvalidArguments(String message) {
        super(message);
    }

    public InvalidArguments(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArguments(Throwable cause) {
        super(cause);
    }
}