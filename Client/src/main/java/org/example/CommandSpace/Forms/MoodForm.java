package org.example.CommandSpace.Forms;

import org.example.CollectionModel.Parametres.Mood;
import org.example.Exceptions.FileModeException;
import org.example.CommandSpace.*;

import java.util.Locale;

/**
 * Класс опросника настроения героя
 */
public class MoodForm extends Form<Mood> {
    private final Printable console;
    private final Inputable scanner;

    public MoodForm(Printable console) {
        if (Console.getFileMode()) {
            this.console = new BlankConsole();
            this.scanner = new ExecuteFileSpace();
        } else {
            this.console = console;
            this.scanner = new ConsoleInput();
        }
    }

    @Override
    public Mood build() {
        while (true) {
            console.println("Возможные варианты настроения:");
            console.println(Mood.list());
            console.println("(Для ввода значения null используйте пустую строку)");
            console.println("Введите настроение:");
            String inputLine = scanner.nextLine().trim();
            if (inputLine.isEmpty()) {
                return null;
            } else {
                try {
                    return Mood.valueOf(inputLine.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException e) {
                    console.printError("Такого настроения нет в списке");
                    if (Console.getFileMode()) throw new FileModeException();
                }
            }
        }
    }
}
