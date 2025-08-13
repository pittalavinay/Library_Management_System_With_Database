package com.library;

import com.library.ui.ConsoleUI;

/**
 * Main class for the Library Management System.
 * This is the entry point of the application.
 */
public class Main {

    /**
     * Main method - entry point of the application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Starting Library Management System...");
        
        try {
            // Create and start the console UI
            ConsoleUI consoleUI = new ConsoleUI();
            consoleUI.start();
            
        } catch (Exception e) {
            System.err.println("Fatal error occurred while running the application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("Library Management System stopped.");
    }
}
