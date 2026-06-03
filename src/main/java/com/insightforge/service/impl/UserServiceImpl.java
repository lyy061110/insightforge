package com.insightforge.service.impl;

import com.insightforge.engine.HashMapEngine;
import com.insightforge.entity.User;
import com.insightforge.mapper.UserMapper;
import com.insightforge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现类
 * <p>
 * 实现用户相关的核心业务逻辑，包括用户注册、登录验证、信息查询和更新等功能。
 * 该类集成了HashMap缓存引擎，用于缓存用户数据以提高查询性能。
 * </p>
 */
@Service
public class UserServiceImpl implements UserService {

    /** 用户数据访问层 */
    @Autowired
    private UserMapper userMapper;

    /** HashMap缓存引擎，用于缓存用户数据 */
    @Autowired
    private HashMapEngine hashMapEngine;

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户对象，不存在则返回null
     */
    @Override
    public User getUser(Long id) {
        return userMapper.findById(id);
    }

    /**
     * 用户登录验证
     * <p>
     * 根据用户名查询用户，验证密码是否匹配
     * </p>
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象，失败返回null
     */
    @Override
    public User login(String username, String password) {
        // 根据用户名查询用户
        User user = userMapper.findByUsername(username);
        // 验证用户存在且密码匹配
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * 用户注册
     * <p>
     * 将用户信息保存到数据库，并更新用户缓存
     * </p>
     *
     * @param user 用户对象，包含注册信息
     * @return 注册成功后的用户对象
     */
    @Override
    public User register(User user) {
        // 保存用户到数据库
        userMapper.insert(user);
        // 更新用户缓存
        hashMapEngine.refreshUserCache(user);
        return user;
    }

    /**
     * 获取所有用户列表
     *
     * @return 所有用户列表
     */
    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    /**
     * 更新用户信息
     * <p>
     * 更新数据库中的用户信息，并同步更新缓存
     * </p>
     *
     * @param user 用户对象，包含要更新的信息
     */
    @Override
    public void updateUser(User user) {
        // 更新数据库
        userMapper.update(user);
        // 同步更新缓存
        hashMapEngine.refreshUserCache(user);
    }
}