package org.example.CommandSpace;

/**
 * Интерфейс для вывода в консоль
 */
public interface Printable {
    void println(String stringLine);
    void print(String string);
    void printError(String stringError);
}
