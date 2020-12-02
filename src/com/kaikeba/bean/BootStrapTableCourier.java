package com.kaikeba.bean;

public class BootStrapTableCourier {
    private int id;
    private String username;
    private String userPhone;
    private String identification;
    private String password;
    private int dispatches;
    private String registerTime;
    private String loginTime;

    public BootStrapTableCourier() {
    }

    public BootStrapTableCourier(int id, String username, String userPhone, String identification, String password, int dispatches, String registerTime, String loginTime) {
        this.id = id;
        this.username = username;
        this.userPhone = userPhone;
        this.identification = identification;
        this.password = password;
        this.dispatches = dispatches;
        this.registerTime = registerTime;
        this.loginTime = loginTime;
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

    public void setDispatch(int dispatches) {
        this.dispatches = dispatches;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
