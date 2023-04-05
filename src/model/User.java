package model;

import java.sql.Date;

public class User {
    private String username;
    private boolean premium = false;

    private java.sql.Date CreationDate = null;
    private String email = null;



    private java.sql.Date SubStart = null;

    private java.sql.Date SubEnd = null;


    private int adsServed = 0;

    public User(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setPremium(boolean status){
        this.premium = status;
    }
    public boolean getPremium(){
        return this.premium;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(Date creationDate) {
        CreationDate = creationDate;
    }

    public int getAdsServed() {
        return adsServed;
    }

    public void setAdsServed(int adsServed) {
        this.adsServed = adsServed;
    }

    public Date getSubStart() {
        return SubStart;
    }

    public void setSubStart(Date subStart) {
        SubStart = subStart;
    }

    public Date getSubEnd() {
        return SubEnd;
    }

    public void setSubEnd(Date subEnd) {
        SubEnd = subEnd;
    }

}
