package model;

public class Artist {
    public int id;
    public String name;
    public int monthlyListeners;

    public Artist(int id, String name, int monthlyListeners) {
        this.id = id;
        this.name = name;
        this.monthlyListeners = monthlyListeners;
    }
}
