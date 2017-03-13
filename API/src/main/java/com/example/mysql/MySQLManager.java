package com.example.mysql;

import com.example.Main;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MySQLManager {

    private static Connection conn = null;
    private static final String host = "localhost";
    private static final String base = "traintickets";
    private static final String pass = "root";
    private static final String user = "root";

    /**
     * Get the MySQL connection or prepare one, if it doesn't exist
     * @return conn
     */
    public static Connection getConnection() throws SQLException {
        if ((conn == null) || (conn.isClosed()) || (!conn.isValid(1))) {
            conn = prepareConnection(base);
        }
        return conn;
    }

    /**
     * Start a new mysql connection
     * @return conn
     */
    private static Connection prepareConnection(String base) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + (base != null ? "/" + base : "") + "?autoReconnect=true";
            return DriverManager.getConnection(url, user, pass);
        }
        catch (SQLException | ClassNotFoundException ex) {
            Main.getLogger().severe("Unable to connect to mysql");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Populate the database (perhaps an sql file instead of this?)
     */
    public static void init() {
        conn = prepareConnection(null);

        if(conn != null) {
            try {
                executeSQL(conn, MySQLManager.class.getResourceAsStream("/tables.sql"));
            }
            catch (SQLException e) {
                Main.getLogger().severe("Unable to execute SQL file");
            }
            finally {
                try {
                    conn.setCatalog(base);
                } catch (SQLException e) {
                    Main.getLogger().severe("Unable to select database");
                }
            }
        }
    }


    /**
     * This method will be used to import .sql files into the database
     * @param conn Connection
     * @param in InputStream
     */
    private static void executeSQL(Connection conn, InputStream in) throws SQLException {
        Scanner s = new Scanner(in);
        s.useDelimiter("(;(\r)?\n)|(--\n)");
        Statement st = null;
        try {
            st = conn.createStatement();
            while (s.hasNext()) {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/")) {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }
                if (line.trim().length() > 0) {
                    st.execute(line);
                }
            }
        }
        finally {
            if (st != null) st.close();
        }
    }
}
