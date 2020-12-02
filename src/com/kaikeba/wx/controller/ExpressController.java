package com.kaikeba.wx.controller;

import com.kaikeba.bean.BootStrapTableExpress;
import com.kaikeba.bean.Express;
import com.kaikeba.bean.Message;
import com.kaikeba.bean.User;
import com.kaikeba.mvc.ResponseBody;
import com.kaikeba.service.ExpressService;
import com.kaikeba.util.DateFormatUtil;
import com.kaikeba.util.ExpressUtil;
import com.kaikeba.util.JSONUtil;
import com.kaikeba.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ExpressController {
    @ResponseBody("/wx/findExpressByUserPhone.do")
    public String findByUserPhone(HttpServletRequest request, HttpServletResponse response) {
        User wxUser = UserUtil.getWxUser(request.getSession());
        String userPhone = wxUser.getUserPhone();
        List<Express> list = ExpressService.findByUserPhone(userPhone);
        List<BootStrapTableExpress> list2 = new ArrayList<>();
        for (Express e : list) {
            String inTime = DateFormatUtil.format(e.getInTime());
            String outTime = e.getOutTime() == null ? "未出库" : DateFormatUtil.format(e.getOutTime());
            String status = e.getStatus() == 0 ? "待取件" : "已取件";
            String code = e.getCode() == null ? "已取件" : e.getCode();
            BootStrapTableExpress e2 = new BootStrapTableExpress(e.getId(), e.getNumber(), e.getUsername(), e.getUserPhone(), e.getCompany(), code, inTime, outTime, status, e.getSysPhone());
            list2.add(e2);
        }
        Message msg = new Message();
        if (list.size() == 0) {
            msg.setStatus(-1);
        } else {
            msg.setStatus(0);
            Stream<BootStrapTableExpress> status0Express = list2.stream().filter(express -> {
                if (express.getStatus().equals("待取件")) {
                    return true;
                } else {
                    return false;
                }
            }).sorted((o1, o2) -> {
                long o1Time = DateFormatUtil.toTime(o1.getInTime());
                long o2Time = DateFormatUtil.toTime(o2.getInTime());
                return (int) (o1Time - o2Time);
            });
            Stream<BootStrapTableExpress> status1Express = list2.stream().filter(express -> {
                if (express.getStatus().equals("已取件")) {
                    return true;
                } else {
                    return false;
                }
            }).sorted((o1, o2) -> {
                long o1Time = DateFormatUtil.toTime(o1.getInTime());
                long o2Time = DateFormatUtil.toTime(o2.getInTime());
                return (int) (o1Time - o2Time);
            });
            Object[] s0 = status0Express.toArray();
            Object[] s1 = status1Express.toArray();
            Map data = new HashMap<>();
            data.put("status0", s0);
            data.put("status1", s1);
            msg.setData(data);
        }
        String json = JSONUtil.toJSON(msg.getData());
        return json;
    }

    @ResponseBody("/wx/userExpressList.do")
    public String expressList(HttpServletRequest request, HttpServletResponse response) {
        String userPhone = request.getParameter("userPhone");
        List<Express> list = ExpressService.findByUserPhoneAndStatus(userPhone, 0);
        List<BootStrapTableExpress> list2 = new ArrayList<>();
        for (Express e : list) {
            String inTime = DateFormatUtil.format(e.getInTime());
            String outTime = e.getOutTime() == null ? "未出库" : DateFormatUtil.format(e.getOutTime());
            String status = e.getStatus() == 0 ? "待取件" : "已取件";
            String code = e.getCode() == null ? "已取件" : e.getCode();
            BootStrapTableExpress e2 = new BootStrapTableExpress(e.getId(), e.getNumber(), e.getUsername(), e.getUserPhone(), e.getCompany(), code, inTime, outTime, status, e.getSysPhone());
            list2.add(e2);
        }
        Message msg = new Message();
        if (list.size() == 0) {
            msg.setStatus(-1);
            msg.setResult("未查询到快递");
        } else {
            msg.setStatus(0);
            msg.setResult("查询成功");
            msg.setData(list2);
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/ul/indexQueryExpress.do")
    public String QueryExpress(HttpServletRequest request, HttpServletResponse response) {
        String expressNum = request.getParameter("expressNum");
        Express e = ExpressService.findByNumber(expressNum);

        Message msg = new Message();
        if (e == null) {
            msg.setStatus(-1);
            msg.setResult("未查询到快递");
        } else {
            ExpressUtil.setExpressByNumber(request.getSession(), e);
            msg.setStatus(0);
            msg.setResult("查询成功");
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/wx/checkExpress.do")
    public String checkExpress(HttpServletRequest request, HttpServletResponse response) {
        Express e = ExpressUtil.getExpressByNumber(request.getSession());
        String inTime = DateFormatUtil.format(e.getInTime());
        String outTime = e.getOutTime() == null ? "未出库" : DateFormatUtil.format(e.getOutTime());
        String status = e.getStatus() == 0 ? "待取件" : "已取件";
        String code = e.getCode() == null ? "已取件" : e.getCode();
        BootStrapTableExpress e2 = new BootStrapTableExpress(e.getId(), e.getNumber(), e.getUsername(), e.getUserPhone(), e.getCompany(), code, inTime, outTime, status, e.getSysPhone());

        Message msg = new Message();
        if (e == null) {
            msg.setStatus(-1);
        } else {
            msg.setStatus(0);
            msg.setData(e2);
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/wx/insert.do")
    public String insert(HttpServletRequest request, HttpServletResponse response){
        String number = request.getParameter("number");
        String company = request.getParameter("company");
        String username = request.getParameter("username");
        String userPhone = request.getParameter("userPhone");
        //Express e = new Express(number,company,username,userPhone, UserUtil.getUserPhone(request.getSession()));
        Express e = new Express(number,username,userPhone,company,UserUtil.getUserPhone(request.getSession()));
        boolean flag = ExpressService.insert(e);
        Message msg = new Message();
        if(flag){
            //录入成功
            msg.setStatus(0);
            msg.setResult("快递录入成功!");
        }else{
            //录入失败
            msg.setStatus(-1);
            msg.setResult("快递录入失败!");
        }
        String json = JSONUtil.toJSON(msg);
        return json;
    }

    @ResponseBody("/wx/historyExpress.do")
    public String historyExpress(HttpServletRequest request, HttpServletResponse response) {
        String sysPhone = UserUtil.getUserPhone(request.getSession());
        List<Express> list = ExpressService.findBySysPhone(sysPhone);
        List<BootStrapTableExpress> list2 = new ArrayList<>();
        for (Express e : list) {
            String inTime = DateFormatUtil.format(e.getInTime());
            String outTime = e.getOutTime() == null ? "未出库" : DateFormatUtil.format(e.getOutTime());
            String status = e.getStatus() == 0 ? "待取件" : "已取件";
            String code = e.getCode() == null ? "已取件" : e.getCode();
            BootStrapTableExpress e2 = new BootStrapTableExpress(e.getId(), e.getNumber(), e.getUsername(), e.getUserPhone(), e.getCompany(), code, inTime, outTime, status, e.getSysPhone());
            list2.add(e2);
        }

        Message msg = new Message();
        if (list.size() == 0) {
            msg.setStatus(-1);
        } else {
            msg.setStatus(0);
            Stream<BootStrapTableExpress> status0Express = list2.stream().filter(express -> {
                if (express.getStatus().equals("待取件")) {
                    return true;
                } else {
                    return false;
                }
            }).sorted((o1, o2) -> {
                long o1Time = DateFormatUtil.toTime(o1.getInTime());
                long o2Time = DateFormatUtil.toTime(o2.getInTime());
                return (int) (o1Time - o2Time);
            });
            Stream<BootStrapTableExpress> status1Express = list2.stream().filter(express -> {
                if (express.getStatus().equals("已取件")) {
                    return true;
                } else {
                    return false;
                }
            }).sorted((o1, o2) -> {
                long o1Time = DateFormatUtil.toTime(o1.getInTime());
                long o2Time = DateFormatUtil.toTime(o2.getInTime());
                return (int) (o1Time - o2Time);
            });
            Object[] s0 = status0Express.toArray();
            Object[] s1 = status1Express.toArray();
            Map data = new HashMap<>();
            data.put("status0", s0);
            data.put("status1", s1);
            msg.setData(data);
        }
        String json = JSONUtil.toJSON(msg.getData());
        return json;
    }

}
