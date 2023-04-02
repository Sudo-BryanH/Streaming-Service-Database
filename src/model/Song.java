package model;

public class Song {
    int trackNum;
    int releaseID;

    String name;

    int duration;


    String genre;

    public Song(){}

    public Song(int track, int release, String name, int duration, String genre) {
        trackNum = track;
        releaseID = release;
        this.name = name;
        this.duration = duration;
        this.genre = genre;
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

