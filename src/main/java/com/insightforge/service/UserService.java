package com.insightforge.service;

import com.insightforge.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * <p>
 * 提供用户相关的核心业务功能，包括用户注册、登录、信息查询和更新等操作。
 * 该接口定义了用户管理的基本方法，支持用户身份认证和信息管理。
 * </p>
 */
public interface UserService {

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    User getUser(Long id);

    /**
     * 用户登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象，登录失败返回null
     */
    User login(String username, String password);

    /**
     * 用户注册
     * <p>
     * 创建新用户账号，并将用户信息添加到缓存
     * </p>
     *
     * @param user 用户对象，包含注册信息
     * @return 注册成功后的用户对象，包含生成的ID
     */
    User register(User user);

    /**
     * 获取所有用户列表
     *
     * @return 所有用户列表
     */
    List<User> getAllUsers();

    /**
     * 更新用户信息
     * <p>
     * 更新用户的基本信息，并同步更新缓存
     * </p>
     *
     * @param user 用户对象，包含要更新的信息
     */
    void updateUser(User user);
}