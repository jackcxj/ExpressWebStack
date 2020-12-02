package com.kaikeba.controller;

import com.kaikeba.bean.BootStrapTableUser;
import com.kaikeba.bean.Message;
import com.kaikeba.bean.ResultData;
import com.kaikeba.bean.User;
import com.kaikeba.mvc.ResponseBody;
import com.kaikeba.service.UserService;
import com.kaikeba.util.DateFormatUtil;
import com.kaikeba.util.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserController {

    @ResponseBody("/user/console.do")
    public String console(HttpServletRequest request, HttpServletResponse response){
        List<Map<String, Integer>> data = UserService.console();
        Message msg = new Message();
        if(data.size() == 0){
            msg.setStatus(-1);
        }else {
            msg.setStatus(0);
        }
        msg.setData(data);
        String json = JSONUtil.toJSON(msg);
        return json;
    }


    @ResponseBody("/user/list.do")
    public String list(HttpServletRequest request, HttpServletResponse response){
        //1.    获取查询数据的起始索引值
        int offset = Integer.parseInt(request.getParameter("offset"));
        //2.    获取当前页要查询的数据量
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        //3.    进行查询
        List<User> list = UserService.findAll(true, offset, pageNumber);
        List<BootStrapTableUser> list2 = new ArrayList<>();
        for(User u:list){
            String registerTime = DateFormatUtil.format(u.getRegisterTime());
            String loginTime = u.getLoginTime()==null?"无":DateFormatUtil.format(u.getLoginTime());
            String identification = u.getIdentification()!=null?"******":"无";
            BootStrapTableUser c2 = new BootStrapTableUser(u.getId(),u.getUsername(),u.getUserPhone(),identification,u.getPassword(),registerTime,loginTime);
            list2.add(c2);
        }
        List<Map<String, Integer>> console = UserService.console();
        Integer total = console.get(0).get("data_size");
        //4.    将集合封装为 bootstrap-table识别的格式
        ResultData<BootStrapTableUser> data = new ResultData<>();
        data.setRows(list2);
        data.setTotal(total);
        String json = JSONUtil.toJSON(data);
        return json;
    }

    @ResponseBody("/user/insert.do")
    public String insert(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        String userPhone = request.getParameter("userPhone");
        String identification = request.getParameter("identification");
        String password = request.getParameter("password");
        //Express e = new Express(number,company,username,userPhone, UserUtil.getUserPhone(request.getSession()));
        User u = new User(username,userPhone,identification,password);
        boolean flag = UserService.insert(u);
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

    @ResponseBody("/user/find.do")
    public String find(HttpServletRequest request,HttpServletResponse response){
        String userphone = request.getParameter("userphone");
        User u = UserService.findByUserPhone(userphone);
        Message msg = new Message();
        if(u == null){
            msg.setStatus(-1);
            msg.setResult("单号不存在");
        }else{
            msg.setStatus(0);
            msg.setResult("查询成功");
            msg.setData(u);
        }
        String json = JSONUtil.toJSON(msg);
        return json;
    }

    @ResponseBody("/user/update.do")
    public String update(HttpServletRequest request,HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String userPhone = request.getParameter("userPhone");
        String identification = request.getParameter("identification");
        String password = request.getParameter("password");
        User newUser = new User(username,userPhone,identification,password);
        boolean flag = UserService.update(id, newUser);
        Message msg = new Message();
        if(flag){
            msg.setStatus(0);
            msg.setResult("修改成功");
        }else{
            msg.setStatus(-1);
            msg.setResult("修改失败");
        }
        String json = JSONUtil.toJSON(msg);
        return json;
    }

    @ResponseBody("/user/delete.do")
    public String delete(HttpServletRequest request,HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        boolean flag = UserService.delete(id);
        Message msg = new Message();
        if(flag){
            msg.setStatus(0);
            msg.setResult("删除成功");
        }else{
            msg.setStatus(-1);
            msg.setResult("删除失败");
        }
        String json = JSONUtil.toJSON(msg);
        return json;
    }
}
