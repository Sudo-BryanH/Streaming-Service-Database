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
        Font boldFont = new Font(font.getFontName(), Font.BOLD, 30);
        label.setFont(boldFont);
        label.setPreferredSize(new Dimension(5,40));
        return label;
    }
}
