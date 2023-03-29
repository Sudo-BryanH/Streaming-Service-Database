package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String username = "ora_dhrubo10";
    private static final String password = "a13198379";
    private static Connection connection = null;
    private static final DatabaseManager instance = new DatabaseManager();

    private DatabaseManager(){
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            if (connection != null) {
                connection.close();
            }
            System.out.println("\nConnected to Oracle!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DatabaseManager getInstance(){
        return instance;
    }

    public static Connection getConnectionInstance(){
        return connection;
    }

    public static Connection establishConnection() throws SQLException{
        connection = DriverManager.getConnection(ORACLE_URL, username, password);
        connection.setAutoCommit(false);
        return connection;
    }

    public static void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("lost connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
