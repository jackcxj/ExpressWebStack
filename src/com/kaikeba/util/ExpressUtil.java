package com.kaikeba.util;

import com.kaikeba.bean.Express;

import javax.servlet.http.HttpSession;

public class ExpressUtil {
    public static void setExpressByNumber(HttpSession session, Express express) {
        session.setAttribute("expressNumber", express);
    }

    public static Express getExpressByNumber(HttpSession session) {
        return (Express) session.getAttribute("expressNumber");
    }
}
