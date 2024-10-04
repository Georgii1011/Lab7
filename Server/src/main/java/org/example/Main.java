package org.example;

import org.example.CommandSpace.DatabaseHandler;
import org.example.Commands.*;
import org.example.Managers.CollectionManager;
import org.example.Managers.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main extends Thread {
    public static final String HASHING = "SHA-384";
    public static final String DATABASE_HELIOS_URL = "jdbc:postgresql://pg:5432/studs";
    public static final String  cfgshka = "/Users/georgij/Documents/Учеба_ИТМО/lab7/data.cfg";

    public static int port = 28008;
    public static final int connectionTimeout = 60 * 1000;
    static final Logger mainLogger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        mainLogger.info("Попытка запуска сервера");
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        CollectionManager collectionManager = new CollectionManager();
        CommandManager commandManager = new CommandManager();

        commandManager.addCommandToHashMap(List.of(
                new Help(commandManager),
                new Info(collectionManager),
                new Show(collectionManager),
                new AddElement(collectionManager),
                new Update(collectionManager),
                new RemoveById(collectionManager),
                new Clear(collectionManager),
                new Execute(),
                new Exit(),
                new RemoveLast(collectionManager),
                new AddIfMax(collectionManager),
                new History(commandManager),
                new SumOfImpactSpeed(collectionManager),
                new FilterGreaterThanImpactSpeed(collectionManager),
                new PrintUniqueMood(collectionManager),
                new Ping(),
                new Register(DatabaseHandler.getDatabaseManager())
        ));

        Server server = new Server(commandManager, DatabaseHandler.getDatabaseManager());
        server.run();
    }
}