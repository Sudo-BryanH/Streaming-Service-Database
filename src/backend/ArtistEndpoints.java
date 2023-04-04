package backend;

import database.DatabaseManager;
import model.Artist;
import model.Song;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArtistEndpoints {
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

    public static List<Artist> getArtistsByRelease(int releaseID) {
        String query = "SELECT a.ID, a.Name, a.MonthlyListeners " +
                "FROM Artist a, Creates c, Releases r " +
                "WHERE a.ID = c.ArtistID AND r.ID = c.ReleaseID AND c.ReleaseID = %d";
        return getArtistsHelper(String.format(query, releaseID));
    }

    public static List<Artist> getArtistsBySong(Song song) {
        String query = "SELECT DISTINCT a.ID, a.Name, a.MonthlyListeners " +
                "FROM Artist a, Song s, Creates c, FeaturedIn f " +
                "WHERE " +
                "((a.ID = c.ArtistID AND s.ReleaseID = c.ReleaseID) OR " +
                "(a.ID = f.ArtistID AND s.ReleaseID = f.ReleaseID AND s.TrackNum = f.TrackNum)) " +
                "AND s.ReleaseID = %d AND s.TrackNum = %d";
        return getArtistsHelper(String.format(query, song.releaseID, song.trackNum));
    }

    private static List<Artist> getArtistsHelper(String query){
        ResultSet results = query(query);
        try {
            List<Artist> artists = new ArrayList<>();

            while (results.next()){
                int id = results.getInt("ID");
                String name = results.getString("Name");
                int monthlyListeners = results.getInt("MonthlyListeners");

                Artist artist = new Artist(id, name, monthlyListeners);
                artists.add(artist);
            }

            results.close();
            return artists;

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }
}
