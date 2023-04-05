package ui.panels;

import backend.DistributorEndpoints;
import model.Distributor;
import ui.MainUI;
import util.Misc;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DistributorsAdminPanel extends ContentPanel{
    private String queryString;
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;
    public DistributorsAdminPanel(MainUI mainUI) {
        super(mainUI);
    }


    private interface FunctionCallback {
        void apply(Distributor distributor);
    }

    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Manage Distributors"), BorderLayout.NORTH);
        headerPanel.add(getSearchBarPanel(), BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        createResultsScrollPane();
        add(resultsScrollPane, BorderLayout.WEST);

        performSearch("");
    }

    private void performSearch(String query) {
        queryString = query;
        List<String> combinations = Misc.stringCombinations(query);

        List<Distributor> results = new ArrayList<>();
        for (String c : combinations) {
            List<Distributor> newResults = DistributorEndpoints.searchDistributors(c);
            mergeLists(results, newResults);
        }

        if (results.size() > 0) {
            resultsPanel.removeAll();
            for (Distributor d : results) {
                resultsPanel.add(getResultPanel(d));
            }

            resultsScrollPane.setVisible(true);
        } else {
            resultsScrollPane.setVisible(false);
        }

        revalidate();
        repaint();
    }

    private JPanel getResultPanel(Distributor distributor) {
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        Dimension miniButtonSize = new Dimension(30,20);

        JLabel nameLabel = new JLabel(distributor.name);
        nameLabel.setPreferredSize(new Dimension(325,20));
        result.add(nameLabel);

        JLabel websiteLabel = new JLabel(distributor.website);
        websiteLabel.setPreferredSize(new Dimension(325,20));
        result.add(websiteLabel);

        JButton editButton = new JButton("✎");
        editButton.setPreferredSize(miniButtonSize);
        editButton.addActionListener(e -> infoPrompt(distributor, this::edit));
        result.add(editButton);

        JButton deleteButton = new JButton("✕");
        deleteButton.setPreferredSize(miniButtonSize);
        deleteButton.addActionListener(e -> {
            DistributorEndpoints.removeDistributor(distributor);
            performSearch(queryString);
        });
        result.add(deleteButton);

        return result;
    }

    private JPanel getSearchBarPanel() {
        JPanel searchBarPanel = new JPanel(new FlowLayout());

        JTextField searchField = new JTextField("");
        searchField.setPreferredSize(new Dimension(575, 25));
        searchField.setForeground(Color.BLACK);
        searchBarPanel.add(searchField);

        JButton enterButton = new JButton("Search");
        enterButton.addActionListener(e -> performSearch(searchField.getText()));
        searchBarPanel.add(enterButton);

        JButton addButton = new JButton("Add Distributor");
        addButton.addActionListener(e -> infoPrompt(null, this::add));
        searchBarPanel.add(addButton);

        return searchBarPanel;
    }

    private void createResultsScrollPane() {
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVisible(false);
    }

    private void add(Distributor distributor) {
        try {
            DistributorEndpoints.addDistributor(distributor);

            Distributor check = DistributorEndpoints.getDistributorByName(distributor.name);
            if (check != null && check.name.equals(distributor.name)){
                JOptionPane.showMessageDialog(this, "Distributor '" + distributor.name + "' added!");
                performSearch(queryString);
            } else {
                JOptionPane.showMessageDialog(this, "Distributor add failed :(");
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Distributor add failed :(");
            System.out.println(exception.getStackTrace());
        }
    }

    private void edit(Distributor distributor) {
        try {
            DistributorEndpoints.editDistributor(distributor);

            Distributor check = DistributorEndpoints.getDistributorByName(distributor.name);
            if (check != null && check.name.equals(distributor.name)){
                performSearch(queryString);
            } else {
                JOptionPane.showMessageDialog(this, "Edit failed :(");
            }
        }
        catch (Exception exception){
            JOptionPane.showMessageDialog(this, "Edit failed :(");
        }
    }

    private void infoPrompt(Distributor distributor, FunctionCallback callback) {
        JFrame infoFrame = new JFrame("Distributor Info");
        infoFrame.setSize(new Dimension(450,150));
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setVisible(true);

        JPanel infoContent = new JPanel(new GridLayout(3,2,10,10));

        JLabel nameLabel = new JLabel("Name:");
        JLabel websiteLabel = new JLabel("Website:");

        JTextField nameField = new JTextField(20);
        JTextField websiteField = new JTextField(20);

        if (distributor != null) {
            nameField.setText(distributor.name);
            nameField.setEnabled(false);
            websiteField.setText(distributor.website);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String website = websiteField.getText();

                callback.apply(new Distributor(name, website));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Info parsing failed :(\n" + ex.getMessage());
            }

            infoFrame.dispose();
        });

        infoContent.add(nameLabel);
        infoContent.add(nameField);
        infoContent.add(websiteLabel);
        infoContent.add(websiteField);
        infoContent.add(new JLabel());
        infoContent.add(submitButton);

        infoContent.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        infoFrame.add(infoContent);
    }

    public static void mergeLists(List<Distributor> target, List<Distributor> addition) {
        for (Distributor a : addition) {
            if (!target.contains(a)) {
                target.add(a);
            }
        }
    }
}
