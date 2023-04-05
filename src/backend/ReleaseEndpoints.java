package backend;

import database.DatabaseManager;
import model.Artist;
import model.Release;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReleaseEndpoints {
    private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

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

    public static List<Release> getReleases() {
        String query = "SELECT ID, Name, Type, ReleaseDate, ArtURL, DistributorName FROM Releases ORDER BY ID ASC";
        return getReleasesHelper(query);
    }
    public static String getGoatedSongs(){
        String query = "SELECT Name FROM Song S WHERE NOT EXISTS ((SELECT Username FROM Users U) MINUS (SELECT A.Username FROM AddsToLibrary A WHERE A.Liked=1 AND A.ReleaseID = S.ReleaseID AND A.TrackNum = S.TrackNum))";
        ResultSet results = query(query);
        ArrayList<String> songs = new ArrayList<>();
        try {
            while (results.next()){
                String name = results.getString("Name");
                songs.add(name);
            }

            results.close();
            if (songs.isEmpty()) {
                return "No Goated Songs Yet. :(";
            } else {
                return String.join(", ", songs);
            }

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return "No Goated Songs Yet. :(";
        }
    }


    public static Release getReleaseByID(int id) {
        String query = "SELECT ID, Name, Type, ReleaseDate, ArtURL, DistributorName FROM Releases WHERE ID = %d";
        List<Release> result = getReleasesHelper(String.format(query, id));

        if (result != null && result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public static List<Release> getReleasesByUser(String username) {
        String query = "SELECT DISTINCT " +
                "r.ID as ID, Name, Type, ReleaseDate, ArtURL, DistributorName " +
                "FROM Releases r, AddsToLibrary a " +
                "WHERE r.ID = a.ReleaseID AND a.Username = '%s'";
        return getReleasesHelper(String.format(query, username));
    }

    public static List<Release> searchReleases(String keywords) {
        String creatingArtist = "SELECT * " +
                "FROM Artist a1, Creates c " +
                "WHERE r.ID = c.ReleaseID AND a1.ID = c.ArtistID AND LOWER(a1.Name) LIKE LOWER('%%%1$s%%')";
        String featuredArtist = "SELECT * " +
                "FROM Artist a2, FeaturedIn f " +
                "WHERE s.ReleaseID = f.ReleaseID AND s.TrackNum = f.TrackNum AND " +
                "a2.ID = f.ArtistID AND LOWER(a2.Name) LIKE LOWER('%%%1$s%%')";
        String existsSong = "SELECT * FROM Song s " +
                "WHERE r.ID = s.ReleaseID AND " +
                "(LOWER(s.Name) LIKE LOWER('%%%1$s%%') OR EXISTS (" + featuredArtist + "))";

        String query = "SELECT DISTINCT " +
                "r.ID as ID, r.Name as Name, Type, ReleaseDate, ArtURL, DistributorName " +
                "FROM Releases r WHERE " +
                "EXISTS (" + existsSong + ") OR " +
                "EXISTS (" + creatingArtist + ") OR " +
                "LOWER(r.Name) LIKE LOWER('%%%1$s%%') " +
                "ORDER BY r.ID ASC";
        return getReleasesHelper(String.format(query, keywords));
    }

    public static void addRelease(Release release) {
        String query = "INSERT INTO Releases VALUES (%d, '%s', '%s', '%s', '%s', '%s')";
        query(String.format(query, release.id, release.name, release.type, release.releaseDate, release.artUrl, release.distributor));
    }

    public static void editRelease(Release release) {
        String query = "UPDATE Releases SET " +
                "Name = '%s', Type = '%s', ReleaseDate = '%s', ArtURL = '%s', DistributorName = '%s' " +
                "WHERE ID = %d";
        query(String.format(query, release.name, release.type, release.releaseDate, release.artUrl, release.distributor, release.id));
    }

    public static void removeRelease(Release release) {
        String query = "DELETE FROM Releases WHERE id = %d";
        query(String.format(query, release.id));
    }

    public static void addCreates(Release release, Artist artist) {
        String query = "INSERT INTO Creates Values (%d, %d)";
        query(String.format(query, release.id, artist.id));
    }

    public static void removeCreates(Release release, Artist artist) {
        String query = "DELETE FROM Creates WHERE ReleaseID = %d AND ArtistID = %d";
        query(String.format(query, release.id, artist.id));
    }

    // **************** HELPERS ****************

    private static List<Release> getReleasesHelper(String query){
        ResultSet results = query(query);
        try {
            List<Release> releases = new ArrayList<>();

            while (results.next()){
                int id = results.getInt("ID");
                String name = results.getString("Name");
                String type = results.getString("Type");
                List<Artist> artists = ArtistEndpoints.getArtistsByRelease(id);
                String releaseDate = (dateFormat.format(results.getDate("ReleaseDate")));
                String artUrl = results.getString("Name");
                String distributor = results.getString("DistributorName");

                Release release = new Release(id, name, artists, type, releaseDate, artUrl, distributor);
                releases.add(release);
            }

            results.close();
            return releases;

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }
}
