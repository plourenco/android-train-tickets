package com.example.dao;

import com.example.Main;
import com.example.exceptions.EncryptionException;
import com.example.exceptions.InvalidCreditCardException;
import com.example.exceptions.InvalidUserDataException;
import com.example.models.CreditCardModel;
import com.example.models.RawUserModel;
import com.example.models.UserModel;
import com.example.mysql.MySQLManager;
import com.example.security.PasswordSecurity;
import com.example.models.UserRole;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserManager {

    /**
     * Insert a new user into the database
     * @param user RawUserModel
     */
    public int createUser(RawUserModel user, UserRole role) {

        if (user == null) {
            throw new InvalidUserDataException("NullPointerException on user. User is not set.");
        }
        if (user.getUsername().isEmpty() || user.getUsername() == null || user.getPassword().isEmpty()
                || user.getPassword() == null || user.getEmail().isEmpty() || user.getEmail() == null) {
            throw new InvalidUserDataException("Invalid data. " +
                    "Please verify if everything is correct. User not created");
        }
        String hash;
        try {
            hash = PasswordSecurity.generateHash(user.getPassword());
        }
        catch(NoSuchAlgorithmException e) {
            throw new EncryptionException("Unable to encrypt password");
        }

        try {
            PreparedStatement ps = MySQLManager.getConnection().prepareStatement(
                    "INSERT INTO `users` VALUES (NULL, ?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getUsername());
            ps.setString(2, hash);
            ps.setString(3, user.getEmail());
            ps.setInt(4, role.id());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("User creation failed, no rows affected");
            }

            try (ResultSet generatedKey = ps.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    return generatedKey.getInt(1);
                } else {
                    throw new SQLException("User creation failed, no ID obtained");
                }
            }
        }
        catch (SQLException e) {
            Main.getLogger().severe(e.getMessage());
            return -1;
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
                        rs.getString("password"),
                        rs.getInt("role"));
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
    @Deprecated
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
                        rs.getString("password"),
                        rs.getInt("role"));
            }
        }
        catch(SQLException e) {
            Main.getLogger().severe(e.getMessage());
        }
        return null;
    }

    public int getUserRole(int id) {
        try {
            PreparedStatement ps = MySQLManager.getConnection().prepareStatement("SELECT role FROM users WHERE id = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("role");
            }
        } catch (SQLException e){
            Main.getLogger().severe(e.getMessage());
        }
        return -1;
    }

    public String saveCard(CreditCardModel cardModel, int userId) {
        try {
            PreparedStatement ps = MySQLManager.getConnection().prepareStatement("Call saveCard(?,?,?,?)");
            ps.setDate(1, cardModel.getExpiryDate());
            ps.setLong(2, Long.valueOf(cardModel.getCreditCardNumber()));
            ps.setInt(3, Integer.valueOf(cardModel.getCvv2()));
            ps.setInt(4, userId);

            int row = ps.executeUpdate();
            return "success;"+row;
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            return sql.getMessage();
        }
    }

    public CreditCardModel getCard(int userId) {
        try {
            PreparedStatement ps = MySQLManager.getConnection().prepareStatement("SELECT * FROM creditcards where fkUser=?");
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String number = rs.getString("number");
                String cvv2 = rs.getString("cvv2");
                Date expDate = rs.getDate("expiryDate");

                CreditCardModel card = new CreditCardModel(number, expDate, cvv2);
                return card;
            } else {
                throw new InvalidCreditCardException("CC for user does not exist");
            }
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            return null;
        }
    }
}
