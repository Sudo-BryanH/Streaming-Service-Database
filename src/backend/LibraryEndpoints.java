package backend;

import database.DatabaseManager;
import model.Playlist;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LibraryEndpoints {

    public static ArrayList<Playlist> getPlaylists(String user) {
        ArrayList<Playlist> res = new ArrayList<>();
        try {

            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT Name" +
                    "FROM User u, Playlist p " +
                    "WHERE u.Username = %d AND u.Username = p.Username", user);

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Playlist pl = new Playlist(user, rs.getString("Name"));
                res.add(pl);
                pl.setSize(getPlaylistCount(user, rs.getString("Name")));
            }
        } catch(SQLException exception) {
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
        return res;

    }

    private static int getPlaylistCount(String user, String name) {
        int count = -1;
        try {

            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT COUNT(*)" +
                            "FROM Playlist p INNER JOIN PlaylistIsIn pi" +
                            "WHERE p.Username = %s AND p.Name = %s", user, name);
            ResultSet rs = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        DatabaseManager.close();
        return count;

    }
