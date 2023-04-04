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
            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT COUNT(*) as count FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'", user);

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                count = rs.getInt("count");
            }


        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return count;
    }
    public static ArrayList<Playlist> getPlaylists(String user) {
        ArrayList<Playlist> res = new ArrayList<>();
        try {
//            "SELECT p.Name FROM Users u, Playlist p WHERE u.Username = p.Username AND p.Username = '%s'"
            Connection connection = DatabaseManager.getInstance().getConnection();
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

        return res;

    }
    public static ArrayList<Song> getPlaylistSongs(String user, String pname) {
        ArrayList<Song> res = new ArrayList<>();
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT s.ReleaseID, s.TrackNum, s.Name, s.Duration, s.Genre FROM Song s, PlaylistIsIn pi WHERE s.ReleaseID = pi.ReleaseID AND pi.TrackNum = s.TrackNum AND pi.Username = '%s' AND pi.Name = '%s'", user, pname);

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Song s = new Song(rs.getInt("ReleaseID"), rs.getInt("TrackNum"), rs.getString("Name"), rs.getInt("Duration"), rs.getString("Genre"));
                res.add(s);

            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return res;

    }

    public static ArrayList<Song> getLibrarySongs(String user) {
        ArrayList<Song> res = new ArrayList<>();
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT s.ReleaseID, s.TrackNum, s.Name, s.Duration, s.Genre FROM Song s , AddsToLibrary a WHERE s.ReleaseID = a.ReleaseID AND a.TrackNum = s.TrackNum AND a.Username = '%s'", user);

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Song s = new Song(rs.getInt("ReleaseID"), rs.getInt("TrackNum"), rs.getString("Name"), rs.getInt("Duration"), rs.getString("Genre"));
                res.add(s);

            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

        return res;

    }

    public static int getSongCount(String user, String name) {
        int count = -1;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "SELECT COUNT(*) as count FROM Playlist p, PlaylistIsIn pi WHERE p.Username = pi.Username AND p.Username = '%s' AND p.Name = '%s'", user, name);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                count = rs.getInt("count");
            }
//            System.out.println(count);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return count;

    }


    public static boolean makePL(String user, String name) {
        boolean insertDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "INSERT INTO Playlist VALUES('%s', '%s')", user, name);
            insertDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return insertDone;

    }

    public static boolean deletePL(String user, String name) {
        boolean deletionDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "DELETE FROM Playlist p WHERE p.Username = '%s' AND p.Name = '%s'", user, name);
            deletionDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return deletionDone;

    }


    public static void deleteLibSong(Song s, String user) {
        boolean deletionDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "DELETE FROM AddsToLibrary a WHERE a.Username = '%s' AND a.TrackNum = %d AND a.ReleaseID = %d", user, s.getTrackNum(), s.getReleaseID());
            deletionDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        /*return deletionDone;*/

    }

    public static boolean deletePLSong(Song s, String plName, String user) {
        boolean deletionDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "DELETE FROM PlaylistIsIn pi WHERE pi.Username = '%s' AND pi.TrackNum = %d AND pi.ReleaseID = %d AND pi.Name = '%s'", user, s.getTrackNum(), s.getReleaseID(), plName);
            deletionDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return deletionDone;

    }

    public static void addToLibrary(Song song, String username) {
        // stub
        boolean insertDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "INSERT INTO AddsToLibrary a VALUES(%d, %d, '%s', 0, 0)", song.getReleaseID(), song.getTrackNum(), username);
            insertDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


//        return insertDone;

    }

    public static void likeSong(Song song, String username) {
        // stub
        boolean insertDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "UPDATE AddsToLibrary a SET a.Liked = 1 WHERE a.Username = '%s' AND a.TrackNum = %d AND a.ReleaseID = %d", username, song.getTrackNum(), song.getReleaseID());
            insertDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


//        return insertDone;

    }

    public static void unlikeSong(Song song, String username) {
        // stub
        boolean insertDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "UPDATE AddsToLibrary a SET a.Liked = 0 WHERE a.Username = '%s' AND a.TrackNum = %d AND a.ReleaseID = %d", username, song.getTrackNum(), song.getReleaseID());
            insertDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void downloadSong(Song song, String username) {
        // stub
        boolean insertDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "UPDATE AddsToLibrary a SET a.Downloaded = 1 WHERE a.Username = '%s' AND a.TrackNum = %d AND a.ReleaseID = %d", username, song.getTrackNum(), song.getReleaseID());
            insertDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void undownloadSong(Song song, String username) {
        // stub
        boolean insertDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "UPDATE AddsToLibrary a SET a.Downloaded = 0 WHERE a.Username = '%s' AND a.TrackNum = %d AND a.ReleaseID = %d", username, song.getTrackNum(), song.getReleaseID());
            insertDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void addSongToPl(Song song, String username, String plName) {
        boolean insertDone = false;
        try {

            Connection connection = DatabaseManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = String.format(
                    "INSERT INTO PlaylistIsIn pi VALUES('%s', '%s', %d, %d)", username, plName, song.getTrackNum(), song.getReleaseID());
            insertDone = statement.execute(query);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}