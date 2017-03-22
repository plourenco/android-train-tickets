package com.example.users;

import com.example.Main;
import com.example.exceptions.InvalidUserDataException;
import com.example.mysql.MySQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserManager {

    /**
     * Insert a new user into the database
     * @param user UserModel
     */
    public int createUser(UserModel user) {

        if (user == null) { throw new InvalidUserDataException("NullPointerException on user. User is not set."); }

        if ( user.getUsername().isEmpty() || user.getUsername() == null || user.getPassword().isEmpty()
                || user.getPassword() == null || user.getEmail().isEmpty() || user.getEmail() == null) {
            throw new InvalidUserDataException("Invalid data. Please verify if everything is correct. User not created"); }

        try {
            PreparedStatement ps = MySQLManager.getConnection().prepareStatement("" +
                    "INSERT INTO `users` VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, user.getIdUser());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            int rows = ps.executeUpdate();

            if (rows == 0) { throw new SQLException("User creation failed, no rows affected"); }

            try (ResultSet generatedKey = ps.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    return generatedKey.getInt(1);
                } else {
                    throw new SQLException("User creation failed, no ID obtained");
                }
            }

        } catch (SQLException e){
            Main.getLogger().severe(e.getMessage());
            return -1;
    public void createUser() {
        try {
            PreparedStatement ps = MySQLManager.getConnection().prepareStatement(
                    "INSERT INTO `users`() VALUES()");
        }
        catch(SQLException e) {
            Main.getLogger().severe(e.getMessage());
        }
    }

    /**
     * Get a user from the database by username
     * @param username String
     */
    public UserModel getUserByEmail(String username) {
        try {
            PreparedStatement ps = MySQLManager.getConnection().prepareStatement(
                    "SELECT * FROM `users` WHERE email = ?");

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new UserModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"));
            }
        }
        catch(SQLException e) {
            Main.getLogger().severe(e.getMessage());
        }
        return null;
    }

    /**
     * Get a user from the database by id
     * @param id int
     */
    public UserModel getUserById(int id) {
        try {
            PreparedStatement ps = MySQLManager.getConnection().prepareStatement(
                    "SELECT * FROM `users` WHERE id = ?");

            ps.setString(1, String.valueOf(id));
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new UserModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"));
            }
        }
        catch(SQLException e) {
            Main.getLogger().severe(e.getMessage());
        }
        return null;
    }
}
