package backend;

import database.DatabaseManager;
import model.Distributor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DistributorEndpoints {
    private static ResultSet query(String query) {
        try {
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public static List<Distributor> getDistributors() {
        String query = "SELECT Name, Website " +
                "FROM Distributor " +
                "ORDER BY Name ASC";
        return getDistributorsHelper(query);
    }

    public static Distributor getDistributorByName(String name) {
        String query = "SELECT Name, Website FROM Distributor WHERE Name = '%s'";
        List<Distributor> result = getDistributorsHelper(String.format(query, name));

        if (result != null && result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public static List<Distributor> searchDistributors(String keywords) {
        if (keywords.equals("")) {
            return getDistributors();
        }

        String query = "SELECT DISTINCT Name, Website " +
                "FROM Distributor WHERE " +
                "LOWER(Name) LIKE LOWER('%%%s%%') " +
                "ORDER BY Name ASC";
        return getDistributorsHelper(String.format(query, keywords));
    }

    public static void addDistributor(Distributor distributor) {
        String query = "INSERT INTO Distributor VALUES ('%s', '%s')";
        query(String.format(query, distributor.name, distributor.website));
    }

    public static void editDistributor(Distributor distributor) {
        String query = "UPDATE Distributor SET " +
                "Website = '%s' " +
                "WHERE Name = '%s'";
        query(String.format(query, distributor.website, distributor.name));
    }

    public static void removeDistributor(Distributor distributor) {
        String query = "DELETE FROM Distributor WHERE Name = '%s'";
        query(String.format(query, distributor.name));
    }

    // **************** HELPERS ****************

    private static List<Distributor> getDistributorsHelper(String query){
        ResultSet results = query(query);
        try {
            List<Distributor> distributors = new ArrayList<>();

            while (results.next()){
                String name = results.getString("Name");
                String website = results.getString("Website");

                Distributor distributor = new Distributor(name, website);
                distributors.add(distributor);
            }

            results.close();
            return distributors;

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }
}
