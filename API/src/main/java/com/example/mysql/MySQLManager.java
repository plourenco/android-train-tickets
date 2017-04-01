package com.example.mysql;

import com.example.Main;
import com.example.dataHolder.*;
import com.example.seats.SeatModel;
import com.example.station.StationModel;
import com.example.steps.StepModel;
import com.example.trains.TrainModel;
import com.example.trips.TripModel;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MySQLManager {

    private static Connection conn = null;
    private static final String DB_VERSION = "1.1";
    private static final String host = "localhost";
    private static final String base = "traintickets";
    private static final String pass = "R00t";
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
            /* Create or check table structure */
            try {
                executeSQL(conn, MySQLManager.class.getResourceAsStream("/tables.sql"));
                if(!checkVersion(conn)) {
                    Main.getLogger().severe("Database table structure was changed and is out of date.");
                    Main.getLogger().severe("Please update it with sql files. This warning will be dismissed");
                    setVersion(conn); // this will shut up the warning next time
                }
            }
            catch (SQLException e) {
                Main.getLogger().info("Unable to execute SQL file. Perhaps database is already updated?");
                e.printStackTrace();
            }
            /* Removed: Population HAS to be run manually, copy paste when you need it */
            /* Run procedures */
            try {
                executeProcedure(conn, MySQLManager.class.getResourceAsStream("/procedures.sql"));
            } catch (SQLException proc) {
                Main.getLogger().severe("Error trying to create procedures");
                proc.printStackTrace();
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
     * Check the version of the database structure
     */
    private static boolean checkVersion(Connection conn) {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "SELECT `cfg_value` FROM `config` WHERE `cfg_tag` = 'db_version'");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getString("cfg_value").equals(DB_VERSION)) {
                    return true;
                }
            }
        }
        catch (SQLException ignored) { }
        return false;
    }

    /**
     * Set the version of the database structure
     */
    private static void setVersion(Connection conn) {
        try {
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE `config` SET `cfg_value` = ? WHERE `cfg_tag` = 'db_version'");
            st.setString(1, DB_VERSION);
            st.executeUpdate();
        }
        catch (SQLException ignored) { }
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
                    st.execute(line.contains("version") ? line.replace("{version}", DB_VERSION) : line);
                }
            }
        }
        finally {
            if (st != null) st.close();
        }
    }

    /**
     * This method is used to import .sql procedures into the database
     * This code is also very much "martelated", so use carefully
     * @param conn A SQL connection
     * @param in SQL file location
     * @throws SQLException
     */
    private static void executeProcedure(Connection conn, InputStream in) throws SQLException {
        Scanner s = new Scanner(in);
        Statement st = null;
        try {
            st = conn.createStatement();
            StringBuilder proc = new StringBuilder();
            while (s.hasNext()) {
                String line = s.nextLine().trim();

                if (line.startsWith("#") && s.hasNext()) line = s.nextLine().trim();
                if (line.startsWith("/*") && s.hasNext()) {
                    do {
                        line = s.nextLine().trim();
                    } while (!line.endsWith("*/"));
                }
                if (line.equalsIgnoreCase("delimiter //") && s.hasNext()) line = s.nextLine().trim();
                if (line.equalsIgnoreCase("delimiter ;") && s.hasNext()) line = s.nextLine().trim();

                if ((line.trim().equalsIgnoreCase("end //") || line.trim().equalsIgnoreCase("end$$"))
                        && s.hasNext()) {
                    proc.append("end");
                    st.execute(proc.toString());
                    proc.setLength(0);
                } else {
                    proc.append(line.trim() + "\n");
                }
            }
        } finally {
            if (st != null) st.close();
        }
    }

    /**
     * This serves the purpose of populating the Stations (StationHolder) class holder.
     */
    public static void populateStations() {
        PreparedStatement ps;
        ResultSet rs;
        try {
            HashMap<Integer, StationModel> stations = new HashMap<>();
            ps = MySQLManager.getConnection().prepareStatement("Call getStations()");
            rs = ps.executeQuery();

            while (rs.next()){
                int idStation = rs.getInt("id");
                int stationNumber = rs.getInt("stationNumber");
                String stationName = rs.getString("stationName");
                stations.put(idStation, new StationModel(idStation, stationNumber, stationName));
            }

            StationHolder.setStations(stations);
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
        }
    }

    /**
     * This serves the purpose of populating the Seats (SeatHolder) class holder.
     */
    public static void populateSeats(){
        PreparedStatement ps;
        ResultSet rs;

        try {
            HashMap<Integer, SeatModel> seats = new HashMap<>();
            ps = MySQLManager.getConnection().prepareStatement("SELECT * FROM seats;");
            rs = ps.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                String seatNumber = rs.getString("seatNumber");
                int trainId = rs.getInt("fkTrain");
                seats.put(id, new SeatModel(id, seatNumber, trainId));
            }
            SeatHolder.setSeats(seats);
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
        }
    }

    /**
     * This serves the purpose of populating the Trains (TrainHolder) class holder.
     */
    public static void populateTrains() {
        PreparedStatement ps;
        ResultSet rs;

        try {
            HashMap<Integer, TrainModel> trains = new HashMap<>();
            List<SeatModel> seats;
            ps = MySQLManager.getConnection().prepareStatement("SELECT * FROM trains;");
            rs = ps.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                int maxCap = rs.getInt("maxCapacity");
                String description = rs.getString("description");
                seats = SeatHolder.getSeats()
                        .values()
                        .stream()
                        .filter(s -> s.getTrainId() == id)
                        .collect(Collectors.toList());
                trains.put(id, new TrainModel(id, maxCap, description, seats));
            }
            TrainHolder.setTrains(trains);
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
        }
    }

    /**
     * This serves the purpose of populating the Steps (StepHolders) class holder.
     */
    public static void populateSteps(){
        PreparedStatement ps;
        ResultSet rs;

        try {
            HashMap<Integer, StepModel> steps = new HashMap<>();
            ps = MySQLManager.getConnection().prepareStatement("SELECT * FROM steps;");
            rs = ps.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                int depStationId = rs.getInt("departureStationId");
                int arrStationId = rs.getInt("arrivalStationId");
                int stepNumber = rs.getInt("stepNumber");
                int distance = rs.getInt("distance");
                float price = rs.getFloat("price");
                int waitingTime = rs.getInt("waitingTime");
                int tripId = rs.getInt("fkTrip");
                int duration = rs.getInt("duration");
                Time depTime = rs.getTime("departureTime");
                Time arrTime = rs.getTime("arrivalTime");

                StationModel depStation = StationHolder.getStations().values().stream()
                        .filter(s -> s.getId() == depStationId).findFirst().get();
                StationModel arrStation = StationHolder.getStations().values().stream()
                        .filter(s -> s.getId() == arrStationId).findFirst().get();

                steps.put(id, new StepModel(id, stepNumber, distance, price, waitingTime,
                        duration, depTime, arrTime, depStation, arrStation, tripId));
            }
            StepHolder.setSteps(steps);
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
        }
    }

    /**
     * This serves the purpose of populating the Trips (TripHolder) class holder
     */
    public static void populateTrips(){
        PreparedStatement ps;
        ResultSet rs;

        try {
            HashMap<Integer, TripModel> trips = new HashMap<>();
            List<StepModel> steps;
            ps = MySQLManager.getConnection().prepareStatement("SELECT * FROM trips;");
            rs = ps.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String direction = rs.getString("direction");
                String increment = rs.getString("increment");
                int trainId = rs.getInt("skTrain");
                steps = StepHolder.getSteps()
                        .values().stream()
                        .filter(s -> s.getTripId() == id)
                        .collect(Collectors.toList());

                TrainModel train = TrainHolder.getTrains().get(trainId);

                trips.put(id, new TripModel(id, description, direction, increment,
                        train, steps));
            }

            TripHolder.setTrips(trips);
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
        }
    }
}
