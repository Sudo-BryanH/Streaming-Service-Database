package ui.panels;

import ui.MainUI;

import javax.swing.*;

public abstract class ContentPanel extends JPanel {
    protected MainUI mainUI;

    public ContentPanel(MainUI mainUI) {
        this.mainUI = mainUI;
        generate();
    }

    protected abstract void generate();
}
