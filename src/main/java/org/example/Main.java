package org.example;

import org.example.auth.AccountManager;
import org.example.database.DatabaseConnection;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection db = new DatabaseConnection();
            db.connect("./Database.db");
            AccountManager accountManager = new AccountManager(db);
            accountManager.init();
            accountManager.register("Darek", "pass");
            db.disconnect();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
}