package com.insightforge.engine;

import com.insightforge.entity.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 栈引擎
 * 使用栈数据结构管理用户收藏、浏览历史和删除历史
 * 提供后进先出（LIFO）的数据访问模式
 */
@Component
public class StackEngine {

    /** 用户收藏栈映射，键为用户ID，值为该用户的收藏帖子ID栈 */
    private final Map<Long, Deque<Long>> userFavorites = new HashMap<>();
    
    /** 用户浏览历史栈映射，键为用户ID，值为该用户的浏览帖子ID栈 */
    private final Map<Long, Deque<Long>> userBrowseHistory = new HashMap<>();
    
    /** 帖子删除历史栈，用于记录已删除的帖子 */
    private final Deque<Post> deleteHistory = new ArrayDeque<>();

    /** 浏览历史的最大保存数量 */
    private static final int MAX_BROWSE_HISTORY = 50;

    /**
     * 添加帖子到用户收藏
     * 将帖子ID压入用户收藏栈顶
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    public void addToFavorites(Long userId, Long postId) {
        // 参数校验
        if (userId == null || postId == null) {
            return;
        }
        // 如果用户没有收藏栈，则创建一个新的
        userFavorites.computeIfAbsent(userId, k -> new ArrayDeque<>());
        Deque<Long> favorites = userFavorites.get(userId);
        // 避免重复收藏
        if (!favorites.contains(postId)) {
            favorites.push(postId);
        }
    }

    /**
     * 从用户收藏中移除帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    public void removeFromFavorites(Long userId, Long postId) {
        if (userId == null || postId == null) {
            return;
        }
        Deque<Long> favorites = userFavorites.get(userId);
        if (favorites != null) {
            favorites.remove(postId);
        }
    }

    /**
     * 获取用户的收藏列表
     *
     * @param userId 用户ID
     * @return 用户收藏的帖子ID列表，按收藏时间倒序排列
     */
    public List<Long> getFavorites(Long userId) {
        List<Long> result = new ArrayList<>();
        if (userId == null) {
            return result;
        }
        Deque<Long> favorites = userFavorites.get(userId);
        if (favorites != null) {
            result.addAll(favorites);
        }
        return result;
    }

    /**
     * 检查用户是否收藏了指定帖子
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 如果已收藏返回true，否则返回false
     */
    public boolean isFavorited(Long userId, Long postId) {
        if (userId == null || postId == null) {
            return false;
        }
        Deque<Long> favorites = userFavorites.get(userId);
        return favorites != null && favorites.contains(postId);
    }

    /**
     * 添加帖子到用户浏览历史
     * 如果帖子已存在于历史中，先移除再添加到栈顶
     * 当历史记录超过最大限制时，移除最早的记录
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    public void addToBrowseHistory(Long userId, Long postId) {
        if (userId == null || postId == null) {
            return;
        }
        // 如果用户没有浏览历史栈，则创建一个新的
        userBrowseHistory.computeIfAbsent(userId, k -> new ArrayDeque<>());
        Deque<Long> history = userBrowseHistory.get(userId);
        // 如果帖子已在历史中，先移除以便重新添加到栈顶
        history.remove(postId);
        // 将帖子压入栈顶
        history.push(postId);
        // 当历史记录超过最大限制时，移除栈底元素（最早的记录）
        while (history.size() > MAX_BROWSE_HISTORY) {
            history.removeLast();
        }
    }

    /**
     * 获取用户的浏览历史
     *
     * @param userId 用户ID
     * @return 用户浏览过的帖子ID列表，按浏览时间倒序排列
     */
    public List<Long> getBrowseHistory(Long userId) {
        List<Long> result = new ArrayList<>();
        if (userId == null) {
            return result;
        }
        Deque<Long> history = userBrowseHistory.get(userId);
        if (history != null) {
            result.addAll(history);
        }
        return result;
    }

    /**
     * 将帖子压入删除历史栈
     *
     * @param post 被删除的帖子对象
     */
    public void pushToDeleteHistory(Post post) {
        if (post != null) {
            deleteHistory.push(post);
        }
    }

    /**
     * 从删除历史栈中弹出最近的删除记录
     *
     * @return 最近删除的帖子对象，栈为空时返回null
     */
    public Post popFromDeleteHistory() {
        return deleteHistory.pollFirst();
    }

    /**
     * 清空用户的收藏列表
     *
     * @param userId 用户ID
     */
    public void clearUserFavorites(Long userId) {
        if (userId == null) {
            return;
        }
        Deque<Long> favorites = userFavorites.get(userId);
        if (favorites != null) {
            favorites.clear();
        }
    }

    /**
     * 清空用户的浏览历史
     *
     * @param userId 用户ID
     */
    public void clearUserHistory(Long userId) {
        if (userId == null) {
            return;
        }
        Deque<Long> history = userBrowseHistory.get(userId);
        if (history != null) {
            history.clear();
        }
    }
}