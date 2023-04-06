package backend;

import database.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProjectionEndpoint {

    public static ArrayList<ArrayList<String>> getProjection(String tableName, String[] columns) {
        ArrayList<ArrayList<String>> table = new ArrayList<>();

        try {
            Connection connection = DatabaseManager.getInstance().getConnection();
            String col = columns[0];
            Statement statement = connection.createStatement();
            for (int i = 1; i < columns.length; i++) {
                col += ", " + columns[i];
            }
            System.out.println(col);
            String query = String.format("SELECT %s FROM %s", col, tableName);

            ResultSet rs = statement.executeQuery(query);


            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0; i < columns.length; i++) {
                    Object a = rs.getObject(columns[i]);
                    String s = a.toString();
                    temp.add(s);
                    System.out.println(a);
                }
                table.add(temp);



            }


        } catch (SQLException s) {
            System.out.println(s.getMessage());

        }

        return table;
    }
}
