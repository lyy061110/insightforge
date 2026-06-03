package com.insightforge.engine;

import com.insightforge.entity.Post;
import com.insightforge.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HashMap引擎
 * 使用HashMap数据结构实现帖子缓存、用户缓存和索引功能
 * 提供快速的数据查询和分类检索能力
 */
@Component
public class HashMapEngine {

    /** 帖子缓存，键为帖子ID，值为帖子对象 */
    private final Map<Long, Post> postCache = new HashMap<>();
    
    /** 用户缓存，键为用户ID，值为用户对象 */
    private final Map<Long, User> userCache = new HashMap<>();
    
    /** 分类索引，键为分类名称，值为该分类下的帖子ID列表 */
    private final Map<String, List<Long>> categoryIndex = new HashMap<>();
    
    /** 标题索引，键为帖子标题，值为帖子ID */
    private final Map<String, Long> titleIndex = new HashMap<>();

    /**
     * 刷新帖子缓存
     * 更新帖子缓存及相关的分类索引和标题索引
     *
     * @param post 要刷新的帖子对象
     */
    public void refreshPostCache(Post post) {
        // 参数校验，确保帖子和帖子ID不为空
        if (post == null || post.getId() == null) {
            return;
        }
        // 将帖子存入缓存
        postCache.put(post.getId(), post);

        // 更新分类索引
        if (post.getCategory() != null) {
            // 如果分类不存在，创建新的列表
            categoryIndex.computeIfAbsent(post.getCategory(), k -> new ArrayList<>());
            List<Long> list = categoryIndex.get(post.getCategory());
            // 避免重复添加
            if (!list.contains(post.getId())) {
                list.add(post.getId());
            }
        }

        // 更新标题索引
        if (post.getTitle() != null) {
            titleIndex.put(post.getTitle(), post.getId());
        }
    }

    /**
     * 从缓存中移除帖子
     * 同时清理相关的分类索引和标题索引
     *
     * @param postId 要移除的帖子ID
     */
    public void removePostFromCache(Long postId) {
        if (postId == null) {
            return;
        }
        // 从缓存中移除帖子
        Post post = postCache.remove(postId);
        if (post != null) {
            // 清理分类索引
            if (post.getCategory() != null) {
                List<Long> list = categoryIndex.get(post.getCategory());
                if (list != null) {
                    list.remove(postId);
                }
            }
            // 清理标题索引
            if (post.getTitle() != null) {
                titleIndex.remove(post.getTitle());
            }
        }
    }

    /**
     * 根据帖子ID获取帖子
     *
     * @param postId 帖子ID
     * @return 帖子对象，不存在时返回null
     */
    public Post getPost(Long postId) {
        return postCache.get(postId);
    }

    /**
     * 根据分类获取帖子列表
     *
     * @param category 分类名称
     * @return 该分类下的帖子列表
     */
    public List<Post> getPostsByCategory(String category) {
        List<Post> result = new ArrayList<>();
        List<Long> postIds = categoryIndex.get(category);
        if (postIds != null) {
            // 根据帖子ID列表从缓存中获取帖子对象
            for (Long postId : postIds) {
                Post post = postCache.get(postId);
                if (post != null) {
                    result.add(post);
                }
            }
        }
        return result;
    }

    /**
     * 根据关键词搜索帖子
     * 在标题索引中进行模糊匹配搜索
     *
     * @param keyword 搜索关键词
     * @return 匹配的帖子列表
     */
    public List<Post> searchPosts(String keyword) {
        List<Post> result = new ArrayList<>();
        // 空值检查
        if (keyword == null || keyword.isEmpty()) {
            return result;
        }
        // 转换为小写进行不区分大小写的搜索
        String lowerKeyword = keyword.toLowerCase();
        // 遍历标题索引进行匹配
        for (Map.Entry<String, Long> entry : titleIndex.entrySet()) {
            if (entry.getKey().toLowerCase().contains(lowerKeyword)) {
                Post post = postCache.get(entry.getValue());
                if (post != null) {
                    result.add(post);
                }
            }
        }
        return result;
    }

    /**
     * 刷新用户缓存
     *
     * @param user 要刷新的用户对象
     */
    public void refreshUserCache(User user) {
        // 参数校验
        if (user == null || user.getId() == null) {
            return;
        }
        // 将用户存入缓存
        userCache.put(user.getId(), user);
    }

    /**
     * 根据用户ID获取用户
     *
     * @param userId 用户ID
     * @return 用户对象，不存在时返回null
     */
    public User getUser(Long userId) {
        return userCache.get(userId);
    }

    /**
     * 获取所有缓存的帖子
     *
     * @return 所有帖子对象的集合
     */
    public Collection<Post> getAllPosts() {
        return postCache.values();
    }

    /**
     * 清空所有缓存和索引
     */
    public void clearAll() {
        postCache.clear();
        userCache.clear();
        categoryIndex.clear();
        titleIndex.clear();
    }
}