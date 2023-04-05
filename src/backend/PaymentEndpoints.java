package backend;

import database.DatabaseManager;
import model.BillingAddress;
import model.Card;
import model.Payment;
import model.User;
import ui.panels.AdminPaymentsPanel;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class PaymentEndpoints {

    public static ArrayList<Payment> getPayments(String username, AdminPaymentsPanel.PaymentType type){
        try {
            Connection connection = DatabaseManager.getInstance().getConnection();
            ArrayList<Payment> payments = new ArrayList<>();
            String query;
            if (Objects.requireNonNull(type) == AdminPaymentsPanel.PaymentType.USERS) {
                query = "SELECT \"Date\", AMOUNT FROM UserPayment WHERE Username= ?";
            } else {
                query = "SELECT \"Date\", AMOUNT FROM CompanyPayment WHERE DistributorName= ?";
            }
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = (dateFormat.format(resultSet.getDate("Date")));
                Float amount = resultSet.getFloat("Amount");
                Payment payment = new Payment(date,amount);
                payments.add(payment);
            }
            return payments;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return null;
    }


    public static ArrayList<String> getDistributors(){
        try {
            Connection connection = DatabaseManager.getInstance().getConnection();
            ArrayList<String> distributors = new ArrayList<>();
            Statement statement = connection.createStatement();
            String query = "SELECT Name FROM Distributor";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String distributor = resultSet.getString("Name");
                distributors.add(distributor);
            }
            return distributors;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public static void insertBillingAddress(BillingAddress billingAddress){
        Connection connection;
        PreparedStatement statement;
        try {
            connection = DatabaseManager.getInstance().getConnection();
            String postalQuery = "INSERT INTO PostalCodeCityProvince VALUES (?, ?, ?)";
            statement = connection.prepareStatement(postalQuery);
            statement.setString(1,billingAddress.getCity());
            statement.setString(2,billingAddress.getProvince());
            statement.setString(3,billingAddress.getPostalCode());
            statement.executeQuery();
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        try {
            connection = DatabaseManager.getInstance().getConnection();
            String billingQuery = "INSERT INTO BillingAddress VALUES (?, ?, ?)";
            statement = connection.prepareStatement(billingQuery);
            statement.setInt(1,billingAddress.getStreetNum());
            statement.setString(2,billingAddress.getStreetName());
            statement.setString(3,billingAddress.getPostalCode());
            statement.executeQuery();
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
    }


    public static boolean makePayment(User user, Card card, BillingAddress billingAddress, double amount){
        if(!user.getPremium()){
            try{
                deleteCurrentUser(user.getUsername());
            }
            catch (SQLException e){
                System.out.println("couldnt delete current user");
                return false;
            }
            try{
                makeCurrentUserPremium(user.getUsername());
            }
            catch (SQLException e){
                System.out.println("couldnt make current user premium");
                return false;
            }
        }
        try {

            String formattedDate = getCurrentDate();
            Payment payment = new Payment(formattedDate,amount);
            registerCard(card);
            Connection connection = DatabaseManager.getInstance().getConnection();
            int paymentID = generateUniqueHashcode(user.getUsername());
            String paymentQuery = "INSERT INTO UserPayment VALUES (?, ? , TO_DATE(?, 'yyyy-MM-dd'), ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(paymentQuery);
            statement.setInt(1,paymentID);
            System.out.println(payment.getAmount());
            statement.setDouble(2,Math.round(payment.getAmount() * 100.0) / 100.0);
            statement.setString(3,payment.getDate());
            statement.setLong(4,card.getCardNum());
            statement.setString(5,user.getUsername());
            statement.setInt(6,billingAddress.getStreetNum());
            statement.setString(7,billingAddress.getStreetName());
            statement.setString(8,billingAddress.getPostalCode());
            statement.executeQuery();
            updateSubscriptionDate(user.getUsername());
            user.setPremium(true);
            return true;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return false;
    }

    private static void deleteCurrentUser(String user) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String deleteQuery = "DELETE FROM FreeUser WHERE Username=?";
        PreparedStatement statement = connection.prepareStatement(deleteQuery);
        statement.setString(1,user);
        statement.executeQuery();
    }

    private static void makeCurrentUserPremium(String user) throws SQLException {
        Connection connection = DatabaseManager.getInstance().getConnection();
        String currentDate = getCurrentDate();
        String monthLater = nextMonth(currentDate);
        String makePremiumQuery = "INSERT INTO PremiumUser VALUES (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(makePremiumQuery);
        statement.setString(1,user);
        statement.setString(2,currentDate);
        statement.setString(3,monthLater);
        statement.executeQuery();
    }


    public static boolean makePaymentToDistributor(String distributor, BillingAddress billingAddress, double amount){
        try {
            String formattedDate = getCurrentDate();
            Payment payment = new Payment(formattedDate,amount);
            Connection connection = DatabaseManager.getInstance().getConnection();
            int paymentID = generateUniqueHashcode(distributor);
            String paymentQuery = "INSERT INTO CompanyPayment VALUES (?, ? , TO_DATE(?, 'yyyy-MM-dd'), ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(paymentQuery);
            statement.setInt(1,paymentID);
            statement.setDouble(2,Math.round(payment.getAmount() * 100.0) / 100.0);
            statement.setString(3,payment.getDate());
            statement.setInt(4,billingAddress.getStreetNum());
            statement.setString(5,billingAddress.getStreetName());
            statement.setString(6,billingAddress.getPostalCode());
            statement.setString(7,distributor);
            System.out.println(paymentQuery);
            statement.executeQuery();
            return true;
        }
        catch (SQLException exception){
            System.out.println("EKHANE");
            System.out.println(exception.getMessage());
        }
        return false;
    }

    private static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;
    }

    private static String nextMonth(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate nextMonth = date.plusMonths(1);
        return nextMonth.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static void registerCard(Card card){
        try{
            Connection connection = DatabaseManager.getInstance().getConnection();
            System.out.println(card.getExpiryDate());
            String cardQuery = "INSERT INTO CardTable VALUES (?, ?, TO_DATE(?, 'yyyy-MM-dd'))";
            PreparedStatement statement = connection.prepareStatement(cardQuery);
            statement.setString(1,card.getCompany());
            statement.setLong(2,card.getCardNum());
            statement.setString(3,card.getExpiryDate());
            statement.executeQuery();
        }
        catch (SQLException exception){
            System.out.println("card error: " + exception.getMessage() );
        }
    }

    private static int generateUniqueHashcode( String username) {
        long currentTime = System.currentTimeMillis(); // get the current time in milliseconds
        String hashString = String.format("%s:%d", username, currentTime); // concatenate the value, username, and time
        return hashString.hashCode(); // use the built-in hashcode method to generate the unique hashcode
    }

    public static ArrayList<String> getUsers() {
        try {
            Connection connection = DatabaseManager.getInstance().getConnection();
            ArrayList<String> users = new ArrayList<>();
            Statement statement = connection.createStatement();
            String query = "SELECT Username FROM Users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String user = resultSet.getString("Username");
                users.add(user);
            }
            return users;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public static boolean getPremiumStatus(User user){
        try{
            Connection connection = DatabaseManager.getInstance().getConnection();
            String query = "SELECT * FROM PremiumUser WHERE Username=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,user.getUsername());
            ResultSet resultSet = statement.executeQuery();
            boolean status = resultSet.next();
            return status;
        }
        catch (SQLException exception){
            System.out.println("Error " + exception.getMessage() );
        }
        return false;
    }

    public static String getSubscriptionPeriod(User user){
        try{
            Connection connection = DatabaseManager.getInstance().getConnection();
            String query = "SELECT SubStartDate,SubEndDate FROM PremiumUser WHERE Username=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,user.getUsername());
            ResultSet resultSet = statement.executeQuery();
            String result = "";
            while (resultSet.next()){
                LocalDate start = resultSet.getDate("SubStartDate").toLocalDate();
                LocalDate end = resultSet.getDate("SubEndDate").toLocalDate();
                String startStr = start.format(DateTimeFormatter.ISO_LOCAL_DATE);
                String endStr = end.format(DateTimeFormatter.ISO_LOCAL_DATE);
                result = startStr + " to " + endStr;
            }
            return result.length() == 0 ? "No subscriptions available" : result;
        }
        catch (SQLException exception){
            System.out.println("Error " + exception.getMessage() );
        }
        return "No subscriptions available";
    }

    private static void updateSubscriptionDate(String user){
        try{
            Connection connection = DatabaseManager.getInstance().getConnection();
            String currentDate = getCurrentDate();
            String monthLater = nextMonth(currentDate);
            String makePremiumQuery = "UPDATE PremiumUser SET SubStartDate = ?, SubEndDate = ? WHERE Username=?";
            PreparedStatement statement = connection.prepareStatement(makePremiumQuery);
            statement.setString(1,currentDate);
            statement.setString(2,monthLater);
            statement.setString(3,user);
            statement.executeQuery();
        }
        catch (SQLException e){
            System.out.println("Could not update the subscription");
        }
    }
}
