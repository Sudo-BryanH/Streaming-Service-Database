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

    public static List<Artist> getArtists() {
        String query = "SELECT ID, Name " +
                "FROM Artist " +
                "ORDER BY ID ASC";
        return getArtistsHelper(query);
    }

    public static Artist getArtistByID(int id) {
        String query = "SELECT ID, Name FROM Artist WHERE ID = %d";
        List<Artist> result = getArtistsHelper(String.format(query, id));

        if (result != null && result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public static List<Artist> getArtistsByRelease(int releaseID) {
        String query = "SELECT a.ID, a.Name " +
                "FROM Artist a, Creates c, Releases r " +
                "WHERE a.ID = c.ArtistID AND r.ID = c.ReleaseID AND c.ReleaseID = %d";
        return getArtistsHelper(String.format(query, releaseID));
    }

    public static List<Artist> getArtistsBySong(Song song) {
        String query = "SELECT DISTINCT a.ID, a.Name " +
                "FROM Artist a, Song s, Creates c, FeaturedIn f " +
                "WHERE " +
                "((a.ID = c.ArtistID AND s.ReleaseID = c.ReleaseID) OR " +
                "(a.ID = f.ArtistID AND s.ReleaseID = f.ReleaseID AND s.TrackNum = f.TrackNum)) " +
                "AND s.ReleaseID = %d AND s.TrackNum = %d";
        return getArtistsHelper(String.format(query, song.releaseID, song.trackNum));
    }

    public static List<Artist> searchArtists(String keywords) {
        if (keywords.equals("")) {
            return getArtists();
        }

        String query = "SELECT DISTINCT ID, Name " +
                "FROM Artist WHERE " +
                "LOWER(Name) LIKE LOWER('%%%s%%') " +
                "ORDER BY ID ASC";
        return getArtistsHelper(String.format(query, keywords));
    }

    public static void addArtist(Artist artist) {
        String query = "INSERT INTO Artist VALUES (%d, '%s', NULL)";
        query(String.format(query, artist.id, artist.name));
    }

    public static void removeArtist(Artist artist) {
        String query = "DELETE FROM Artist WHERE ID = %d";
        query(String.format(query, artist.id));
    }

    // **************** HELPERS ****************

    private static List<Artist> getArtistsHelper(String query){
        ResultSet results = query(query);
        try {
            List<Artist> artists = new ArrayList<>();

            while (results.next()){
                int id = results.getInt("ID");
                String name = results.getString("Name");

                Artist artist = new Artist(id, name);
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
