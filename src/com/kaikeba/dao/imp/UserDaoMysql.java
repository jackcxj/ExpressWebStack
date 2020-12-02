package com.kaikeba.dao.imp;

import com.kaikeba.bean.User;
import com.kaikeba.dao.BaseUserDao;
import com.kaikeba.exception.DuplicateCodeException;
import com.kaikeba.util.DruidUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoMysql implements BaseUserDao {
    //用于查询数据库中的全部用户（总数+日注册人数）
    public static final String SQL_CONSOLE = "SELECT " +
            "COUNT(ID) data_size," +
            "COUNT(TO_DAYS(REGISTERTIME)=TO_DAYS(NOW()) OR NULL) data_day" +
            " FROM USER";
    //用于查询数据库中的所有用户信息
    public static final String SQL_FIND_ALL = "SELECT * FROM USER";
    //用于分页查询数据库中的用户信息
    public static final String SQL_FIND_LIMIT = "SELECT * FROM USER LIMIT ?,?";
    //通过ID查询用户信息
    public static final String SQL_FIND_BY_ID = "SELECT * FROM USER WHERE ID=?";
    //通过用户手机号查询用户所有快递
    public static final String SQL_FIND_BY_USERPHONE = "SELECT * FROM USER WHERE USERPHONE=?";
    //录入用户
    public static final String SQL_INSERT = "INSERT INTO USER (USERNAME,USERPHONE,IDENTIFICATION,PASSWORD,REGISTERTIME) VALUES(?,?,?,?,NOW())";
    //用户修改
    public static final String SQL_UPDATE = "UPDATE USER SET USERNAME=?,USERPHONE=?,IDENTIFICATION=?, PASSWORD=? WHERE ID=?";
    //用户删除
    public static final String SQL_DELETE = "DELETE FROM USER WHERE ID=?";
    //通过手机号查询是否是用户
    public static final String SQL_IS_USER = "SELECT COUNT(USERPHONE) number FROM USER WHERE USERPHONE=?";


    /**
     * 用于查询数据库中的全部快递（总数+新增），待取件快递（总数+新增）
     *
     * @return [{size:总数,day:新增},{size:总数,day:新增}]
     */
    @Override
    public List<Map<String, Integer>> console() {
        ArrayList<Map<String,Integer>> data = new ArrayList<>();
        //1.    获取数据库的连接
        Connection conn = DruidUtil.getConnection();
        PreparedStatement state = null;
        ResultSet result = null;
        //2.    预编译SQL语句
        try {
            state = conn.prepareStatement(SQL_CONSOLE);
            //3.    填充参数(可选)
            //4.    执行SQL语句
            result = state.executeQuery();
            //5.    获取执行的结果
            if(result.next()){
                int data_size = result.getInt("data_size");
                int data_day = result.getInt("data_day");
                Map data1 = new HashMap();
                data1.put("data_size",data_size);
                data1.put("data_day",data_day);
                data.add(data1);
            }
            //6.    资源的释放
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidUtil.close(conn,state,result);
        }
        return data;
    }

    /**
     * 用于查询所有快递员
     * @param limit 是否分页的标记，true表示分页。false表示查询所有快递员
     * @param offset SQL语句的起始索引
     * @param pageNumber 页查询的数量
     * @return 快递的集合
     */
    @Override
    public List<User> findAll(boolean limit, int offset, int pageNumber) {
        ArrayList<User> data = new ArrayList<>();
        //1.    获取数据库的连接
        Connection conn = DruidUtil.getConnection();
        PreparedStatement state = null;
        ResultSet result = null;
        //2.    预编译SQL语句
        try {
            if(limit) {
                state = conn.prepareStatement(SQL_FIND_LIMIT);
                //3.    填充参数(可选)
                state.setInt(1,offset);
                state.setInt(2,pageNumber);
            }else {
                state = conn.prepareStatement(SQL_FIND_ALL);
            }

            //4.    执行SQL语句
            result = state.executeQuery();
            //5.    获取执行的结果
            while(result.next()){
                int id = result.getInt("id");
                String username = result.getString("username");
                String userPhone = result.getString("userPhone");
                String identification = result.getString("identification");
                String password = result.getString("password");
                Timestamp registerTime = result.getTimestamp("registerTime");
                Timestamp loginTime = result.getTimestamp("loginTime");
                User u = new User(id,username,userPhone,identification,password,registerTime,loginTime);
                data.add(u);
            }
            //6.    资源的释放
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidUtil.close(conn,state,result);
        }
        return data;
    }

    /**
     * 根据快递编号，查询快递员信息
     * @param id 编号
     * @return 查询的快递员信息，编号不存在时返回null
     */
    @Override
    public User findById(int id) {
        //1.    获取数据库的连接
        Connection conn = DruidUtil.getConnection();
        PreparedStatement state = null;
        ResultSet result = null;
        //2.    预编译SQL语句
        try {
            state = conn.prepareStatement(SQL_FIND_BY_ID);
            //3.    填充参数(可选)
            state.setInt(1, id);
            //4.    执行SQL语句
            result = state.executeQuery();
            //5.    获取执行的结果
            if(result.next()){
                String username = result.getString("username");
                String userPhone = result.getString("userPhone");
                String identification = result.getString("identification");
                String password = result.getString("password");
                Timestamp registerTime = result.getTimestamp("registerTime");
                Timestamp loginTime = result.getTimestamp("loginTime");
                User u = new User(id,username,userPhone,identification,password,registerTime,loginTime);
                return u;
            }
            //6.    资源的释放
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidUtil.close(conn,state,result);
        }
        return null;
    }

    /**
     * 根据用户手机号码，查询快递员所有的信息
     * @param userPhone 手机号码
     * @return 查询的快递员信息列表
     */
    @Override
    public User findByUserPhone(String userPhone) {
        //1.    获取数据库的连接
        Connection conn = DruidUtil.getConnection();
        PreparedStatement state = null;
        ResultSet result = null;
        //2.    预编译SQL语句
        try {
            state = conn.prepareStatement(SQL_FIND_BY_USERPHONE);
            //3.    填充参数(可选)
            state.setString(1,userPhone);
            //4.    执行SQL语句
            result = state.executeQuery();
            //5.    获取执行的结果
            if(result.next()){
                int id = result.getInt("id");
                String username = result.getString("username");
                String identification = result.getString("identification");
                String password = result.getString("password");
                Timestamp registerTime = result.getTimestamp("registerTime");
                Timestamp loginTime = result.getTimestamp("loginTime");
                User u = new User(id,username,userPhone,identification,password,registerTime,loginTime);
                return u;
            }
            //6.    资源的释放
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidUtil.close(conn,state,result);
        }
        return null;
    }

    /**
     * 快递的录入
     * INSERT INTO COURIER (USERNAME,USERPHONE,IDENTIFICATION,PASSWORD,REGISTERTIME,LOGINTIME) VALUES(?,?,?,?,?,0,NOW(),?)
     * @param u 要录入的快递员对象
     * @return 录入的结果，true表示成功，false表示失败
     */
    @Override
    public boolean insert(User u) throws DuplicateCodeException{
        //1.    连接的获取
        Connection conn = DruidUtil.getConnection();
        //2.    预编译SQL语句
        PreparedStatement state = null;
        try {
            state = conn.prepareStatement(SQL_INSERT);
            //3.    填充参数
            state.setString(1,u.getUsername());
            state.setString(2,u.getUserPhone());
            state.setString(3,u.getIdentification());
            state.setString(4,u.getPassword());
            //4.    执行SQL语句,并获取执行结果
            return state.executeUpdate()>0?true:false;
        } catch (SQLException e1) {
            /*
            * 这块具体什么异常还需要测试
            * */
            if(e1.getMessage().endsWith("for key 'code'")){
                //是因为取件码重复,而出现了异常
                DuplicateCodeException e2 = new DuplicateCodeException(e1.getMessage());
                throw e2;
            }else{
                e1.printStackTrace();
            }
        }finally {
            //5.    释放资源
            DruidUtil.close(conn,state,null);
        }
        return false;
    }

     /**
     * 快递的修改
     * UPDATE COURIER SET USERNAME=?,USERPHONE=?,IDENTIFICATION=?,PASSWORD=? WHERE ID=?
     * @param id 要修改的快递员id
     * @param newUser 新的快递员对象（username，userPhone,identification,password）
     * @return 修改的结果，true表示成功，false表示失败
     */
    @Override
    public boolean update(int id, User newUser) {
        //1.    连接的获取
        Connection conn = DruidUtil.getConnection();
        PreparedStatement state = null;
        //2.    预编译SQL语句
        try {
            state = conn.prepareStatement(SQL_UPDATE);
            state.setString(1,newUser.getUsername());
            state.setString(2,newUser.getUserPhone());
            state.setString(3,newUser.getIdentification());
            state.setString(4,newUser.getPassword());
            state.setInt(5,id);
            return state.executeUpdate()>0?true:false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidUtil.close(conn,state,null);
        }
        return false;
    }

    /**
     * 根据id，删除单个快递员信息
     * DELETE FROM COURIER WHERE ID=?
     * @param id 要删除的快递员id
     * @return 删除的结果，true表示成功，false表示失败
     */
    @Override
    public boolean delete(int id) {
        //1.    连接的获取
        Connection conn = DruidUtil.getConnection();
        PreparedStatement state = null;
        //2.    预编译SQL语句
        try {
            state = conn.prepareStatement(SQL_DELETE);
            state.setInt(1,id);
            return state.executeUpdate()>0?true:false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidUtil.close(conn,state,null);
        }
        return false;
    }

    /**
     * 根据userPhone判断是否是普通用户
     * SELECT COUNT(USERPHONE) number FROM USER WHERE USERPHONE=?
     * @param userPhone 要查询的电话
     * @return 查询的结果，true表示存在，false表示不存在
     */
    @Override
    public boolean isUser(String userPhone) {
        //1.    连接的获取
        Connection conn = DruidUtil.getConnection();
        PreparedStatement state = null;
        ResultSet result = null;
        int number = 0;
        //2.    预编译SQL语句
        try {
            state = conn.prepareStatement(SQL_IS_USER);
            state.setString(1,userPhone);
            //4.    执行SQL语句
            result = state.executeQuery();
            //5.    获取执行的结果
            while(result.next()){
                number = result.getInt("number");
            }
            if(number == 1){
                return true;
            }else{
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidUtil.close(conn,state,result);
        }
        return false;
    }
}
