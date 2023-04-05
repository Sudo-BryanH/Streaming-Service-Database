package ui.panels;

import backend.ArtistEndpoints;
import backend.ReleaseEndpoints;
import model.Artist;
import ui.MainUI;
import util.Misc;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
    }

    private JLabel generateGoatedSongs() {
        JLabel goatedSongs = createBoldedTitle("Goated Songs: " + ReleaseEndpoints.getGoatedSongs());
        goatedSongs.setPreferredSize(new Dimension(500,40));
        return goatedSongs;
    }

    private JLabel generateMostSongs() {
        List<Artist> artists = ArtistEndpoints.getArtistsMostSongs();
        JLabel label = createBoldedTitle("Artist(s) with Most Songs: " + Misc.artistsToString(artists));
        label.setPreferredSize(new Dimension(500,40));
        return label;
    }
}
