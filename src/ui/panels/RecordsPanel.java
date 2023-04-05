package ui.panels;

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
        JPanel goatedSongsPanel = generateGoatedSongsPanel();
        add(goatedSongsPanel,BorderLayout.NORTH);
    }

    private JPanel generateGoatedSongsPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(createTitle("Records"), BorderLayout.NORTH);
        JLabel goatedSongs = createBoldedTitle("Goated Songs: " + ReleaseEndpoints.getGoatedSongs());
        goatedSongs.setPreferredSize(new Dimension(20,50));
        goatedSongs.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        headerPanel.add(goatedSongs, BorderLayout.CENTER);
        return headerPanel;
    }
}
