package ui.panels;

import ui.MainUI;

import javax.swing.*;
import java.awt.*;

import static ui.panels.UserPaymentsPanel.createBoldedTitle;

public abstract class  PaymentPanel extends ContentPanel {
    public PaymentPanel(MainUI mainUI) {
        super(mainUI);
    }

    protected JPanel createPaymentPortalPanel() {
        JPanel paymentPortalPanel = new JPanel(new GridLayout(10,2,8,10));
        JLabel cardNumberLabel = new JLabel("Card Number:");
        JTextField cardNumberTextField = new JTextField(20);
        JLabel expDateLabel = new JLabel("Expiration Date:");
        JTextField expDateTextField = new JTextField(20);
        JLabel cvvLabel = new JLabel("CVV:");
        JTextField cvvTextField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
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
        JTextField streetNumberTextField = new JTextField(20);
        JLabel streetLabel = new JLabel("Street:");
        JTextField streetTextField = new JTextField(20);
        JLabel cityLabel = new JLabel("City:");
        JTextField cityTextField = new JTextField(20);
        JLabel provinceLabel = new JLabel("Province:");
        JTextField provinceTextField = new JTextField(20);
        JLabel postalCodeLabel = new JLabel("Postal Code:");
        JTextField postalCodeTextField = new JTextField(20);
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
        paymentPortalPanel.add(submitButton);
        return paymentPortalPanel;
    }
}
