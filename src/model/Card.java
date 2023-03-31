package model;

public class Card {
    private String expiryDate;
    private long cardNum;
    private String company;

    public Card(String expiryDate, long cardNum, String company) {
        this.expiryDate = expiryDate;
        this.cardNum = cardNum;
        this.company = company;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public long getCardNum() {
        return cardNum;
    }

    public String getCompany() {
        return company;
    }
}
