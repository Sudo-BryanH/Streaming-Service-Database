package ui.panels;

import ui.MainUI;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends ContentPanel{
    public HomePanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Home"), BorderLayout.NORTH);
        headerPanel.add(createSubtitle("Welcome back, {USER}"), BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        JPanel suggestionsPanel = new JPanel();
        suggestionsPanel.setLayout(new BoxLayout(suggestionsPanel, BoxLayout.Y_AXIS));
        suggestionsPanel.add(new JSeparator()); // spacer
        suggestionsPanel.add(getLibrarySuggestions());
        suggestionsPanel.add(new JSeparator()); // spacer
        suggestionsPanel.add(getRandomSuggestions());
        add(suggestionsPanel, BorderLayout.WEST);
    }

    protected JLabel createSuggestionsTitle(String title) {
        JLabel label = new JLabel(title);
        Font font = label.getFont();

        label.setFont( new Font(font.getFontName(), font.getStyle(), 20));
        label.setPreferredSize(new Dimension(5,40));

        return label;
    }

    private JPanel getLibrarySuggestions() {
        JPanel librarySuggestions = new JPanel(new BorderLayout());
        librarySuggestions.add(createSuggestionsTitle("Start off with picks from your library:"), BorderLayout.NORTH);

        JPanel suggestionContents = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        suggestionContents.setPreferredSize(new Dimension(750, 50));
        populateSuggestions(suggestionContents);
        librarySuggestions.add(new JScrollPane(suggestionContents));

        return librarySuggestions;
    }

    private JPanel getRandomSuggestions() {
        JPanel randomSuggestions = new JPanel(new BorderLayout());
        randomSuggestions.add(createSuggestionsTitle("SoundHive's suggestions:"), BorderLayout.NORTH);

        JPanel suggestionContents = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        suggestionContents.setPreferredSize(new Dimension(750, 50));
        populateSuggestions(suggestionContents);
        randomSuggestions.add(new JScrollPane(suggestionContents));

        return randomSuggestions;
    }

    private void populateSuggestions(JPanel suggestionContents) {
        for (int i = 1; i < 13; i++) {
            suggestionContents.add(getSuggestionPanel(String.valueOf(i)));
        }
    }

    private JPanel getSuggestionPanel(String releaseID) {
        JPanel result = new JPanel();
        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.setPreferredSize(new Dimension(100,100));

        JLabel nameLabel = new JLabel("Album " + releaseID);
        nameLabel.setPreferredSize(new Dimension(20, 35));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel artistLabel = new JLabel("Fake Artist");
        artistLabel.setPreferredSize(new Dimension(20, 35));
        artistLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton viewButton = new JButton("View");

        result.add(nameLabel);
        result.add(artistLabel);
        result.add(viewButton);

        return result;
    }
}
