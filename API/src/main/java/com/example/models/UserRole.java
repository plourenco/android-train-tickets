package com.example.models;

public enum UserRole {
    USER(2), INSPECTOR(1), ANONYMOUS(0);

    private int id;

    UserRole(int role) {
        this.id = role;
    }

    public int id() {
        return id;
    }

    public static UserRole idToRole(int role) {
        for(UserRole r : values()) {
            if(r.id() == role)
                return r;
        }
        return null;
    }
}
