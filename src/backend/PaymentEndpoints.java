package backend;

import database.DatabaseManager;
import model.BillingAddress;
import model.Card;
import model.Payment;
import model.User;
import ui.panels.AdminPaymentsPanel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class PaymentEndpoints {

    public static ArrayList<Payment> getPayments(String username, AdminPaymentsPanel.PaymentType type){
        try {
            Connection connection = DatabaseManager.establishConnection();
            ArrayList<Payment> payments = new ArrayList<>();
            Statement statement = connection.createStatement();
            String query;
            if (Objects.requireNonNull(type) == AdminPaymentsPanel.PaymentType.USERS) {
                query = "SELECT \"Date\", AMOUNT FROM UserPayment WHERE Username='" + username + "'";
            } else {
                query = "SELECT \"Date\", AMOUNT FROM CompanyPayment WHERE DistributorName='" + username + "'";
            }
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = (dateFormat.format(resultSet.getDate("Date")));
                Float amount = resultSet.getFloat("Amount");
                Payment payment = new Payment(date,amount);
                payments.add(payment);
            }
            DatabaseManager.close();
            return payments;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
        return null;
    }


    public static ArrayList<String> getDistributors(){
        try {
            Connection connection = DatabaseManager.establishConnection();
            ArrayList<String> distributors = new ArrayList<>();
            Statement statement = connection.createStatement();
            String query = "SELECT Name FROM Distributor";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String distributor = resultSet.getString("Name");
                distributors.add(distributor);
            }
            DatabaseManager.close();
            return distributors;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
        return null;
    }

    public static void insertBillingAddress(BillingAddress billingAddress){
        Connection connection;
        Statement statement;
        try {
            connection = DatabaseManager.establishConnection();
            statement = connection.createStatement();
            String postalQuery = String.format("INSERT INTO PostalCodeCityProvince VALUES ('%s', '%s', '%s')",
                    billingAddress.getCity(),billingAddress.getProvince(),billingAddress.getPostalCode());
            statement.executeQuery(postalQuery);
            DatabaseManager.close();
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        try {
            connection = DatabaseManager.establishConnection();
            statement = connection.createStatement();
            String billingQuery = String.format("INSERT INTO BillingAddress VALUES (%d, '%s', '%s')",
                    billingAddress.getStreetNum(),billingAddress.getStreetName(),billingAddress.getPostalCode());
            statement.executeQuery(billingQuery);
            DatabaseManager.close();
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
    }


    public static boolean makePayment(User user, Card card, BillingAddress billingAddress, float amount){
        if(!user.getPremium()){
            try{
                deleteCurrentUser(user.getUsername());
            }
            catch (SQLException e){
                DatabaseManager.close();
                System.out.println("couldnt delete current user");
                return false;
            }
            try{
                makeCurrentUserPremium(user.getUsername());
            }
            catch (SQLException e){
                DatabaseManager.close();
                System.out.println("couldnt make current user premium");
                return false;
            }
        }
        try {

            String formattedDate = getCurrentDate();
            Payment payment = new Payment(formattedDate,amount);
            registerCard(card);
            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            int paymentID = generateUniqueHashcode(user.getUsername());
            String paymentQuery = String.format(
                    "INSERT INTO UserPayment VALUES (%d, %.2f , TO_DATE('%s', 'yyyy-MM-dd'), %d, '%s', %d, '%s', '%s')",
                    paymentID,payment.getAmount(),payment.getDate(),card.getCardNum(),user.getUsername(),
                    billingAddress.getStreetNum(),billingAddress.getStreetName(),billingAddress.getPostalCode());
            statement.executeQuery(paymentQuery);
            DatabaseManager.close();
            updateSubscriptionDate(user.getUsername());
            user.setPremium(true);
            return true;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
        return false;
    }

    private static void deleteCurrentUser(String user) throws SQLException {
        Connection connection = DatabaseManager.establishConnection();
        Statement statement = connection.createStatement();
        String deleteQuery = String.format("DELETE FROM FreeUser WHERE Username='%s'", user);
        statement.executeQuery(deleteQuery);
        DatabaseManager.close();
    }

    private static void makeCurrentUserPremium(String user) throws SQLException {
        Connection connection = DatabaseManager.establishConnection();
        Statement statement = connection.createStatement();
        String currentDate = getCurrentDate();
        String monthLater = nextMonth(currentDate);
        String makePremiumQuery = String.format("INSERT INTO PremiumUser VALUES ('%s','%s','%s')", user,currentDate,monthLater);
        statement.executeQuery(makePremiumQuery);
        DatabaseManager.close();
    }


    public static boolean makePaymentToDistributor(String distributor, BillingAddress billingAddress, float amount){
        try {
            String formattedDate = getCurrentDate();
            Payment payment = new Payment(formattedDate,amount);
            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            int paymentID = generateUniqueHashcode(distributor);
            String paymentQuery = String.format(
                    "INSERT INTO CompanyPayment VALUES (%d, %.2f , TO_DATE('%s', 'yyyy-MM-dd'), %d, '%s', '%s', '%s')",
                    paymentID,payment.getAmount(),payment.getDate(), billingAddress.getStreetNum(),
                    billingAddress.getStreetName(),billingAddress.getPostalCode(),distributor);
            System.out.println(paymentQuery);
            statement.executeQuery(paymentQuery);
            DatabaseManager.close();
            return true;
        }
        catch (SQLException exception){
            System.out.println("EKHANE");
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
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
            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            System.out.println(card.getExpiryDate());
            String cardQuery = String.format("INSERT INTO CardTable VALUES ('%s', %d, TO_DATE('%s', 'yyyy-MM-dd'))",
                    card.getCompany(),card.getCardNum(),card.getExpiryDate());
            statement.executeQuery(cardQuery);
        }
        catch (SQLException exception){
            System.out.println("card error: " + exception.getMessage() );
        }
        DatabaseManager.close();
    }

    private static int generateUniqueHashcode( String username) {
        long currentTime = System.currentTimeMillis(); // get the current time in milliseconds
        String hashString = String.format("%s:%d", username, currentTime); // concatenate the value, username, and time
        return hashString.hashCode(); // use the built-in hashcode method to generate the unique hashcode
    }

    public static ArrayList<String> getUsers() {
        try {
            Connection connection = DatabaseManager.establishConnection();
            ArrayList<String> users = new ArrayList<>();
            Statement statement = connection.createStatement();
            String query = "SELECT Username FROM Users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String user = resultSet.getString("Username");
                users.add(user);
            }
            DatabaseManager.close();
            return users;
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
        }
        DatabaseManager.close();
        return null;
    }

    public static boolean getPremiumStatus(User user){
        try{
            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM PremiumUser WHERE Username='%s'",user.getUsername());
            ResultSet resultSet = statement.executeQuery(query);
            DatabaseManager.close();
            return resultSet.next();
        }
        catch (SQLException exception){
            System.out.println("Error " + exception.getMessage() );
        }
        DatabaseManager.close();
        return false;
    }

    public static String getSubscriptionPeriod(User user){
        try{
            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String query = String.format("SELECT SubStartDate,SubEndDate FROM PremiumUser WHERE Username='%s'",user.getUsername());
            ResultSet resultSet = statement.executeQuery(query);
            String result = "";
            while (resultSet.next()){
                LocalDate start = resultSet.getDate("SubStartDate").toLocalDate();
                LocalDate end = resultSet.getDate("SubEndDate").toLocalDate();
                String startStr = start.format(DateTimeFormatter.ISO_LOCAL_DATE);
                String endStr = end.format(DateTimeFormatter.ISO_LOCAL_DATE);
                result = startStr + " to " + endStr;
            }
            DatabaseManager.close();
            return result.length() == 0 ? "No subscriptions available" : result;
        }
        catch (SQLException exception){
            System.out.println("Error " + exception.getMessage() );
        }
        DatabaseManager.close();
        return "No subscriptions available";
    }

    private static void updateSubscriptionDate(String user){
        try{
            Connection connection = DatabaseManager.establishConnection();
            Statement statement = connection.createStatement();
            String currentDate = getCurrentDate();
            String monthLater = nextMonth(currentDate);
            String makePremiumQuery = String.format("UPDATE PremiumUser SET SubStartDate = '%s', SubEndDate = '%s' WHERE Username='%s'", currentDate,monthLater,user);
            statement.executeQuery(makePremiumQuery);
            DatabaseManager.close();
        }
        catch (SQLException e){
            System.out.println("Could not update the subscription");
        }
    }
}
