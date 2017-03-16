package com.example.users;

public class UserModel {

    private int id;
    private String username;
    private String email;
    private String password;

    public UserModel(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() { return email; }
}
