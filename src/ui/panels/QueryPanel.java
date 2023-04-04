package ui.panels;

import backend.QueryEndpoints;
import javafx.util.Pair;
import ui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QueryPanel extends ContentPanel {

    ArrayList<Pair<String, String>> userInfo;
    JScrollPane userScroll;
    JPanel PFButtons;
    JPanel userTable;
    boolean matter = false;
    boolean PorF = false;

    public QueryPanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate(){

        queryUsers();
        JPanel nestedPanels = new JPanel();
        BoxLayout layout = new BoxLayout(nestedPanels, BoxLayout.Y_AXIS);
        nestedPanels.setLayout(layout);
        PFButtons = makePFButtons();
        userScroll = makeScroll();
        userScroll.setViewportView(userTable);
        nestedPanels.add(userScroll);


        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(nestedPanels, BorderLayout.PAGE_START);
        nestedPanels.setVisible(true);

    }

    private JPanel makePFButtons() {
        JPanel temp = new JPanel();
        temp.setLayout(new FlowLayout(1, 0, 5));
        temp.setBackground(Color.gray);
        temp.setMaximumSize(new Dimension(800, 100));

        JButton free = new JButton("Free Users");
        free.setVisible(true);
        free.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    matter = !matter;
                    PorF = false;
                    queryUsers();
               }
           }

        );


        JButton prem = new JButton("Premium Users");
        prem.setVisible(true);
        prem.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   matter = !matter;
                   PorF = true;
                   queryUsers();
               }
           }

        );

        temp.add(free);
        temp.add(prem);
        temp.setOpaque(true);
        return temp;
    }

    private void queryUsers() {
        userInfo = QueryEndpoints.getUser(matter, PorF);

        makeUserTable();
    }

    private void makeUserTable() {
//        queryUsers();

        if (userTable == null) {
            userTable = new JPanel();

            userTable.setLayout(new BoxLayout(userTable, BoxLayout.PAGE_AXIS));
            userTable.setBackground(Color.white);
            userTable.setForeground(Color.white);
        }

        int size = QueryEndpoints.countUsers(matter, PorF);

        userTable.setMaximumSize(new Dimension(800, size*40));
        userTable.setPreferredSize(new Dimension(800, size*40));

        userTable.removeAll();
        for (Pair<String, String> p : userInfo) {
            userTable.add(makeUserPanel(p.getKey(), p.getValue()));
        }

        userTable.setVisible(true);
        userTable.setOpaque(true);

        revalidate();
        repaint();

    }

    private JPanel makeUserPanel(String username, String emailAd) {
        JPanel result = new JPanel(new GridLayout(1, 3));
        result.setMaximumSize(new Dimension(800, 40));
        result.setPreferredSize(new Dimension(800, 40));
        result.setBackground(Color.lightGray);

        JLabel user = new JLabel(username);
        user.setMaximumSize(new Dimension(180,30));
        user.setMinimumSize(new Dimension(180,30));
        user.setPreferredSize(new Dimension(180,30));
        user.setOpaque(true);

        JLabel email = new JLabel(emailAd);
        email.setMaximumSize(new Dimension(180,30));
        email.setMinimumSize(new Dimension(180,30));
        email.setPreferredSize(new Dimension(180,30));
        email.setOpaque(true);


        JButton select = new JButton("Select");

        select.setPreferredSize(new Dimension(80,30));
        select.setMaximumSize(new Dimension(80,30));
        select.setAlignmentX(Component.RIGHT_ALIGNMENT);
        select.setEnabled(true);
        select.setOpaque(true);
        select.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

//                deletePL(pl);

            }

        });
        result.add(user);
        result.add(email);
        result.add(select);
        result.setOpaque(true);
        result.setVisible(true);
        return result;
    }

    private JScrollPane makeScroll() {

        JScrollPane temp = new JScrollPane();
        JScrollBar sb = new JScrollBar();
        sb.setOpaque(true);
        temp.setVerticalScrollBar(sb);
        temp.setBackground(Color.lightGray);
        temp.setMaximumSize(new Dimension(800,450));
        temp.setPreferredSize(new Dimension(800,450));
        temp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        temp.setColumnHeaderView(PFButtons);
        temp.setOpaque(true);
        temp.setVisible(true);
        return temp;
    }



    // TODO have a view that can be selected per person which can then query other stuff like ads served and subscription dates
}
