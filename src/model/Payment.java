package model;

public class Payment {
    private String date;
    private float amount;

    public Payment(String date, float amount){
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}
