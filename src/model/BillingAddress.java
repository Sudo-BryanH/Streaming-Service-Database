package model;

public class BillingAddress {
    private int streetNum;
    private String streetName;
    private String city;
    private String province;
    private String postalCode;

    public BillingAddress(int streetNum, String streetName, String city, String province, String postalCode) {
        this.streetNum = streetNum;
        this.streetName = streetName;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    public int getStreetNum() {
        return streetNum;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
