package ui;

import database.DatabaseManager;
import ui.login.LoginPage;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.getInstance();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Exiting application, closing connection.");
            DatabaseManager.getInstance().close();
        }));

        new LoginPage();
    }
}
