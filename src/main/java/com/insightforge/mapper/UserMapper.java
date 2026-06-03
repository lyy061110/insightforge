package com.insightforge.mapper;

import com.insightforge.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问层接口
 * 提供用户相关的数据库操作，包括用户的查询、插入、更新等功能
 */
@Mapper
public interface UserMapper {
    /**
     * 根据用户ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户实体对象，如果不存在则返回null
     */
    User findById(@Param("id") Long id);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户实体对象，如果不存在则返回null
     */
    User findByUsername(@Param("username") String username);

    /**
     * 查询所有用户列表
     *
     * @return 用户实体列表
     */
    List<User> findAll();

    /**
     * 插入新用户
     *
     * @param user 用户实体对象
     * @return 影响的行数
     */
    int insert(User user);

    /**
     * 更新用户声望值
     *
     * @param id    用户ID
     * @param delta 声望值变化量（正数增加，负数减少）
     * @return 影响的行数
     */
    int updateReputation(@Param("id") Long id, @Param("delta") int delta);

    /**
     * 更新用户信息
     *
     * @param user 用户实体对象
     * @return 影响的行数
     */
    int update(User user);
}