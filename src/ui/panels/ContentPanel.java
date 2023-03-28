package ui.panels;

import ui.MainUI;

import javax.swing.*;
import java.awt.*;

public abstract class ContentPanel extends JPanel {
    protected MainUI mainUI;

    public ContentPanel(MainUI mainUI) {
        this.mainUI = mainUI;
        generate();
    }

    protected abstract void generate();

    protected JLabel createTitle(String title) {
        JLabel label = new JLabel(title);
        Font font = label.getFont();

        label.setFont( new Font(font.getFontName(), Font.BOLD, 35));
        label.setPreferredSize(new Dimension(5,40));

        return label;
    }

    protected JLabel createSubtitle(String subtitle) {
        JLabel label = new JLabel(subtitle);
        Font font = label.getFont();

        label.setFont(new Font(font.getFontName(), font.getStyle(), 20));
        label.setPreferredSize(new Dimension(5,25));

        return label;
    }
}
