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

        add(createTitle("Home"), BorderLayout.NORTH);
    }
}
