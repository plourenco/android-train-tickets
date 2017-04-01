package com.example.users;

public enum UserRole {
    USER(2), INSPECTOR(1), ANONYMOUS(0);

    private int id;

    UserRole(int role) {
        this.id = role;
    }

    public int id() {
        return id;
    }
}
