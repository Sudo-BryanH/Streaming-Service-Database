package model;

public class Payment {
    private String date;
    private double amount;

    public Payment(String date, double amount){
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
