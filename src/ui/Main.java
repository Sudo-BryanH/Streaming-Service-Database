package ui;

import database.DatabaseManager;
import ui.login.LoginPage;

public class Main {
    public static void main(String[] args) {
        DatabaseManager db = DatabaseManager.getInstance();
        System.out.println("Hello world!");
        new LoginPage();
    }
}
