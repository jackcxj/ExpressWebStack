package com.kaikeba.dao;

import com.kaikeba.bean.User;
import com.kaikeba.exception.DuplicateCodeException;

import java.util.List;
import java.util.Map;

public interface BaseUserDao {

    /**
     * 用于查询数据库中的全部用户（总数+新增）
     * @return [{size:总数,day:新增}]
     *
     */
    List<Map<String,Integer>> console();

    /**
     * 用于查询所有用户
     * @param limit 是否分页的标记，true表示分页。false表示查询所有用户
     * @param offset SQL语句的起始索引
     * @param pageNumber 页查询的数量
     * @return 快递的集合
     */
    List<User> findAll(boolean limit, int offset, int pageNumber);

    /**
     * 根据快递编号，查询快递员信息
     * @param id 编号
     * @return 查询的快递员信息，编号不存在时返回null
     */
    User findById(int id);

    /**
     * 根据用户手机号码，查询快递员所有的信息
     * @param userPhone 手机号码
     * @return 查询的快递员信息列表
     */
    User findByUserPhone(String userPhone);

    /**
     * 快递的录入
     * @param u 要录入的用户对象
     * @return 录入的结果，true表示成功，false表示失败
     */
    boolean insert(User u) throws DuplicateCodeException;

    /**
     * 快递的修改
     * @param id 要修改的快递员id
     * @param newUser 新的快递员对象（username，userPhone, identification, password）
     * @return 修改的结果，true表示成功，false表示失败
     */
    boolean update(int id,User newUser);

    /**
     * 根据id，删除单个快递员信息
     * @param id 要删除的快递员id
     * @return 删除的结果，true表示成功，false表示失败
     */
    boolean delete(int id);

    /**
     * 根据userPhone判断是否是普通用户
     * SELECT COUNT(USERPHONE) number FROM USER WHERE USERPHONE=?
     * @param userPhone 要查询的电话
     * @return 查询的结果，true表示存在，false表示不存在
     */
    boolean isUser(String userPhone);
}
