package ui.panels;

import backend.ArtistEndpoints;
import backend.ReleaseEndpoints;
import ui.MainUI;

import javax.swing.*;
import java.awt.*;

import static ui.panels.UserPaymentsPanel.createBoldedTitle;

public class RecordsPanel extends ContentPanel{
    public RecordsPanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createTitle("Records"), BorderLayout.NORTH);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        add(innerPanel, BorderLayout.WEST);

        innerPanel.add(generateGoatedSongs());
        innerPanel.add(generateMostSongs());
        innerPanel.add(generateReleasesOverHour());
    }

    private JLabel generateGoatedSongs() {
        JLabel goatedSongs = createBoldedTitle("Goated Songs: " + ReleaseEndpoints.getGoatedSongs());
        goatedSongs.setPreferredSize(new Dimension(800,40));
        return goatedSongs;
    }

    private JLabel generateMostSongs() {
        String artists = ArtistEndpoints.getArtistsMostSongs();
        JLabel label = createBoldedTitle("Artist(s) with Most Songs: " + artists);
        label.setPreferredSize(new Dimension(800,40));
        return label;
    }

    private JLabel generateReleasesOverHour() {
        String releases = ReleaseEndpoints.getReleasesOverHour();
        JLabel label = createBoldedTitle("Releases Over 1 Hr Long: " + releases);
        label.setPreferredSize(new Dimension(800,40));
        return label;
    }
}
