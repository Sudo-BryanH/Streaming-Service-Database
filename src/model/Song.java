package model;

public class Song {
    public int releaseID;
    public int trackNum;

    public String name;
    public int duration;
    public String genre;
    public int plays;
    public boolean added;

    public boolean downloaded;
    public boolean liked;

    public Song(int trackNum, int releaseID, String name, int duration, String genre) {
        this.trackNum = trackNum;
        this.releaseID = releaseID;
        this.name = name;
        this.duration = duration;
        this.genre = genre;
    }

    public Song(int releaseID, int trackNum, String name, int duration, String genre, int plays) {
        this.releaseID = releaseID;
        this.trackNum = trackNum;
        this.name = name;
        this.duration = duration;
        this.genre = genre;
        this.plays = plays;
    }

    public int getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(int trackNum) {
        this.trackNum = trackNum;
    }

    public int getReleaseID() {
        return releaseID;
    }

    public void setReleaseID(int releaseID) {
        this.releaseID = releaseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }


    }

