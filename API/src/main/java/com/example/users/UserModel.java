package com.example.users;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserModel {

    private int idUser;
    private String username;
    private String email;
    private String password;
    private int roleUser;

    // Need to refactor this
    public UserModel(int id, String username, String email, String password) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public UserModel() {
    }
    public UserModel(int idUser, String username, String password, String email, int roleUser) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roleUser = roleUser;
    }


    public int getIdUser() {
        return idUser;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getRoleUser() {
        return roleUser;
    }
    public String getEmail() { return email; }
}
