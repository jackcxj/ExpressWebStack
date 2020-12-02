package com.kaikeba.service;

import com.kaikeba.bean.Courier;
import com.kaikeba.dao.BaseCourierDao;
import com.kaikeba.dao.imp.CourierDaoMysql;
import com.kaikeba.exception.DuplicateCodeException;

import java.util.List;
import java.util.Map;

public class CourierService {
    private static BaseCourierDao dao = new CourierDaoMysql();

    public static List<Map<String, Integer>> console() {
        return dao.console();
    }

    /**
     * 用于查询所有快递员
     * @param limit 是否分页的标记，true表示分页。false表示查询所有快递员
     * @param offset SQL语句的起始索引
     * @param pageNumber 页查询的数量
     * @return 快递的集合
     */
    public static List<Courier> findAll(boolean limit, int offset, int pageNumber) {
        return dao.findAll(limit,offset,pageNumber);
    }

    /**
     * 根据快递编号，查询快递员信息
     * @param id 编号
     * @return 查询的快递员信息，编号不存在时返回null
     */
    public static Courier findById(int id) {
        return dao.findById(id);
    }

    /**
     * 根据用户手机号码，查询快递员所有的信息
     * @param userPhone 手机号码
     * @return 查询的快递员信息列表
     */
    public static Courier findByUserPhone(String userPhone) {
        return dao.findByUserPhone(userPhone);
    }

    /**
     * 快递的录入
     * INSERT INTO COURIER (ID,USERNAME,USERPHONE,IDENTIFICATION,PASSWORD,DISPATCHES,REGISTERTIME,LOGINTIME) VALUES(?,?,?,?,?,0,NOW(),?)
     * @param c 要录入的快递员对象
     * @return 录入的结果，true表示成功，false表示失败
     */
    public static boolean insert(Courier c) {
        try {
            return dao.insert(c);
        } catch (DuplicateCodeException e) {
            e.printStackTrace();
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
    public static boolean update(int id, Courier newCourier) {
        return dao.update(id, newCourier);
    }

    /**
     * 根据id，删除单个快递员信息
     * DELETE FROM COURIER WHERE ID=?
     * @param id 要删除的快递员id
     * @return 删除的结果，true表示成功，false表示失败
     */
    public static boolean delete(int id) {
       return dao.delete(id);
    }

    /**
     * 根据userPhone判断是否是快递员
     * SELECT COUNT(USERPHONE) number FROM COURIER WHERE USERPHONE=?
     * @param userPhone 要查询的电话
     * @return 查询的结果，true表示存在，false表示不存在
     */
    public static boolean isCourier(String userPhone){return dao.isCourier(userPhone);}
}
