package backend;

import database.DatabaseManager;
import util.PasswordHasher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoginRegistrationEndpoints {

    public static boolean login(String username, String password){
        try {
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            password = PasswordHasher.hashPassword(password);
            String query = "SELECT * FROM Users WHERE Username='" + username + "' AND Password='" + password + "'";
            ResultSet resultSet = statement.executeQuery(query);
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
            Statement statement = connection.createStatement();
            String query = String.format("INSERT INTO USERS VALUES('%s', '%s', '%s', '%s')",username,email,password,formattedDate);
            statement.executeQuery(query);
            query = String.format("INSERT INTO FreeUser VALUES('%s', %d)",username,0);
            statement.executeQuery(query);
            return true;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return false;
    }
}
