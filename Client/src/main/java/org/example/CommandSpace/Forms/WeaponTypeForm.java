package org.example.CommandSpace.Forms;

import org.example.CollectionModel.Parametres.WeaponType;
import org.example.Exceptions.FileModeException;
import org.example.CommandSpace.*;

import java.util.Locale;

/**
 * Класс опросника оружия героя
 */
public class WeaponTypeForm extends Form<WeaponType> {
    private final Printable console;
    private final Inputable scanner;

    public WeaponTypeForm(Printable console) {
        if (Console.getFileMode()) {
            this.console = new BlankConsole();
            this.scanner = new ExecuteFileSpace();
        } else {
            this.console = console;
            this.scanner = new ConsoleInput();
        }
    }

    @Override
    public WeaponType build() {
        while (true) {
            console.println("Возможные варинты оружия:");
            console.println(WeaponType.list());
            console.println("Введите вариант оружия:");
            String inputLine = scanner.nextLine().trim();
            if (inputLine.isEmpty()) {
                console.printError("Оружие не может быть null");
            } else {
                try {
                    return WeaponType.valueOf(inputLine.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException e) {
                    console.printError("Такого оружия нет в списке");
                    if (Console.getFileMode()) throw new FileModeException();
                }
            }
        }
    }
}
