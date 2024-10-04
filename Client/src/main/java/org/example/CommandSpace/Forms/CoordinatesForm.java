package org.example.CommandSpace.Forms;

import org.example.CollectionModel.Parametres.Coordinates;
import org.example.Exceptions.FileModeException;
import org.example.CommandSpace.*;

/**
 * Класс опросника координат
 */
public class CoordinatesForm extends Form<Coordinates> {
    private final Printable console;
    private final Inputable scanner;

    public CoordinatesForm(Printable console) {
        if (Console.getFileMode()) {
            this.console = new BlankConsole();
            this.scanner = new ExecuteFileSpace();
        } else {
            this.console = console;
            this.scanner = new ConsoleInput();
        }
    }

    @Override
    public Coordinates build() {
        return new Coordinates(askX(), askY());
    }

    private int askX() {
        while (true) {
            console.println("Введите координату 'x' (тип int):");
            String inputLine = scanner.nextLine().trim();
            try {
                return Integer.parseInt(inputLine);
            } catch (NumberFormatException e) {
                console.printError("'x' должно быть числом типа int");
                if (Console.getFileMode()) throw  new FileModeException();
            }
        }
    }

    private double askY() {
        while (true) {
            console.println("Введите координату 'y' > -157 (тип double):");
            String inputLine = scanner.nextLine().trim().replace(",", ".");
            try {
                double y = Double.parseDouble(inputLine);
                if (y <= -157) {
                    console.printError("'y' должно быть > -157");
                } else return y;
            } catch (NumberFormatException e) {
                console.printError("'y' должно быть числом типа double");
                if (Console.getFileMode()) throw  new FileModeException();
            }
        }
    }
}
