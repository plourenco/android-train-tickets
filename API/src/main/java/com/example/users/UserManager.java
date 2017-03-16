package com.example.users;

import com.example.Main;
import com.example.mysql.MySQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {

    /**
     * Insert a new user into the database
     */
    public void createUser() {

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
