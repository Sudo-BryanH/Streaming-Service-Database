package model;

import java.util.Objects;

public class Distributor {
    public String name;
    public String website;

    public Distributor(String name, String website) {
        this.name = name;
        this.website = website;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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
        return this.name.equals(other.name);
    }
}
