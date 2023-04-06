package ui.panels;

import backend.ProjectionEndpoint;
import ui.MainUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProjectionPanel extends ContentPanel{
    private JComboBox<String> tableDropdown;
    private JPanel checkboxesPanel;
    private HashMap<String, List<String>> tableColumns;

    public ProjectionPanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate() {
        initializeDropDownsCheckBoxes();
        tableColumns.put("Creates", new ArrayList<>(Arrays.asList("ReleaseID", "ArtistID")));
        tableDropdown = new JComboBox<>(tableColumns.keySet().toArray(new String[0]));
        tableDropdown.addActionListener(e -> {
            updateCheckBoxes();
        });
        checkboxesPanel = new JPanel();
        checkboxesPanel.setLayout(new BoxLayout(checkboxesPanel, BoxLayout.PAGE_AXIS));
        setLayout(new BorderLayout());
        add(tableDropdown, BorderLayout.NORTH);
        add(checkboxesPanel, BorderLayout.CENTER);
    }

    private void initializeDropDownsCheckBoxes() {
        tableColumns = new HashMap<>();
        tableColumns.put("Distributor", new ArrayList<>(Arrays.asList("Name","Website")));
        tableColumns.put("Users", new ArrayList<>(Arrays.asList("Username", "Email", "Password","CreationDate")));
        tableColumns.put("FreeUser", new ArrayList<>(Arrays.asList("Username", "AdsServed")));
        tableColumns.put("PremiumUser", new ArrayList<>(Arrays.asList("Username", "SubStartDate", "SubEndDate")));
        tableColumns.put("Releases", new ArrayList<>(Arrays.asList("ID", "Name", "Type","ReleaseDate","ArtURL","DistributorName")));
        tableColumns.put("Song", new ArrayList<>(Arrays.asList("ReleaseID", "TrackNum", "Name","Duration","Genre","Plays")));
        tableColumns.put("Artist", new ArrayList<>(Arrays.asList("ID", "Name")));
        tableColumns.put("CardTable", new ArrayList<>(Arrays.asList("CardCompany", "CardNum", "CardExpiry")));
        tableColumns.put("PostalCodeCityProvince", new ArrayList<>(Arrays.asList("City", "Province", "PostalCode")));
        tableColumns.put("BillingAddress", new ArrayList<>(Arrays.asList("StreetNum", "Street", "PostalCode")));
        tableColumns.put("UserPayment", new ArrayList<>(Arrays.asList("ID", "Amount", "\"Date\"", "CardNum", "Username", "StreetNum", "Street", "PostalCode")));
        tableColumns.put("CompanyPayment", new ArrayList<>(Arrays.asList("ID", "Amount", "\"Date\"", "StreetNum", "Street", "PostalCode", "DistributorName")));
        tableColumns.put("Playlist", new ArrayList<>(Arrays.asList("Username", "Name")));
        tableColumns.put("PlaylistIsIn", new ArrayList<>(Arrays.asList("Username", "Name", "ReleaseID", "TrackNum")));
        tableColumns.put("AddsToLibrary", new ArrayList<>(Arrays.asList("ReleaseID", "TrackNum", "Username", "Downloaded", "Liked")));
        tableColumns.put("FeaturedIn", new ArrayList<>(Arrays.asList("ArtistID", "ReleaseID", "TrackNum")));
    }

    private void updateCheckBoxes(){
        checkboxesPanel.removeAll();
        String selectedTable = (String) tableDropdown.getSelectedItem();
        for (String column : tableColumns.get(selectedTable)) {
            JCheckBox checkbox = new JCheckBox(column);
            checkboxesPanel.add(checkbox);
        }
        JButton projectionButton = new JButton("Project!");
        projectionButton.addActionListener(e -> {
            ArrayList<String> columns = new ArrayList<>();
            for (Component comp : checkboxesPanel.getComponents()) {
                if (comp instanceof JCheckBox) {
                    JCheckBox checkbox = (JCheckBox) comp;
                    if (checkbox.isSelected()) {
                        columns.add(checkbox.getText());
                    }
                }
            }
            ArrayList<ArrayList<String>> result = ProjectionEndpoint.getProjection(selectedTable,columns);
            generateViewFrame(selectedTable,result,columns);

        });
        checkboxesPanel.add(projectionButton);
        checkboxesPanel.repaint();
        checkboxesPanel.revalidate();
    }

    private void generateViewFrame(String tableHeader, ArrayList<ArrayList<String>> columnsResult, ArrayList<String> columnHeader){
        JFrame viewFrame = new JFrame();
        JTable table = new JTable();
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.GRAY);
        header.setForeground(Color.WHITE);
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = UserPaymentsPanel.createBoldedTitle(tableHeader);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        Object[] columns = columnHeader.toArray();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        if(columnsResult != null){
            for(ArrayList<String> row : columnsResult){
                model.addRow(row.toArray());
            }
        }
        table.setModel(model);
        JScrollPane pane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(800, 600));
        panel.add(title,BorderLayout.NORTH);
        panel.add(pane,BorderLayout.WEST);
        viewFrame.add(panel);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setSize(1000,1000);
        viewFrame.setResizable(false);
        viewFrame.setVisible(true);
    }
}
