package org.example.CommandSpace;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayDeque;

/**
 * Класс для работы с файлами исполняемых скриптов
 */
public class ExecuteFileSpace implements Inputable {
    private static final ArrayDeque<String> paths = new ArrayDeque<>();
    private static final ArrayDeque<BufferedReader> fileReader = new ArrayDeque<>();

    public static void pushFile(String path) throws FileNotFoundException {
        paths.push(String.valueOf(Paths.get(path)));
        fileReader.push(new BufferedReader(new InputStreamReader(new FileInputStream(path))));
    }

    public static void popFile() throws IOException {
        fileReader.getFirst().close();
        fileReader.pop();
        if (!paths.isEmpty()) {
            paths.pop();
        }
    }

    public static void popRecursion() {
        if (!paths.isEmpty()) {
            paths.pop();
        }
    }

    public File getFile() {
        return new File(paths.getLast());
    }

    public static String readLine() throws IOException {
        return fileReader.getFirst().readLine();
    }

    /**
     * Метод для обнаружения рекурсий в скриптах
     */
    public static boolean isFileRepeat(String path) {
        return paths.contains(new File(path).getAbsolutePath());
    }

    @Override
    public String nextLine() {
        try {
            return readLine();
        } catch (IOException e) {
            return "";
        }
    }
}