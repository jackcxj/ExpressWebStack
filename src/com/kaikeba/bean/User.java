package com.kaikeba.bean;

import java.sql.Timestamp;
import java.util.Objects;

public class User {
    private int id;
    private String username;
    private String userPhone;
    private String identification;
    private String password;
    private Timestamp registerTime;
    private Timestamp loginTime;
    private boolean user;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", identification='" + identification + '\'' +
                ", password='" + password + '\'' +
                ", registerTime=" + registerTime +
                ", loginTime=" + loginTime +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user1 = (User) o;
        return id == user1.id &&
                user == user1.user &&
                Objects.equals(username, user1.username) &&
                Objects.equals(userPhone, user1.userPhone) &&
                Objects.equals(identification, user1.identification) &&
                Objects.equals(password, user1.password) &&
                Objects.equals(registerTime, user1.registerTime) &&
                Objects.equals(loginTime, user1.loginTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, userPhone, identification, password, registerTime, loginTime, user);
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public User() {
    }

    public User(int id, String username, String userPhone, String identification, String password, Timestamp registerTime, Timestamp loginTime) {
        this.id = id;
        this.username = username;
        this.userPhone = userPhone;
        this.identification = identification;
        this.password = password;
        this.registerTime = registerTime;
        this.loginTime = loginTime;
    }

    public User(String username, String userPhone, String identification, String password) {
        this.username = username;
        this.userPhone = userPhone;
        this.identification = identification;
        this.password = password;
    }
}
