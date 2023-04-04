package model;

public class Playlist {



    private String username;
    private String plName;
    private int size;
    public Playlist(String username, String plName) {
        this.username = username;
        this.plName = plName;
        size = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlName() {
        return plName;
    }

    public void setPlName(String plName) {
        this.plName = plName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
