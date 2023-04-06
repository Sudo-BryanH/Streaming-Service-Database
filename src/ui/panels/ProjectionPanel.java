package ui.panels;

import ui.MainUI;

import javax.swing.*;
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
        tableColumns.put("Users", new ArrayList<>(Arrays.asList("Username", "Email", "Password")));
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
            System.out.println(selectedTable);
            for (Component comp : checkboxesPanel.getComponents()) {
                if (comp instanceof JCheckBox) {
                    JCheckBox checkbox = (JCheckBox) comp;
                    if (checkbox.isSelected()) {
                        System.out.println(checkbox.getText());
                    }
                }
            }
        });
        checkboxesPanel.add(projectionButton);
        checkboxesPanel.repaint();
        checkboxesPanel.revalidate();
    }
}
