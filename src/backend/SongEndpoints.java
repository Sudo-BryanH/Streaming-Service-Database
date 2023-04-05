package backend;

import database.DatabaseManager;
import model.Release;
import model.Song;
import util.Misc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongEndpoints {
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

    public static List<Song> getSongsByRelease(Release release, String username) {
        int releaseID = release.id;
        String query = "SELECT ReleaseID, TrackNum, Name, Duration, Genre, Plays " +
                "FROM Song WHERE ReleaseID = %d";
        return getSongsHelper(String.format(query, releaseID), username);
    }

    public static void addSong(Song song) {
        String query = "INSERT INTO Song VALUES (%d, %d, '%s', %d, '%s', %d)";
        query(String.format(query, song.releaseID, song.trackNum, song.name, song.duration, song.genre, song.plays));
    }

    public static void editSong(Song song) {
        String query = "UPDATE Releases SET " +
                "Name = '%s', Duration = %d, Genre = '%s', Plays = %d " +
                "WHERE ReleaseID = %d AND TrackNum = %d";
        query(String.format(query, song.name, song.duration, song.genre, song.plays, song.releaseID, song.trackNum));
    }

    public static void removeSong(Song song) {
        String query = "DELETE FROM Song WHERE ReleaseID = %d AND TrackNum = %d";
        query(String.format(query, song.releaseID, song.trackNum));
    }

    // **************** HELPERS ****************

    private static List<Song> getSongsHelper(String query, String username){
        ResultSet results = query(query);
        try {
            List<Song> songs = new ArrayList<>();

            while (results.next()){
                int releaseID = results.getInt("ReleaseID");
                int trackNum = results.getInt("TrackNum");
                String name = results.getString("Name");
                int duration = results.getInt("Duration");
                String genre = results.getString("Genre");
                int plays = results.getInt("Plays");

                Song song = new Song(releaseID, trackNum, name, duration, genre, plays);
                setLibraryStatus(song, username);
                songs.add(song);
            }

            results.close();
            return songs;

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    private static void setLibraryStatus(Song song, String username) {
        String query = "SELECT Downloaded, Liked FROM AddsToLibrary " +
                "WHERE ReleaseID = %d AND TrackNum = %d AND Username = '%s' " +
                "ORDER BY ReleaseID ASC";
        ResultSet results = query(String.format(query, song.releaseID, song.trackNum, username));

        try {
            if (results.next()) {
                song.added = true;
                song.downloaded = Misc.intToBool(results.getInt("Downloaded"));
                song.liked = Misc.intToBool(results.getInt("Liked"));
            } else {
                song.added = false;
                song.downloaded = false;
                song.liked = false;
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }

    }
}
