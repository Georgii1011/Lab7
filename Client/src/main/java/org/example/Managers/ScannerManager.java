package org.example.Managers;

import java.util.Scanner;

/**
 * Менеджера консольного ввода
 */
public class ScannerManager {
    public static Scanner userScanner = new Scanner(System.in);

    public static Scanner getUserScanner() {
        return userScanner;
    }
}
