package backend;

import database.DatabaseManager;
import javafx.util.Pair;
import model.User;

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
                res.add(new Pair<>(rs.getString("Username"), rs.getString("Email")));
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

    public static User findUserInfo(String username) {
        User u = new User(username);
        try {
//            "SELECT p.Name FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'"
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String emailQ = String.format("SELECT Email FROM Users u WHERE u.Username = '%s'", username);
            String CreationQ = String.format("SELECT CreationDate FROM Users u WHERE u.Username = '%s'", username);
            ResultSet rs = statement.executeQuery(emailQ);
            rs.next();
            u.setEmail(rs.getString("Email"));

            rs = statement.executeQuery(CreationQ);
            rs.next();
            u.setCreationDate(rs.getDate("CreationDate"));

            String freeQuery = String.format("SELECT * FROM FreeUser f WHERE f.Username = '%s'", username);

            rs = statement.executeQuery(freeQuery);
            if (rs.next()) {
                u.setPremium(false);
                u.setAdsServed(rs.getInt(rs.getInt("AdsServed")));
            } else {
                String premQuery = String.format("SELECT * FROM PremiumUser p WHERE p.Username = '%s'", username);
                rs = statement.executeQuery(premQuery);

                u.setPremium(true);
                u.setSubStart(rs.getDate("SubStartDate"));
                u.setSubEnd(rs.getDate("SubEndDate"));
            }


        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }






        return u;
    }

    public static ArrayList<Pair<String, Integer>> mostLikedGenres(String username) {
        ArrayList<Pair<String, Integer>> topFiveGenres = new ArrayList<>();
        try {
//            "SELECT p.Name FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'"
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();

            String query = String.format(
"SELECT Genre, COUNT(*) as count FROM Song s, AddsToLibrary a, Users u WHERE a.Username = u.Username AND u.Username = '%s' AND a.TrackNum = s.TrackNum AND a.ReleaseID = a.ReleaseID GROUP BY s.Genre HAVING COUNT(*) > 1",
                    username);
            ResultSet rs = statement.executeQuery(query);

        int counter = 5;

        while (rs.next() && counter > 0) {
            topFiveGenres.add(new Pair<String, Integer>(rs.getString("Genre"), rs.getInt("count")));
            counter--;
        }



        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return topFiveGenres;

    }
    // TODO maybe use a string array to list what needs to be considered for each part
    /*
        SELECT Username, COUNT(*)
        FROM Playlist p
        GROUP BY p.Username



        SELECT Username, COUNT(*)
        FROM AddsToLibrary a
        GROUP BY a.Username


     */
    public static ArrayList<Pair<String, Integer>> countConditionGroupBy(String[] condition) {
        ArrayList<Pair<String, Integer>> result = new ArrayList<>();
        try {
//            "SELECT p.Name FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'"
            Connection connection = DatabaseManager.getInstance().getConnection();
           Statement statement = connection.createStatement();

      //      String query = "SELECT ? as tabOne, COUNT(*) as count FROM ? ? GROUP BY ?";
//            PreparedStatement ps = connection.prepareStatement(query);
//            ps.setString(1, condition[0]);
//            ps.setString(2, condition[1]);
//            ps.setString(3, condition[2]);
//            ps.setString(4, condition[3]);
//            ResultSet rs = ps.executeQuery();
            String query = String.format(
                    "SELECT %s as tabOne, COUNT(*) as count FROM %s %s GROUP BY %s", condition[0], condition[1], condition[2], condition[3]);
            ResultSet rs = statement.executeQuery(query);

           int count = 0;

            while (rs.next()) {
                try {
                    result.add(new Pair<String, Integer>(Integer.toString(rs.getInt("tabOne")), rs.getInt("count")));
                } catch (SQLException e) {
                    result.add(new Pair<String, Integer>(rs.getString("tabOne"), rs.getInt("count")));
                }
                count++;
            }
            System.out.println(count);



        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return result;

    }


}
