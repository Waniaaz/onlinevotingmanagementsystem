package vms.pkg1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/vms";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<String[]> getData(String query, Object... params) {
        List<String[]> results = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            ResultSet rs = stmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                String[] row = new String[columnCount];

                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }

                results.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // in production, log this properly
        }

        return results;
    }

    public static int executeUpdate(String query, Object... params) {
        int affectedRows = 0;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            affectedRows = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging in production
        }

        return affectedRows;
    }

}
