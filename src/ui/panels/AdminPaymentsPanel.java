package ui.panels;

import backend.PaymentEndpoints;
import model.BillingAddress;
import model.Payment;
import ui.MainUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

import static ui.panels.UserPaymentsPanel.createBoldedTitle;

public class AdminPaymentsPanel extends PaymentPanel{
    private JPanel distributorsPanelTable;
    private ArrayList<String> distributors;
    private ArrayList<String> users;
    private JPanel usersPanelTable;

    public AdminPaymentsPanel(MainUI mainUI) {
        super(mainUI);

    }

    @Override
    protected void generate() {
        distributors = PaymentEndpoints.getDistributors();
        users = PaymentEndpoints.getUsers();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        headerPanel.add(createTitle("Payments"), BorderLayout.NORTH);
        add(headerPanel, BorderLayout.NORTH);
        distributorsPanelTable = createScrollPane(PaymentType.DISTRIBUTORS);
        distributorsPanelTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        usersPanelTable = createScrollPane(PaymentType.USERS);
        add(distributorsPanelTable, BorderLayout.CENTER);
        add(usersPanelTable, BorderLayout.SOUTH);
    }

    private JPanel generateDistributorsPanel(){
        JPanel distributorList = new JPanel();
        distributorList.setLayout(new BoxLayout(distributorList, BoxLayout.Y_AXIS));
        for (String r : distributors) {
            distributorList.add(getDistributorPanel(r));
        }
        return distributorList;
    }

    private JPanel generateUsersPanel(){
        JPanel userList = new JPanel();
        userList.setLayout(new BoxLayout(userList, BoxLayout.Y_AXIS));
        for (String r : users) {
            userList.add(getUserPanel(r));
        }
        return userList;
    }

    private JPanel getUserPanel(String user) {
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel nameLabel = new JLabel(user);
        nameLabel.setPreferredSize(new Dimension(325,20));
        userPanel.add(nameLabel);

        JButton viewButton = new JButton("View");
        viewButton.addActionListener(e -> {
            generateViewFrame(user, PaymentType.USERS);
        });
        userPanel.add(viewButton);

        return userPanel;
    }

    private JPanel getDistributorPanel(String distributor) {
        JPanel distributorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel nameLabel = new JLabel(distributor);
        nameLabel.setPreferredSize(new Dimension(240,20));
        distributorPanel.add(nameLabel);

        JButton payButton = new JButton("Pay");
        payButton.addActionListener(e -> {
            JFrame paymentFrame = new JFrame("Pay to: " + distributor);
            paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel paymentContent = adminPaymentPortalPanel();
            paymentContent.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            paymentFrame.add(paymentContent);
            paymentFrame.setSize(new Dimension(400,400));
            paymentSubmitButton.addActionListener(event -> {
                try{
                    BillingAddress billingAddress = new BillingAddress(Integer.parseInt(streetNumberTextField.getText()),
                            streetTextField.getText(),cityTextField.getText(),provinceTextField.getText(),postalCodeTextField.getText());
                    PaymentEndpoints.insertBillingAddress(billingAddress);
                    if(PaymentEndpoints.makePaymentToDistributor(distributor,billingAddress,Double.parseDouble(this.amountField.getText()))){
                        paymentFrame.dispose();
                        JOptionPane.showMessageDialog(this, "Payment Successful to " + distributor);
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "Payment Failed");
                    }
                }
                catch (Exception exception){
                    System.out.println(exception.getMessage());
                    JOptionPane.showMessageDialog(this, "Payment Failed");
                }
            });
            paymentFrame.setVisible(true);
        });
        distributorPanel.add(payButton);
        JButton viewButton = new JButton("View");
        viewButton.addActionListener(e -> {
            generateViewFrame(distributor, PaymentType.DISTRIBUTORS);
        });
        distributorPanel.add(viewButton);

        return distributorPanel;
    }

    private JPanel createScrollPane(PaymentType type){
        JPanel panelTable = new JPanel(new BorderLayout());
        JScrollPane scrollPane;
        JLabel label;
        switch (type){
            case DISTRIBUTORS:
                label = UserPaymentsPanel.createBoldedTitle("Distributors");
                label.setPreferredSize(new Dimension(325,20));
                JPanel distributorsPanel = generateDistributorsPanel();
                scrollPane = new JScrollPane(distributorsPanel);
                break;
            default:
                label = createBoldedTitle("Users");
                label.setPreferredSize(new Dimension(325,20));
                JPanel usersPanel = generateUsersPanel();
                scrollPane = new JScrollPane(usersPanel);
        }
        scrollPane.setMaximumSize(new Dimension(450,200));
        scrollPane.setPreferredSize(new Dimension(450,200));
        panelTable.add(label, BorderLayout.NORTH);
        panelTable.add(scrollPane, BorderLayout.WEST);
        return panelTable;
    }

    private void generateViewFrame(String user, PaymentType type){
        JFrame viewFrame = new JFrame();
        JTable table = new JTable();
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.GRAY);
        header.setForeground(Color.WHITE);
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = UserPaymentsPanel.createBoldedTitle(user + "'s Payment History");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        Object[] columns = {"Date", "Amount"};
        DefaultTableModel model = new DefaultTableModel();
        ArrayList<Payment> payments = PaymentEndpoints.getPayments(user,type);
        model.setColumnIdentifiers(columns);
        if(payments != null){
            for(Payment p : payments){
                model.addRow(new Object[]{p.getDate(),p.getAmount()});
            }
        }
        table.setModel(model);
        JScrollPane pane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(400, 300));
        panel.add(title,BorderLayout.NORTH);
        panel.add(pane,BorderLayout.WEST);
        viewFrame.add(panel);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setSize(400,400);
        viewFrame.setResizable(false);
        viewFrame.setVisible(true);
    }

    public enum PaymentType {USERS, DISTRIBUTORS};
}
