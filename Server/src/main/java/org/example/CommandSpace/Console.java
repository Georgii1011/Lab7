package org.example.CommandSpace;

/**
 * Класс консоли для вывода
 */
public class Console implements Printable {
    private static boolean fileMode = false;

    public static boolean getFileMode() {
        return fileMode;
    }

    /**
     * Задать режим работы с файлом
     * @param fileMode
     */
    public static void setFileMode(boolean fileMode) {
        Console.fileMode = fileMode;

    }

    @Override
    public void println(String stringLine) {
        System.out.println(stringLine);
    }

    @Override
    public void print(String string) {
        System.out.print(string);
    }

    @Override
    public void printError(String stringError) {
        System.out.println("\u001B[31m" + stringError + "\u001B[0m");
    }
}
