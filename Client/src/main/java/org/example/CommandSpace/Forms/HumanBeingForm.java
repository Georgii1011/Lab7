package org.example.CommandSpace.Forms;

import org.example.CollectionModel.HumanBeing;
import org.example.CollectionModel.Parametres.Car;
import org.example.CollectionModel.Parametres.Coordinates;
import org.example.CollectionModel.Parametres.Mood;
import org.example.CollectionModel.Parametres.WeaponType;
import org.example.Exceptions.FileModeException;
import org.example.CommandSpace.*;

import java.util.Date;

/**
 * Класс опросника для создания элемента коллекции
 */
public class HumanBeingForm extends Form<HumanBeing> {
    private final Printable console;
    private final Inputable scanner;

    public HumanBeingForm(Printable console) {
        if (Console.getFileMode()) {
            this.console = new BlankConsole();
            this.scanner = new ExecuteFileSpace();
        } else {
            this.console = console; //execute file bug
            this.scanner = new ConsoleInput();
        }
    }

    @Override
    public HumanBeing build() {
        return new HumanBeing(
                askName(),
                askCoordinates(),
                new Date(),
                askRealHero(),
                askHasToothpick(),
                askImpactSpeed(),
                askWeaponType(),
                askMood(),
                askCar()
        );
    }

    private String askName() {
        while (true) {
            console.println("Введите имя:");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                console.printError("Имя не может быть пустым");
                if (Console.getFileMode()) throw new FileModeException();
            } else return name;
        }
    }

    private Coordinates askCoordinates() {
        return new CoordinatesForm(console).build();
    }

    private Boolean askRealHero() {
        while (true) {
            console.println("Реален ли герой? 0 - нет, другое целое число типа int - да");
            String inputLine = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(inputLine);
                return (number == 0) ? Boolean.FALSE : Boolean.TRUE;
            } catch (NumberFormatException e) {
                console.printError("Ответ на вопрос должен быть целым числом типа int");
                if (Console.getFileMode()) throw new FileModeException();
            }
        }
    }

    private Boolean askHasToothpick() {
        while (true) {
            console.println("Есть ли у героя зубочистка? 0 - нет, другое целое число типа int - да");
            String inputLine = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(inputLine);
                return (number == 0) ? Boolean.FALSE : Boolean.TRUE;
            } catch (NumberFormatException e) {
                console.printError("Ответ на вопрос должен быть целым числом типа int");
                if (Console.getFileMode()) throw new FileModeException();
            }
        }
    }

    private Integer askImpactSpeed() {
        while (true) {
            console.println("Введите скорость удара (тип int >= 0):");
            String inputLine = scanner.nextLine().trim();
            try {
                int impactSpeed = Integer.parseInt(inputLine);
                if (impactSpeed < 0) {
                    console.printError("Скорость удара не может быть отрицательной");
                    continue;
                }
                return impactSpeed;
            } catch (NumberFormatException e) {
                console.printError("Скорость удара должна быть числом типа int");
                if (Console.getFileMode()) throw  new FileModeException();
            }
        }
    }

    private WeaponType askWeaponType() {
        return new WeaponTypeForm(console).build();
    }

    private Mood askMood() {
        return new MoodForm(console).build();
    }

    private Car askCar() {
        return new CarForm(console).build();
    }
}
