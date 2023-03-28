package ui.panels;

import ui.MainUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserPaymentsPanel extends PaymentPanel{
    public UserPaymentsPanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Payments"), BorderLayout.NORTH);
        headerPanel.add(createBoldedTitle("SubscriptionPeriod : ???"), BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        JPanel paymentHistory = createPaymentHistory();
        JPanel payNowPanel = createPayNowPanel();
        JPanel nestedPanels = new JPanel();
        BoxLayout layout = new BoxLayout(nestedPanels, BoxLayout.Y_AXIS);
        nestedPanels.setLayout(layout);
        nestedPanels.add(paymentHistory);
        nestedPanels.add(payNowPanel);
        add(nestedPanels, BorderLayout.WEST);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }



   private JPanel createPayNowPanel(){
        JPanel payNowPanel = new JPanel();
        payNowPanel.setMaximumSize(new Dimension(450,50));
        payNowPanel.setPreferredSize(new Dimension(450,50));
        payNowPanel.setLayout(new BorderLayout());
        JButton payButton = new JButton("Pay Now");
        payButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        payButton.setMaximumSize(new Dimension(100,20));
        payButton.setPreferredSize(new Dimension(100,20));
        payButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame paymentFrame = new JFrame("Payment Gateway");
            JPanel paymentContent = createPaymentPortalPanel();
            paymentContent.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            paymentFrame.add(paymentContent);
            paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            paymentFrame.setSize(new Dimension(400,400));
            paymentFrame.setVisible(true);
        }
        });
        payNowPanel.add(payButton,BorderLayout.WEST);
        return payNowPanel;
    }

    private JPanel createPaymentHistory() {
        JPanel paymentHistory = new JPanel();
        paymentHistory.setLayout(new BorderLayout());
        paymentHistory.setMaximumSize(new Dimension(450,200));
        paymentHistory.setPreferredSize(new Dimension(450,200));
        // Create table model
        String[] columnNames = {"Date", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
//        for (Payment payment : paymentList) {
//            Object[] rowData = {payment.getDate(), payment.getAmount(), payment.getDescription()};
//            model.addRow(rowData);
//        }
        JTable paymentTable = new JTable(model);
        JTableHeader header = paymentTable.getTableHeader();
        header.setBackground(Color.GRAY);
        header.setForeground(Color.WHITE);
        paymentTable.setFillsViewportHeight(true);
        paymentTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(400,250));
        scrollPane.setPreferredSize(new Dimension(400,250));
        JLabel paymentHistoryLabel = createBoldedTitle("Payment History");
        paymentHistory.add(paymentHistoryLabel, BorderLayout.NORTH);
        paymentHistory.add(scrollPane, BorderLayout.WEST);
        return paymentHistory;
    }

    public static JLabel createBoldedTitle(String value){
        JLabel label = new JLabel(value);
        Font font = label.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        label.setFont(boldFont);
        label.setPreferredSize(new Dimension(5,20));
        return label;
    }
}
