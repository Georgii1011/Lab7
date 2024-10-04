package org.example.CommandSpace;


import org.example.Managers.Database.DatabaseManager;

import java.util.Objects;

public class DatabaseHandler {
    private static DatabaseManager databaseManager;
    static {
        databaseManager = new DatabaseManager();
    }
    public static DatabaseManager getDatabaseManager() {
        if (Objects.isNull(databaseManager)) databaseManager = new DatabaseManager();
        return databaseManager;
    }
}
