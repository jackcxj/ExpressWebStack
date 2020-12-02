package com.kaikeba.wx.controller;

import com.kaikeba.bean.Message;
import com.kaikeba.bean.User;
import com.kaikeba.mvc.ResponseBody;
import com.kaikeba.service.CourierService;
import com.kaikeba.util.JSONUtil;
import com.kaikeba.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserController {
    @ResponseBody("/wx/loginSms.do")
    public String sendSms(HttpServletRequest request, HttpServletResponse response) {
        String userPhone = request.getParameter("userPhone");
        //String code = RandomUtil.getCode()+"";
        //boolean flag = SMSUtil.loginSMS(userPhone, code);
        String code = "123456";
        boolean flag = true;
        Message msg = new Message();
        if (flag) {
            //短信发送成功
            msg.setStatus(0);
            msg.setResult("验证码已发送,请查收!");
        } else {
            //短信发送失败
            msg.setStatus(1);
            msg.setResult("验证码下发失败,请检查手机号或稍后再试");
        }
        UserUtil.setLoginSms(request.getSession(), userPhone, code);
        UserUtil.setUserPhone(request.getSession(), userPhone);

        String json = JSONUtil.toJSON(msg);
        return json;
    }

    @ResponseBody("/wx/login.do")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String userPhone = request.getParameter("userPhone");
        String userCode = request.getParameter("code");
        String sysCode = UserUtil.getLoginSms(request.getSession(), userPhone);
        Message msg = new Message();
        if (sysCode == null) {
            //这个手机号未获取短信
            msg.setStatus(-1);
            msg.setResult("手机号码未获取短信");
        } else if (sysCode.equals(userCode)) {
            //这里手机号码和短信一致 , 登陆成功
            //TODO 这个判断应替换为快递员表格查询手机号的结果
            User user = new User();
            boolean flag = CourierService.isCourier(userPhone);
            if (flag) {
                //快递员
                msg.setStatus(1);
                user.setUser(false);
            } else {
                //用户
                msg.setStatus(0);
                user.setUser(true);
            }
            user.setUserPhone(userPhone);
            UserUtil.setWxUser(request.getSession(), user);
        } else {
            //这里是验证码不一致 , 登陆失败
            msg.setStatus(-2);
            msg.setResult("验证码不一致,请检查");
        }
        String json = JSONUtil.toJSON(msg);
        return json;
    }

    @ResponseBody("/wx/userInfo.do")
    public String userInfo(HttpServletRequest request, HttpServletResponse response) {
        User user = UserUtil.getWxUser(request.getSession());
        boolean isUser = user.isUser();
        Message msg = new Message();
        if (isUser) {
            msg.setStatus(0);
        } else {
            msg.setStatus(1);
        }
        msg.setResult(user.getUserPhone());
        String json = JSONUtil.toJSON(msg);
        return json;
    }


    @ResponseBody("/wx/logout.do")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        //1.    销毁session
        request.getSession().invalidate();
        //2.    给客户端回复消息
        Message msg = new Message(0);
        return JSONUtil.toJSON(msg);
    }
}
