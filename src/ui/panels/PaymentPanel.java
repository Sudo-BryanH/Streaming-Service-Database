package ui.panels;

import ui.MainUI;

import javax.swing.*;
import java.awt.*;

import static ui.panels.UserPaymentsPanel.createBoldedTitle;

public abstract class  PaymentPanel extends ContentPanel {
    protected JButton paymentSubmitButton;
    protected JTextField cardNumberTextField;
    protected JTextField expDateTextField;
    protected JTextField streetNumberTextField;
    protected JTextField streetTextField;
    protected JTextField cityTextField;
    protected JTextField provinceTextField;
    protected JTextField postalCodeTextField;
    protected JTextField amountField;


    public PaymentPanel(MainUI mainUI) {
        super(mainUI);
    }

    protected JPanel createPaymentPortalPanel() {
        JPanel paymentPortalPanel = new JPanel(new GridLayout(11,2,8,10));
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(20);
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberTextField = new JTextField(20);
        JLabel expDateLabel = new JLabel("Expiration Date: (yyyy-MM-dd)");
        expDateTextField = new JTextField(20);
        JLabel cvvLabel = new JLabel("CVV:");
        JTextField cvvTextField = new JTextField(3);
        paymentSubmitButton = new JButton("Submit");
        paymentPortalPanel.add(amountLabel);
        paymentPortalPanel.add(amountField);
        paymentPortalPanel.add(cardNumberLabel);
        paymentPortalPanel.add(cardNumberTextField);
        paymentPortalPanel.add(expDateLabel);
        paymentPortalPanel.add(expDateTextField);
        paymentPortalPanel.add(cvvLabel);
        paymentPortalPanel.add(cvvTextField);
        JLabel BillingAddressLabel = createBoldedTitle("Billing Address");
        paymentPortalPanel.add(BillingAddressLabel);
        paymentPortalPanel.add(new JLabel());
        JLabel streetNumberLabel = new JLabel("Street#:");
        streetNumberTextField = new JTextField(20);
        JLabel streetLabel = new JLabel("Street:");
        streetTextField = new JTextField(20);
        JLabel cityLabel = new JLabel("City:");
        cityTextField = new JTextField(20);
        JLabel provinceLabel = new JLabel("Province:");
        provinceTextField = new JTextField(2);
        JLabel postalCodeLabel = new JLabel("Postal Code:");
        postalCodeTextField = new JTextField(6);
        paymentPortalPanel.add(streetNumberLabel);
        paymentPortalPanel.add(streetNumberTextField);
        paymentPortalPanel.add(streetLabel);
        paymentPortalPanel.add(streetTextField);
        paymentPortalPanel.add(cityLabel);
        paymentPortalPanel.add(cityTextField);
        paymentPortalPanel.add(provinceLabel);
        paymentPortalPanel.add(provinceTextField);
        paymentPortalPanel.add(postalCodeLabel);
        paymentPortalPanel.add(postalCodeTextField);
        paymentPortalPanel.add(new JLabel());
        paymentPortalPanel.add(paymentSubmitButton);
        return paymentPortalPanel;
    }

    protected JPanel adminPaymentPortalPanel(){
        JPanel paymentPortalPanel = new JPanel(new GridLayout(8,2,8,10));
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(20);
        paymentSubmitButton = new JButton("Submit");
        paymentPortalPanel.add(amountLabel);
        paymentPortalPanel.add(amountField);
        JLabel BillingAddressLabel = createBoldedTitle("Billing Address");
        paymentPortalPanel.add(BillingAddressLabel);
        paymentPortalPanel.add(new JLabel());
        JLabel streetNumberLabel = new JLabel("Street#:");
        streetNumberTextField = new JTextField(20);
        JLabel streetLabel = new JLabel("Street:");
        streetTextField = new JTextField(20);
        JLabel cityLabel = new JLabel("City:");
        cityTextField = new JTextField(20);
        JLabel provinceLabel = new JLabel("Province:");
        provinceTextField = new JTextField(2);
        JLabel postalCodeLabel = new JLabel("Postal Code:");
        postalCodeTextField = new JTextField(6);
        paymentPortalPanel.add(streetNumberLabel);
        paymentPortalPanel.add(streetNumberTextField);
        paymentPortalPanel.add(streetLabel);
        paymentPortalPanel.add(streetTextField);
        paymentPortalPanel.add(cityLabel);
        paymentPortalPanel.add(cityTextField);
        paymentPortalPanel.add(provinceLabel);
        paymentPortalPanel.add(provinceTextField);
        paymentPortalPanel.add(postalCodeLabel);
        paymentPortalPanel.add(postalCodeTextField);
        paymentPortalPanel.add(new JLabel());
        paymentPortalPanel.add(paymentSubmitButton);
        return paymentPortalPanel;
    }
}
