package org.example.CommandSpace.Forms;

import org.example.CollectionModel.Parametres.Car;
import org.example.Exceptions.FileModeException;
import org.example.CommandSpace.*;

/**
 * Класс опросника о машине героя
 */
public class CarForm extends Form<Car> {
    private final Printable console;
    private final Inputable scanner;

    public CarForm(Printable console) {
        if (Console.getFileMode()) {
            this.console = new BlankConsole();
            this.scanner = new ExecuteFileSpace();
        } else {
            this.console = console;
            this.scanner = new ConsoleInput();
        }
    }

    @Override
    public Car build() {
        while (true) {
            console.println("Введите имя авто");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                console.printError("Имя не может быть пустым");
                if (Console.getFileMode()) throw new FileModeException();
            } else return new Car(name);
        }
    }
}
