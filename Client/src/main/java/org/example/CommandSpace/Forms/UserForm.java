package org.example.CommandSpace.Forms;

import org.example.CommandSpace.*;
import org.example.Connection.User;
import org.example.Exceptions.FileModeException;

import java.util.Objects;

public class UserForm  extends Form<User> {
    private final Printable console;
    private final Inputable scanner;

    public UserForm(Printable console) {
        if (Console.getFileMode()) {
            this.console = new BlankConsole();
            this.scanner = new ExecuteFileSpace();
        } else {
            this.console = console;
            this.scanner = new ConsoleInput();
        }
    }

    @Override
    public User build() {
        return new User(askLogin(), askPassword());
    }

    public boolean askIfLogin() {
        while (true) {
            console.println("Есть ли у вас аккаунт? [да/нет]");
            String line = scanner.nextLine().trim().toLowerCase();
            switch (line) {
                case "да", "yes", "y": return true;
                case "нет", "no", "n": return false;
                default: console.printError("Ответ не распознан");
            }
        }
    }

    public String askLogin() {
        String login;
        while (true) {
            console.println("Введите ваш логин");
            login = scanner.nextLine().trim();
            if (login.isEmpty()) {
                console.printError("Логин не может быть пустым");
                if (Console.getFileMode()) throw new FileModeException();
            } else return login;
        }
    }

    public String askPassword() {
        String password;
        while (true) {
            console.println("Введите ваш пароль");
            password = (Objects.isNull(System.console()))
                    ? scanner.nextLine().trim()
                    : new String(System.console().readPassword());
            if (password.isEmpty()) {
                console.printError("Пароль не может быть пустым");
                if (Console.getFileMode()) throw new FileModeException();
            } else return password;
        }
    }














}
