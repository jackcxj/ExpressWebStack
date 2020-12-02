package com.kaikeba.bean;

import java.sql.Timestamp;
import java.util.Objects;

public class Courier {
    private int id;
    private String username;
    private String userPhone;
    private String identification;
    private String password;
    private int dispatches;
    private Timestamp registerTime;
    private Timestamp loginTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Courier)) return false;
        Courier courier = (Courier) o;
        return id == courier.id &&
                dispatches == courier.dispatches &&
                Objects.equals(username, courier.username) &&
                Objects.equals(userPhone, courier.userPhone) &&
                Objects.equals(identification, courier.identification) &&
                Objects.equals(password, courier.password) &&
                Objects.equals(registerTime, courier.registerTime) &&
                Objects.equals(loginTime, courier.loginTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, userPhone, identification, password, dispatches, registerTime, loginTime);
    }

    @Override
    public String toString() {
        return "Courier{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", identification='" + identification + '\'' +
                ", password='" + password + '\'' +
                ", dispatches=" + dispatches +
                ", registerTime=" + registerTime +
                ", loginTime=" + loginTime +
                '}';
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

    public int getDispatches() {
        return dispatches;
    }

    public void setDispatches(int dispatches) {
        this.dispatches = dispatches;
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

    public Courier() {
    }

    public Courier(int id, String username, String userPhone, String identification, String password, int dispatches, Timestamp registerTime, Timestamp loginTime) {
        this.id = id;
        this.username = username;
        this.userPhone = userPhone;
        this.identification = identification;
        this.password = password;
        this.dispatches = dispatches;
        this.registerTime = registerTime;
        this.loginTime = loginTime;
    }

    public Courier(int id, String username, String userPhone, String identification, String password, int dispatches) {
        this.id = id;
        this.username = username;
        this.userPhone = userPhone;
        this.identification = identification;
        this.password = password;
        this.dispatches = dispatches;
    }

    public Courier(String username, String userPhone, String identification, String password, int dispatches) {
        this.username = username;
        this.userPhone = userPhone;
        this.identification = identification;
        this.password = password;
        this.dispatches = dispatches;
    }
}
