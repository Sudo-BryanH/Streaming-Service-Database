package model;

public class User {
    private String username;
    private boolean premium;

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

}
