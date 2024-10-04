package org.example;

import org.example.CommandSpace.BlankConsole;
import org.example.CommandSpace.Console;
import org.example.CommandSpace.Printable;
import org.example.Exceptions.InvalidArguments;
import org.example.Managers.RuntimeManager;

import java.util.Scanner;

public class Main {
    private static String host;
    private static int port;
    private static Printable console = new BlankConsole();

    public static void main(String[] args) {
        if (!parseHostPort(args)) return;
        console = new Console();
        Client client = new Client(host, port, 5000, 5, console);
        console.println("Добро пожаловать!");
        new RuntimeManager(console, new Scanner(System.in), client).runInteractive();
    }

    public static boolean parseHostPort(String[] args) {
        try {
            if (args.length != 2) throw new InvalidArguments("Передайте хост и порт в аргументах командной строки");
            host = args[0];
            port = Integer.parseInt(args[1]);
            if (port < 0) throw new InvalidArguments("Порт должен быть натуральным числом");
            return true;
        } catch (InvalidArguments e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}