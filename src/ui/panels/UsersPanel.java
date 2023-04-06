package ui.panels;

import backend.QueryEndpoints;
import javafx.util.Pair;
import model.User;
import ui.MainUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UsersPanel extends ContentPanel {

    private ArrayList<Pair<String, String>> userInfo;
    private ArrayList<Pair<String, Integer>> userAggInfo;
    private JScrollPane userScroll;

    private JScrollPane userAggScroll;
    private JPanel genButtons;
    private JPanel userTable;
    private boolean matter = false;
    private boolean PorF = false;

    private boolean conditional = false; // f: list of users t: conditional queries
    private JButton condButton;
    private String[] insertCond;
    private String[] insertGen;

    private JPanel userAggTable;

    public UsersPanel(MainUI mainUI) {
        super(mainUI);
    }

    @Override
    protected void generate(){

//        ArrayList<ArrayList<String>> s = ProjectionEndpoint.getProjection("Users", new String[]{"Username", "Email", "Password", "CreationDate"});
        JPanel nestedPanels = new JPanel();
        BoxLayout layout = new BoxLayout(nestedPanels, BoxLayout.Y_AXIS);
        nestedPanels.setLayout(layout);
        genButtons = makeGenButtons();

        userInfo = QueryEndpoints.getUser(false, false, new String[]{"u.Email"});
        makeUserTable();
        userScroll = makeScroll(genButtons);
        userScroll.setViewportView(userTable);
        nestedPanels.add(userScroll);

        userAggInfo = QueryEndpoints.countConditionGroupBy(new String[]{"Username ", " Playlist p", "", " p.Username"});
        makeUserAggTable();
        userAggScroll = makeScroll(makeCondButtons());
        userAggScroll.setViewportView(userAggTable);
        nestedPanels.add(userAggScroll);

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(nestedPanels, BorderLayout.PAGE_START);
        nestedPanels.setVisible(true);

    }

    private JPanel makeGenButtons() {
        JPanel temp = new JPanel();
        temp.setLayout(new GridLayout(1, 4));
        temp.setBackground(Color.gray);
        temp.setMaximumSize(new Dimension(800, 100));

        JLabel exp = new JLabel ("Generic User lookup: ");
        exp.setVisible(true);

        String[] fp = new String[]{"All", "Free", "Premium"};
        String[] userAtt = new String[]{"Email", "Creation Date", "Account Age (Month)"};


        JComboBox<String> fpDrop = new JComboBox<>(fp);
        fpDrop.setVisible(true);
        fpDrop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox1 = (JComboBox) e.getSource();
                Object selected = comboBox1.getSelectedItem();
                switch((String) selected) {
                    case "All":
                        matter = false;
                        break;
                    case "Free":
                        matter = true;
                        PorF = false;
                        break;
                    case "Premium":
                        matter = true;
                        PorF = true;
                        break;
                    default:
                        matter = false;
                        break;

                }
            }
        });

        JComboBox<String> attDrop = new JComboBox<>(userAtt);
        attDrop.setVisible(true);
        attDrop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox1 = (JComboBox) e.getSource();
                Object selected = comboBox1.getSelectedItem();
                switch((String) selected) {
                    case "Email":
                        insertGen = new String[]{"u.Email"};
                        break;
                    case "Account Age (Month)":
                        insertGen = new String[]{"(TO_DATE('2023-04-05', 'YYYY-MM-DD') -  u.CreationDate) / 30"};
                        PorF = false;
                        break;
                    case "Creation Date":
                        insertGen = new String[]{"u.CreationDate"};
                        PorF = true;
                        break;
                    default:

                        break;

                }
            }
        });

        JButton submit = new JButton("Submit");
        submit.setVisible(true);
        submit.setEnabled(true);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                userInfo = QueryEndpoints.getUser(matter, PorF, (insertGen == null) ? new String[]{"u.Email"} : insertGen);
                makeUserTable();
            }
        });




        temp.add(exp);
        temp.add(fpDrop);
        temp.add(attDrop);
        temp.add(submit);
        temp.setOpaque(true);
        return temp;
    }

    private JPanel makeCondButtons() {
        JPanel head = new JPanel();
        head.setLayout(new GridLayout(1, 3));
        head.setBackground(Color.gray);
        head.setMaximumSize(new Dimension(800, 100));

        JLabel exp = new JLabel ("Information about Users by Condition: ");
        exp.setVisible(true);

        JButton submit = new JButton("Submit");
        submit.setVisible(true);
        submit.setEnabled(true);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userAggInfo = QueryEndpoints.countConditionGroupBy(insertCond);
                makeUserAggTable();
            }
        });

        String[] conds = new String[]{"Select Condition", "1. Creation Date", "2. # of Playlists", "3. # of Songs in Library"};
        JComboBox<String> options = new JComboBox<>(conds);
        options.setVisible(true);
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox1 = (JComboBox) e.getSource();
                Object selected = comboBox1.getSelectedItem();
                switch ((String) selected) {
                    case "1. Creation Date":

                        insertCond = new String[]{"EXTRACT(year FROM CreationDate) ", " Users u ", " WHERE u.CreationDate IS NOT NULL", " EXTRACT(year FROM CreationDate)"};
                        break;
                    case "2. # of Playlists":

                        insertCond = new String[]{"Username ", " Playlist p", "", " p.Username"};
                        break;
                    case "3. # of Songs in Library":
                        insertCond = new String[]{"Username ", " AddsToLibrary a", "", " a.Username"};

                        break;
                    default:

                        String s = insertCond[0];
                        break;
                }


            }
        });



        head.add(exp);
        head.add(options);
        head.add(submit);
        head.setVisible(true);
        head.setOpaque(true);

        return head;
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

    private void makeUserAggTable() {
//        queryUsers();

        if (userAggTable == null) {
            userAggTable = new JPanel();

            userAggTable.setLayout(new BoxLayout(userAggTable, BoxLayout.PAGE_AXIS));
            userAggTable.setBackground(Color.white);
            userAggTable.setForeground(Color.white);
        }

        int size = userAggInfo.size();
        System.out.println(size);

        userAggTable.setMaximumSize(new Dimension(800, size*40));
        userAggTable.setPreferredSize(new Dimension(800, size*40));

//        userAggInfo = QueryEndpoints.countCreationDate();

        userAggTable.removeAll();
        for (Pair<String, Integer> p : userAggInfo) {
            userAggTable.add(makeUserAggPanel(p.getKey(), p.getValue()));
        }

        userAggTable.setVisible(true);
        userAggTable.setOpaque(true);

        revalidate();
        repaint();

    }
    private JPanel makeUserAggPanel(String username, int count) {
        JPanel result = new JPanel();
        result.setLayout(new GridLayout(1, 3));
        result.setMaximumSize(new Dimension(800, 40));
        result.setPreferredSize(new Dimension(800, 40));
        result.setBackground(Color.lightGray);

        JLabel user = new JLabel(username);
        user.setMaximumSize(new Dimension(180,30));
        user.setMinimumSize(new Dimension(180,30));
        user.setPreferredSize(new Dimension(180,30));
        user.setOpaque(true);

        JLabel countLabel = new JLabel(Integer.toString(count));
        countLabel.setMaximumSize(new Dimension(180,30));
        countLabel.setMinimumSize(new Dimension(180,30));
        countLabel.setPreferredSize(new Dimension(180,30));
        countLabel.setOpaque(true);


//        JButton select = new JButton("Select");
//
//        select.setPreferredSize(new Dimension(80,30));
//        select.setMaximumSize(new Dimension(80,30));
//        select.setAlignmentX(Component.RIGHT_ALIGNMENT);
//        select.setEnabled(true);
//        select.setOpaque(true);
//        select.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
////                deletePL(pl);
//                JFrame selection = userSelect(username);
//
//            }
//
//        });
        result.add(user);
        result.add(countLabel);
//        result.add(select);
        result.setOpaque(true);
        result.setVisible(true);
        return result;
    }
    private JPanel makeUserPanel(String username, String info) {
        JPanel result = new JPanel(new GridLayout(1, 3));
        result.setMaximumSize(new Dimension(800, 40));
        result.setPreferredSize(new Dimension(800, 40));
        result.setBackground(Color.lightGray);

        JLabel user = new JLabel(username);
        user.setMaximumSize(new Dimension(180,30));
        user.setMinimumSize(new Dimension(180,30));
        user.setPreferredSize(new Dimension(180,30));
        user.setOpaque(true);

        JLabel data = new JLabel(info);
        data.setMaximumSize(new Dimension(180,30));
        data.setMinimumSize(new Dimension(180,30));
        data.setPreferredSize(new Dimension(180,30));
        data.setOpaque(true);


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
                JFrame selection = userSelect(username);

            }

        });
        result.add(user);
        result.add(data);
        result.add(select);
        result.setOpaque(true);
        result.setVisible(true);
        return result;
    }

    private JFrame userSelect(String username) {
        User us = QueryEndpoints.findUserInfo(username);
        JPanel panel = new JPanel();
        GridLayout grid = new GridLayout(8, 2);
        panel.setPreferredSize(new Dimension(400, 650));

        JLabel userLabel = genButtons("Username: ");
        JLabel user = genButtons(username);

        JLabel emailLabel = genButtons("Email: ");
        JLabel email = genButtons(us.getEmail());

        JLabel creationDateLabel = genButtons("Creation Date: ");
        JLabel creationDate = genButtons(us.getCreationDate().toString());

        JLabel tierLabel = genButtons("Tier: ");
        JLabel tier = genButtons((us.getPremium()) ? "Premium" : "Free");

        JLabel adsServedLabel = genButtons("adsServed: ");
        JLabel adsServed = genButtons((us.getPremium()) ? Integer.toString(us.getAdsServed()) : Integer.toString(us.getAdsServed()));

        JLabel subStartLabel = genButtons("Subscription Start Date: ");
        JLabel subStart = genButtons((us.getPremium()) ? ((us.getSubStart() == null)? "N/A":us.getSubStart().toString()): "N/A");

        ArrayList<Pair<String, Integer>> mostLiked = QueryEndpoints.mostLikedGenres(username);
        System.out.println(mostLiked.size());
        String[] cols = new String[]{"Genre", "Liked Songs"};


        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Pair<String, Integer> m : mostLiked) {
            Object[] items = {m.getKey(), m.getValue()};
            model.addRow(items);
        }

        JTable genres = new JTable(model);
//        genres.setMaximumSize(new Dimension( 300, 300));
//        genres.setPreferredSize(new Dimension( 300, 300));
        JTableHeader head = genres.getTableHeader();
        head.setVisible(true);
        head.setOpaque(true);
        genres.setOpaque(true);
        genres.setVisible(true);
        JScrollPane table = new JScrollPane(genres);
        table.setPreferredSize(new Dimension(400, 300));
        table.setMaximumSize(new Dimension(400, 300));
        table.setVisible(true);





        panel.setVisible(true);
        panel.add(userLabel);
        panel.add(user);
        panel.add(emailLabel);
        panel.add(email);
        panel.add(creationDateLabel);
        panel.add(creationDate);
        panel.add(tierLabel);
        panel.add(tier);
        panel.add(adsServedLabel);
        panel.add(adsServed);
        panel.add(subStartLabel);
        panel.add(subStart);
        panel.add(table);


        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(panel);
        frame.setSize(new Dimension(400,650));
        frame.setVisible(true);

        return frame;
    }

    private JLabel genButtons(String text) {
        JLabel temp = new JLabel(text);
        temp.setPreferredSize(new Dimension(150, 40));
        temp.setMaximumSize(new Dimension(240, 40));
        return temp;
    }



    private JScrollPane makeScroll(JPanel column) {

        JScrollPane temp = new JScrollPane();
        JScrollBar sb = new JScrollBar();
        sb.setOpaque(true);
        temp.setVerticalScrollBar(sb);
        temp.setBackground(Color.lightGray);
        temp.setMaximumSize(new Dimension(800,250));
        temp.setPreferredSize(new Dimension(800,250));
        temp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        temp.setColumnHeaderView(column);
        temp.setOpaque(true);
        temp.setVisible(true);
        return temp;
    }



    // TODO have a view that can be selected per person which can then query other stuff like ads served and subscription dates
}
