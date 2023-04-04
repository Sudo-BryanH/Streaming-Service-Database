package ui.panels;

import ui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SearchPanel extends ContentPanel{
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;
    public SearchPanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Search"), BorderLayout.NORTH);
        headerPanel.add(getSearchBarPanel(), BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        createResultsScrollPane();
        add(resultsScrollPane, BorderLayout.WEST);
    }

    private void performSearch(String query) {
        String[] tempResults;

        if (Objects.equals(query, "fill")) {
           tempResults = new String[]{
                   "Result 1", "Result 2", "Result 3", "Result 4",
                   "Result 5", "Result 6", "Result 7", "Result 8",
                   "Result 9", "Result 10", "Result 11", "Result 12"
           };
        } else {
           tempResults = new String[]{"Result 1", "Result 2", "Result 3"};
        }

        resultsPanel.removeAll();
        for (String r : tempResults) {
            resultsPanel.add(getResultPanel(r, "Fake Artist"));
        }

        resultsScrollPane.setVisible(true);
        revalidate();
        repaint();
    }

    private JPanel getResultPanel(String name, String artist) {
        JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setPreferredSize(new Dimension(325,20));
        result.add(nameLabel);

        JLabel artistLabel = new JLabel(artist);
        artistLabel.setPreferredSize(new Dimension(325,20));
        result.add(artistLabel);

        JButton viewButton = new JButton("View");
        viewButton.addActionListener(e -> mainUI.swapPanel(new HomePanel(mainUI)));
        result.add(viewButton);

        return result;
    }

    private JPanel getSearchBarPanel() {
        JPanel searchBarPanel = new JPanel(new FlowLayout());

        JTextField searchField = new JTextField("");
        searchField.setPreferredSize(new Dimension(680, 25));
        searchField.setForeground(Color.BLACK);
        searchBarPanel.add(searchField);

        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(e -> performSearch(searchField.getText()));
        searchBarPanel.add(enterButton);

        return searchBarPanel;
    }

    private void createResultsScrollPane() {
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVisible(false);
    }
}
