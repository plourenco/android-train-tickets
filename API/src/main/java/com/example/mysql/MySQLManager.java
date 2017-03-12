package com.example.mysql;

import com.example.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLManager {

    private static Connection conn = null;
    private static final String host = "localhost";
    private static final String base = "traintickets";
    private static String pass = "root";
    private static String user = "root";

    /**
     * Get the MySQL connection or prepare one, if it doesn't exist
     * @return conn
     */
    public static Connection getConnection() throws SQLException {
        if ((conn == null) || (conn.isClosed()) || (!conn.isValid(1))) {
            conn = prepareConnection();
        }
        return conn;
    }

    /**
     * Start a new mysql connection
     * @return conn
     */
    private static Connection prepareConnection() {
        try {
            String url = "jdbc:mysql://" + host + "/" + base + "?autoReconnect=true";
            return DriverManager.getConnection(url, user, pass);
        }
        catch (SQLException ex) {
            System.out.println("Unable to connect to database!");
        }
        return null;
    }

    /**
     * Populate the database (perhaps an sql file instead of this?)
     */
    public static void init() {
        conn = prepareConnection();

        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS `users`(" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "`username` VARCHAR(255) NOT NULL, " +
                    "`password` VARCHAR(255) NOT NULL, " +
                    "PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;");
        }
        catch(SQLException e) {
            Main.getLogger().severe(e.getMessage());
        }
    }
}
