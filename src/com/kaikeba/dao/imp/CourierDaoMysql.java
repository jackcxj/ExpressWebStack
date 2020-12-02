package com.kaikeba.dao.imp;

import com.kaikeba.bean.Courier;
import com.kaikeba.dao.BaseCourierDao;
import com.kaikeba.exception.DuplicateCodeException;
import com.kaikeba.util.DruidUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourierDaoMysql implements BaseCourierDao {
    //用于查询数据库中的全部快递员（总数+日注册人数）
    public static final String SQL_CONSOLE = "SELECT " +
            "COUNT(ID) data_size," +
            "COUNT(TO_DAYS(REGISTERTIME)=TO_DAYS(NOW()) OR NULL) data_day" +
            " FROM COURIER";
    //用于查询数据库中的所有快递员信息
    public static final String SQL_FIND_ALL = "SELECT * FROM COURIER";
    //用于分页查询数据库中的快递员信息
    public static final String SQL_FIND_LIMIT = "SELECT * FROM COURIER LIMIT ?,?";
    //通过ID查询快递员信息
    public static final String SQL_FIND_BY_ID = "SELECT * FROM COURIER WHERE ID=?";
    //通过用户手机号查询用户所有快递
    public static final String SQL_FIND_BY_USERPHONE = "SELECT * FROM COURIER WHERE USERPHONE=?";
    //录入快递员
    public static final String SQL_INSERT = "INSERT INTO COURIER (USERNAME,USERPHONE,IDENTIFICATION,PASSWORD,DISPATCHES,REGISTERTIME) VALUES(?,?,?,?,0,NOW())";
    //快递员修改
    public static final String SQL_UPDATE = "UPDATE COURIER SET USERNAME=?,USERPHONE=?,IDENTIFICATION=?,PASSWORD=? WHERE ID=?";
    //快递员删除
    public static final String SQL_DELETE = "DELETE FROM COURIER WHERE ID=?";
    //通过手机号查询是否是快递员
    public static final String SQL_IS_COURIER = "SELECT COUNT(USERPHONE) number FROM COURIER WHERE USERPHONE=?";


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
    public List<Courier> findAll(boolean limit, int offset, int pageNumber) {
        ArrayList<Courier> data = new ArrayList<>();
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
                int dispatches = result.getInt("dispatches");
                Timestamp registerTime = result.getTimestamp("registerTime");
                Timestamp loginTime = result.getTimestamp("loginTime");
                Courier c = new Courier(id,username,userPhone,identification,password,dispatches,registerTime,loginTime);
                data.add(c);
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
    public Courier findById(int id) {
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
                int dispatches = result.getInt("dispatches");
                Timestamp registerTime = result.getTimestamp("registerTime");
                Timestamp loginTime = result.getTimestamp("loginTime");
                Courier c = new Courier(id,username,userPhone,identification,password,dispatches,registerTime,loginTime);
                return c;
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
    public Courier findByUserPhone(String userPhone) {
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
                int dispatches = result.getInt("dispatches");
                Timestamp registerTime = result.getTimestamp("registerTime");
                Timestamp loginTime = result.getTimestamp("loginTime");
                Courier c = new Courier(id,username,userPhone,identification,password,dispatches,registerTime,loginTime);
                return c;
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
     * INSERT INTO COURIER (ID,USERNAME,USERPHONE,IDENTIFICATION,PASSWORD,DISPATCHES,REGISTERTIME,LOGINTIME) VALUES(?,?,?,?,?,0,NOW(),?)
     * @param c 要录入的快递员对象
     * @return 录入的结果，true表示成功，false表示失败
     */
    @Override
    public boolean insert(Courier c) throws DuplicateCodeException{
        //1.    连接的获取
        Connection conn = DruidUtil.getConnection();
        //2.    预编译SQL语句
        PreparedStatement state = null;
        try {
            state = conn.prepareStatement(SQL_INSERT);
            //3.    填充参数
            state.setString(1,c.getUsername());
            state.setString(2,c.getUserPhone());
            state.setString(3,c.getIdentification());
            state.setString(4,c.getPassword());
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
     * @param newCourier 新的快递员对象（username，userPhone,identification,password）
     * @return 修改的结果，true表示成功，false表示失败
     */
    @Override
    public boolean update(int id, Courier newCourier) {
        //1.    连接的获取
        Connection conn = DruidUtil.getConnection();
        PreparedStatement state = null;
        //2.    预编译SQL语句
        try {
            state = conn.prepareStatement(SQL_UPDATE);
            state.setString(1,newCourier.getUsername());
            state.setString(2,newCourier.getUserPhone());
            state.setString(3,newCourier.getIdentification());
            state.setString(4,newCourier.getPassword());
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
     * 根据userPhone判断是否是快递员
     * SELECT COUNT(USERPHONE) number FROM COURIER WHERE USERPHONE=?
     * @param userPhone 要查询的电话
     * @return 查询的结果，true表示存在，false表示不存在
     */
    @Override
    public boolean isCourier(String userPhone) {
        //1.    连接的获取
        Connection conn = DruidUtil.getConnection();
        PreparedStatement state = null;
        ResultSet result = null;
        int number = 0;
        //2.    预编译SQL语句
        try {
            state = conn.prepareStatement(SQL_IS_COURIER);
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
