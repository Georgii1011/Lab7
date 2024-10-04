package org.example.CommandSpace;

import org.example.Managers.ScannerManager;

import java.util.Scanner;

/**
 * Класс для работы с пользовательским вводом
 */
public class ConsoleInput implements Inputable {
    private static final Scanner userScanner = ScannerManager.getUserScanner();

    @Override
    public String nextLine() {
        return userScanner.nextLine();
    }
}
