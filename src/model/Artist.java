package model;

import java.util.Objects;

public class Artist {
    public int id;
    public String name;

    public Artist(int id, String name) {
        this.id = id;
        this.name = name;
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
