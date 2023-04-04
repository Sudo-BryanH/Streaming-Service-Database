package backend;

import database.DatabaseManager;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;

public class QueryEndpoints {

    public static ArrayList<Pair<String, String>> getUser(boolean matter, boolean fp) {
        ArrayList<Pair<String, String>> res = new ArrayList<>();
        try {
//            "SELECT p.Name FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'"
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String queryFree = String.format(
                    "SELECT u.Username, u.Email FROM Users u, FreeUser f WHERE u.Username = f.Username");
            String queryPremium = String.format(
                    "SELECT u.Username, u.Email FROM Users u, PremiumUser p WHERE u.Username = p.Username");
            String queryAll = String.format("SELECT u.Username, u.Email FROM Users u");
            ResultSet rs = null;
            if (matter) {
                if (fp) {   // fp = TRUE = premium
                    rs = statement.executeQuery(queryPremium);
                } else {
                    rs = statement.executeQuery(queryFree);
                }
            } else {
                rs = statement.executeQuery(queryAll);
            }

            while (rs.next()) {
                res.add(new Pair<>(rs.getString("Email"), rs.getString("Username")));
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return res;

    }


    public static int countUsers(boolean matter, boolean fp) {
        int res = -1;
        try {
//            "SELECT p.Name FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'"
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String queryFree = String.format(
                    "SELECT COUNT(*) as count FROM Users u, FreeUser f WHERE u.Username = f.Username");
            String queryPremium = String.format(
                    "SELECT COUNT(*) as count FROM Users u, PremiumUser p WHERE u.Username = p.Username");
            String queryAll = String.format(
                    "SELECT COUNT(*) as count FROM Users u");
            ResultSet rs = statement.executeQuery(queryAll);
            if (matter) {
                if (fp) {   // fp = TRUE = premium
                    rs = statement.executeQuery(queryPremium);
                } else {
                    rs = statement.executeQuery(queryFree);
                }
            }

            while (rs.next()) {
                res = rs.getInt("count");
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return res;

    }
}
