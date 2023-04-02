package backend;

import database.DatabaseManager;
import model.Playlist;
import model.Song;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LibraryEndpoints {

    public static int getPlaylistCount(String user) {
        int count = -1;
        try {
//            "SELECT p.Name FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'"
            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT * FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'", user);

            Boolean b = statement.execute(query);

            count = statement.getUpdateCount();


        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
        return count;
    }
    public static ArrayList<Playlist> getPlaylists(String user) {
        ArrayList<Playlist> res = new ArrayList<>();
        try {
//            "SELECT p.Name FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'"
            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT p.Name FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'", user);

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Playlist pl = new Playlist(user, rs.getString("Name"));
                res.add(pl);
                pl.setSize(getSongCount(user, rs.getString("Name")));
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
        return res;

    }
    public static ArrayList<Song> getPlaylistSongs(String user, String pname) {
        ArrayList<Song> res = new ArrayList<>();
        try {

            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT ReleaseID, TrackNum, Name, Duration, Genre FROM Song s INNER JOIN PlaylistIsIn pi WHERE pi.Username = '%s' AND pi.Name = '%s'", user, pname);

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Song s = new Song(rs.getInt("ReleaseID"), rs.getInt("TrackNum"), rs.getString("Name"), rs.getInt("Duration"), rs.getString("Genre"));
                res.add(s);

            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
        return res;

    }

    public static ArrayList<Song> getLibrarySongs(String user) {
        ArrayList<Song> res = new ArrayList<>();
        try {

            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT ReleaseID, TrackNum, Name, Duration, Genre" +
                            "FROM Song s INNER JOIN AddsToLibrary a " +
                            "WHERE a.Username = %s", user);

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Song s = new Song(rs.getInt("ReleaseID"), rs.getInt("TrackNum"), rs.getString("Name"), rs.getInt("Duration"), rs.getString("Genre"));
                res.add(s);

            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
        return res;

    }

    private static int getSongCount(String user, String name) {
        int count = -1;
        try {

            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT COUNT(*)" +
                            "FROM Playlist p INNER JOIN PlaylistIsIn pi" +
                            "WHERE p.Username = %s AND p.Name = %s", user, name);
            boolean b = statement.execute(query);
            count = statement.getUpdateCount();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        DatabaseManager.close();
        return count;

    }
}