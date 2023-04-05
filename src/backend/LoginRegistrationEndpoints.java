package backend;

import database.DatabaseManager;
import util.PasswordHasher;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoginRegistrationEndpoints {

    public static boolean login(String username, String password){
        try {
            Connection connection = DatabaseManager.getInstance().getConnection();
            password = PasswordHasher.hashPassword(password);
            String query = "SELECT * FROM Users WHERE Username=? AND Password=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();
            boolean status = resultSet.next();
            return status;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return false;
    }

    public static boolean register(String username, String password, String email){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        password = PasswordHasher.hashPassword(password);
        try{
            Connection connection = DatabaseManager.getInstance().getConnection();
            String query = "INSERT INTO USERS VALUES(?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            statement.setString(2,email);
            statement.setString(3,password);
            statement.setString(4,formattedDate);
            statement.executeQuery();
            query = "INSERT INTO FreeUser VALUES(?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1,username);
            statement.setInt(2,0);
            statement.executeQuery();
            return true;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return false;
    }
}
