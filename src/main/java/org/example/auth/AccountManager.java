package org.example.auth;

import org.example.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager {
    DatabaseConnection databaseConnection;

    public AccountManager(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean register(String name, String password) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String insertSQL = "INSERT INTO accounts (username, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return true;
    }

    public void init() throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String createSQLTable = "CREATE TABLE IF NOT EXISTS accounts( " +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)";
        PreparedStatement statement = connection.prepareStatement(createSQLTable);
        statement.executeUpdate();
        databaseConnection.disconnect();
    }

    public boolean authenticate(String username, String password) throws SQLException {
        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement("SELECT password from accounts where username = ?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            return password.equals(storedPassword);
        }
        return false;
    }

    public Account getAccount(String username) throws SQLException {
        int id;
        String usernameAccount;
        String url = "SELECT * FROM accounts WHERE username=?";
        PreparedStatement statement = databaseConnection.getConnection().prepareStatement(url);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery(url);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
            usernameAccount = resultSet.getString(2);
            return new Account(id, usernameAccount);
        } else return null;
    }
}
