package model;

import util.Misc;

import java.util.List;
import java.util.Objects;

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

    public Release(int id, String name, String type, String releaseDate, String artUrl, String distributor) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.releaseDate = releaseDate;
        this.artUrl = artUrl;
        this.distributor = distributor;
    }

    public String getArtistNames() {
       return Misc.artistsToString(artists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Release)) {
            return false;
        }
        Release other = (Release) obj;
        return this.id == other.id;
    }
}
