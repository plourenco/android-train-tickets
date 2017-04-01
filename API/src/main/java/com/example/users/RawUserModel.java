package com.example.users;

import com.example.tickets.TicketModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Raw user model has no hashed passwords, clear as directly from input
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawUserModel {

    private int id;
    private String username;
    private String email;
    private String password;

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }

    public RawUserModel() {

    }

    public RawUserModel(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
