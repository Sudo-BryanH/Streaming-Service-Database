package model;

import util.Misc;

import java.util.List;

public class Release {
    public int id;
    public String name;
    public List<Artist> artists;
    public String type;
    public String releaseDate;
    public String artUrl;
    public String distributor;

    public Release(int id, String name, List<Artist> artists, String type, String releaseDate, String artUrl, String distributor) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.type = type;
        this.releaseDate = releaseDate;
        this.artUrl = artUrl;
        this.distributor = distributor;
    }

    public String getArtistNames() {
       return Misc.artistsToString(artists);
    }
}
