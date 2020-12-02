package com.kaikeba.util;

import com.kaikeba.bean.User;

import javax.servlet.http.HttpSession;

public class UserUtil {
    public static String getUserName(HttpSession session){
        return (String) session.getAttribute("adminUserName");
    }

    public static void setUserPhone(HttpSession session, String userPhone){
        session.setAttribute("sysPhone", userPhone);
    }

    public static String getUserPhone(HttpSession session){
        return (String) session.getAttribute("sysPhone");
    }
    public static String getLoginSms(HttpSession session,String userPhone){
        return (String) session.getAttribute(userPhone);
    }
    public static void setLoginSms(HttpSession session,String userPhone,String code){
        session.setAttribute(userPhone,code);
    }
    public static void setWxUser(HttpSession session, User user){
        session.setAttribute("wxUser",user);
    }
    public static User getWxUser(HttpSession session){
        return (User) session.getAttribute("wxUser");
    }
}
