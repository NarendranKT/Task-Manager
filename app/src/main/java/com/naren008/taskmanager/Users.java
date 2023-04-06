package com.naren008.taskmanager;

public class Users {
    private String username, email, role, phno,uid, hour, min;

    public Users() {
    }

    public Users(String username, String email, String role, String phno, String uid) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.phno = phno;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }
}
