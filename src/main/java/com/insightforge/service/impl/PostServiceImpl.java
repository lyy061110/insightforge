package com.insightforge.service.impl;

import com.insightforge.engine.HashMapEngine;
import com.insightforge.engine.PriorityQueueEngine;
import com.insightforge.engine.StackEngine;
import com.insightforge.entity.BrowseHistory;
import com.insightforge.entity.Like;
import com.insightforge.entity.Post;
import com.insightforge.entity.Report;
import com.insightforge.entity.Save;
import com.insightforge.entity.User;
import com.insightforge.mapper.BrowseHistoryMapper;
import com.insightforge.mapper.CommentMapper;
import com.insightforge.mapper.LikeMapper;
import com.insightforge.mapper.PostMapper;
import com.insightforge.mapper.ReportMapper;
import com.insightforge.mapper.SaveMapper;
import com.insightforge.mapper.UserMapper;
import com.insightforge.service.PostService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 帖子服务实现类
 * <p>
 * 实现帖子相关的核心业务逻辑，包括帖子的创建、查询、点赞、收藏、举报等功能。
 * 该类集成了多种数据结构引擎（HashMap、优先队列、栈）来实现高效的数据缓存和业务处理：
 * - HashMapEngine: 用于缓存帖子和用户数据，提高查询性能
 * - PriorityQueueEngine: 用于管理热门帖子排行和举报审核队列
 * - StackEngine: 用于管理用户收藏、浏览历史和帖子删除恢复
 * </p>
 */
@Service
public class PostServiceImpl implements PostService {

    /** 帖子数据访问层 */
    @Autowired
    private PostMapper postMapper;

    /** 点赞数据访问层 */
    @Autowired
    private LikeMapper likeMapper;

    /** 收藏数据访问层 */
    @Autowired
    private SaveMapper saveMapper;

    /** 举报数据访问层 */
    @Autowired
    private ReportMapper reportMapper;

    /** 浏览历史数据访问层 */
    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;

    /** 评论数据访问层 */
    @Autowired
    private CommentMapper commentMapper;

    /** 用户数据访问层 */
    @Autowired
    private UserMapper userMapper;

    /** HashMap缓存引擎，用于缓存帖子和用户数据 */
    @Autowired
    private HashMapEngine hashMapEngine;

    /** 栈引擎，用于管理收藏、浏览历史和删除恢复 */
    @Autowired
    private StackEngine stackEngine;

    /** 优先队列引擎，用于管理热门帖子和审核队列 */
    @Autowired
    private PriorityQueueEngine priorityQueueEngine;

    /**
     * 初始化方法
     * <p>
     * 在Bean创建后自动执行，刷新所有缓存数据
     * </p>
     */
    @PostConstruct
    public void init() {
        refreshCache();
    }

    /**
     * 创建新帖子
     * <p>
     * 设置帖子的初始状态和计数器，保存到数据库并更新缓存
     * </p>
     *
     * @param post 帖子对象
     * @return 创建成功后的帖子对象
     */
    @Override
    public Post createPost(Post post) {
        // 设置帖子初始状态为待审核
        post.setStatus("PENDING");
        // 初始化各项计数器为0
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setSaveCount(0);
        post.setReportCount(0);
        post.setWeight(0.0);
        // 保存帖子到数据库
        postMapper.insert(post);
        // 获取保存后的完整帖子信息
        Post savedPost = postMapper.findById(post.getId());
        // 更新缓存
        hashMapEngine.refreshPostCache(savedPost);
        return savedPost;
    }

    /**
     * 根据ID获取帖子详情
     * <p>
     * 优先从缓存获取，缓存未命中时从数据库查询并更新缓存
     * </p>
     *
     * @param id 帖子ID
     * @return 帖子对象，不存在则返回null
     */
    @Override
    public Post getPost(Long id) {
        // 优先从缓存获取帖子
        Post post = hashMapEngine.getPost(id);
        if (post == null) {
            // 缓存未命中，从数据库查询
            post = postMapper.findById(id);
            if (post != null) {
                // 更新缓存
                hashMapEngine.refreshPostCache(post);
            }
        }
        // 刷新帖子作者的用户缓存
        if (post != null && post.getAuthorId() != null) {
            hashMapEngine.refreshUserCache(hashMapEngine.getUser(post.getAuthorId()));
        }
        return post;
    }

    /**
     * 获取所有已发布的帖子列表
     * <p>
     * 从缓存获取所有帖子并过滤出已发布状态的帖子
     * </p>
     *
     * @return 已发布的帖子列表
     */
    @Override
    public List<Post> getAllPosts() {
        // 从缓存获取所有帖子
        Collection<Post> cachedPosts = hashMapEngine.getAllPosts();
        if (cachedPosts != null && !cachedPosts.isEmpty()) {
            // 过滤出已发布的帖子
            return cachedPosts.stream()
                    .filter(post -> "PUBLISHED".equals(post.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        }
        // 缓存为空时从数据库查询
        List<Post> posts = postMapper.findAll();
        for (Post post : posts) {
            hashMapEngine.refreshPostCache(post);
        }
        return posts;
    }

    /**
     * 获取所有帖子列表（管理员用）
     * <p>
     * 直接从数据库查询所有状态的帖子，不经过缓存
     * </p>
     *
     * @return 所有帖子列表
     */
    @Override
    public List<Post> getAllPostsForAdmin() {
        return postMapper.findAllAdmin();
    }

    /**
     * 根据分类获取帖子列表
     * <p>
     * 优先从缓存获取，过滤出已发布状态的帖子
     * </p>
     *
     * @param category 分类名称
     * @return 指定分类下的已发布帖子列表
     */
    @Override
    public List<Post> getPostsByCategory(String category) {
        // 从缓存获取指定分类的帖子
        List<Post> cached = hashMapEngine.getPostsByCategory(category);
        if (cached != null && !cached.isEmpty()) {
            // 过滤出已发布的帖子
            return cached.stream()
                    .filter(post -> "PUBLISHED".equals(post.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        }
        // 缓存未命中，从数据库查询
        List<Post> posts = postMapper.findByCategory(category);
        for (Post post : posts) {
            hashMapEngine.refreshPostCache(post);
        }
        return posts;
    }

    /**
     * 根据关键词搜索帖子
     * <p>
     * 优先从缓存搜索，过滤出已发布状态的帖子
     * </p>
     *
     * @param keyword 搜索关键词
     * @return 匹配关键词的已发布帖子列表
     */
    @Override
    public List<Post> searchPosts(String keyword) {
        // 从缓存搜索帖子
        List<Post> cached = hashMapEngine.searchPosts(keyword);
        if (cached != null && !cached.isEmpty()) {
            // 过滤出已发布的帖子
            return cached.stream()
                    .filter(post -> "PUBLISHED".equals(post.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        }
        // 缓存未命中，从数据库搜索
        List<Post> posts = postMapper.searchByTitle(keyword);
        for (Post post : posts) {
            hashMapEngine.refreshPostCache(post);
        }
        return posts;
    }

    /**
     * 获取热门帖子列表
     * <p>
     * 从优先队列获取权重最高的帖子
     * </p>
     *
     * @param limit 返回数量上限
     * @return 热门帖子列表
     */
    @Override
    public List<Post> getTrendingPosts(int limit) {
        return priorityQueueEngine.getTopTrending(limit);
    }

    /**
     * 获取最新帖子列表
     *
     * @param limit 返回数量上限
     * @return 最新帖子列表
     */
    @Override
    public List<Post> getLatestPosts(int limit) {
        return postMapper.findLatest(limit);
    }

    /**
     * 点赞帖子
     * <p>
     * 检查是否已点赞，避免重复点赞。点赞后更新点赞计数和帖子权重。
     * 权重计算公式：权重 = 点赞数 * 2.0 + 评论数 * 3.0 + 收藏数
     * </p>
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    @Override
    public void likePost(Long postId, Long userId) {
        // 检查是否已点赞，避免重复点赞
        Like existing = likeMapper.findByPostAndUser(postId, userId);
        if (existing != null) {
            return;
        }
        // 创建点赞记录
        Like like = new Like();
        like.setPostId(postId);
        like.setUserId(userId);
        likeMapper.insert(like);
        // 更新帖子点赞计数
        postMapper.incrementLikeCount(postId);
        // 从数据库获取最新数据，确保获取到更新后的点赞数
        Post post = postMapper.findById(postId);
        if (post != null) {
            // 计算新的权重值
            int likeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;
            int commentCount = post.getCommentCount() != null ? post.getCommentCount() : 0;
            int saveCount = post.getSaveCount() != null ? post.getSaveCount() : 0;
            double weight = likeCount * 2.0 + commentCount * 3.0 + saveCount;
            post.setWeight(weight);
            // 更新数据库中的权重
            postMapper.updateWeight(postId, weight);
            // 刷新缓存
            hashMapEngine.refreshPostCache(post);
            // 更新热门帖子队列
            priorityQueueEngine.addToTrending(post);
        }
    }

    /**
     * 取消点赞帖子
     * <p>
     * 删除点赞记录，更新点赞计数和帖子权重，同时从收藏中移除（如果存在）
     * </p>
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    @Override
    public void unlikePost(Long postId, Long userId) {
        // 删除点赞记录
        likeMapper.delete(postId, userId);
        // 更新帖子点赞计数
        postMapper.decrementLikeCount(postId);
        // 从数据库获取最新数据，确保获取到更新后的点赞数
        Post post = postMapper.findById(postId);
        if (post != null) {
            // 重新计算权重
            int likeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;
            int commentCount = post.getCommentCount() != null ? post.getCommentCount() : 0;
            int saveCount = post.getSaveCount() != null ? post.getSaveCount() : 0;
            double weight = likeCount * 2.0 + commentCount * 3.0 + saveCount;
            post.setWeight(weight);
            postMapper.updateWeight(postId, weight);
            // 刷新缓存
            hashMapEngine.refreshPostCache(post);
            // 更新热门帖子队列
            priorityQueueEngine.addToTrending(post);
            // 如果帖子在收藏中，也从收藏中移除
            if (isSaved(postId, userId)) {
                stackEngine.removeFromFavorites(userId, postId);
            }
        }
    }

    /**
     * 检查用户是否已点赞该帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 已点赞返回true，否则返回false
     */
    @Override
    public boolean isLiked(Long postId, Long userId) {
        return likeMapper.findByPostAndUser(postId, userId) != null;
    }

    /**
     * 收藏帖子
     * <p>
     * 检查是否已收藏，避免重复收藏。收藏后更新收藏计数和帖子权重。
     * </p>
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    @Override
    public void savePost(Long postId, Long userId) {
        // 检查是否已收藏，避免重复收藏
        Save existing = saveMapper.findByPostAndUser(postId, userId);
        if (existing != null) {
            return;
        }
        // 创建收藏记录
        Save save = new Save();
        save.setPostId(postId);
        save.setUserId(userId);
        saveMapper.insert(save);
        // 更新帖子收藏计数
        postMapper.incrementSaveCount(postId);
        // 添加到用户的收藏栈
        stackEngine.addToFavorites(userId, postId);
        // 从数据库获取最新数据，确保获取到更新后的收藏数
        Post post = postMapper.findById(postId);
        if (post != null) {
            // 计算新的权重值
            int likeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;
            int commentCount = post.getCommentCount() != null ? post.getCommentCount() : 0;
            int saveCount = post.getSaveCount() != null ? post.getSaveCount() : 0;
            double weight = likeCount * 2.0 + commentCount * 3.0 + saveCount;
            post.setWeight(weight);
            postMapper.updateWeight(postId, weight);
            // 刷新缓存
            hashMapEngine.refreshPostCache(post);
            // 更新热门帖子队列
            priorityQueueEngine.addToTrending(post);
        }
    }

    /**
     * 取消收藏帖子
     * <p>
     * 删除收藏记录，更新收藏计数和帖子权重
     * </p>
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    @Override
    public void unsavePost(Long postId, Long userId) {
        // 删除收藏记录
        saveMapper.delete(postId, userId);
        // 更新帖子收藏计数
        postMapper.decrementSaveCount(postId);
        // 从用户的收藏栈中移除
        stackEngine.removeFromFavorites(userId, postId);
        // 从数据库获取最新数据，确保获取到更新后的收藏数
        Post post = postMapper.findById(postId);
        if (post != null) {
            // 重新计算权重
            int likeCount = post.getLikeCount() != null ? post.getLikeCount() : 0;
            int commentCount = post.getCommentCount() != null ? post.getCommentCount() : 0;
            int saveCount = post.getSaveCount() != null ? post.getSaveCount() : 0;
            double weight = likeCount * 2.0 + commentCount * 3.0 + saveCount;
            post.setWeight(weight);
            postMapper.updateWeight(postId, weight);
            // 刷新缓存
            hashMapEngine.refreshPostCache(post);
            // 更新热门帖子队列
            priorityQueueEngine.addToTrending(post);
        }
    }

    /**
     * 检查用户是否已收藏该帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 已收藏返回true，否则返回false
     */
    @Override
    public boolean isSaved(Long postId, Long userId) {
        return saveMapper.findByPostAndUser(postId, userId) != null;
    }

    /**
     * 获取用户收藏的帖子列表
     * <p>
     * 从用户的收藏栈中获取帖子ID列表，再从缓存中获取帖子详情
     * </p>
     *
     * @param userId 用户ID
     * @return 用户收藏的帖子列表
     */
    @Override
    public List<Post> getSavedPosts(Long userId) {
        List<Post> savedPosts = new ArrayList<>();
        // 从收藏栈获取帖子ID列表
        List<Long> favoriteIds = stackEngine.getFavorites(userId);
        // 遍历获取帖子详情
        for (Long postId : favoriteIds) {
            Post post = hashMapEngine.getPost(postId);
            if (post != null) {
                savedPosts.add(post);
            }
        }
        return savedPosts;
    }

    /**
     * 举报帖子
     * <p>
     * 创建举报记录，根据举报原因设置严重程度，当举报数达到阈值时自动标记帖子。
     * 严重程度：HARASSMENT(骚扰)=5, MISINFORMATION(虚假信息)=4, SPAM(垃圾)=3, 其他=1
     * </p>
     *
     * @param postId      帖子ID
     * @param userId      举报用户ID
     * @param reason      举报原因
     * @param description 举报详细描述
     */
    @Override
    public void reportPost(Long postId, Long userId, String reason, String description) {
        // 检查是否已举报，避免重复举报
        Report existing = reportMapper.findByPostAndUser(postId, userId);
        if (existing != null) {
            return;
        }
        // 创建举报记录
        Report report = new Report();
        report.setPostId(postId);
        report.setUserId(userId);
        report.setReason(reason);
        report.setDescription(description);
        report.setStatus("PENDING");
        // 根据举报原因设置严重程度
        int severity;
        if ("HARASSMENT".equals(reason)) {
            severity = 5;
        } else if ("MISINFORMATION".equals(reason)) {
            severity = 4;
        } else if ("SPAM".equals(reason)) {
            severity = 3;
        } else {
            severity = 1;
        }
        report.setSeverity(severity);
        reportMapper.insert(report);
        // 更新帖子举报计数
        postMapper.incrementReportCount(postId);
        // 从数据库获取最新数据，确保获取到更新后的举报数
        Post post = postMapper.findById(postId);
        if (post != null) {
            int reportCount = post.getReportCount() != null ? post.getReportCount() : 0;
            // 当举报数达到3次时，自动标记帖子为待审核状态
            if (reportCount >= 3) {
                postMapper.updateStatus(postId, "FLAGGED");
                post.setStatus("FLAGGED");
            }
            hashMapEngine.refreshPostCache(post);
        }
        // 将举报添加到审核队列
        priorityQueueEngine.addToReviewQueue(report);
    }

    /**
     * 删除帖子
     * <p>
     * 将帖子移入删除历史栈，从缓存中移除，然后从数据库删除
     * </p>
     *
     * @param postId 帖子ID
     */
    @Override
    public void deletePost(Long postId) {
        Post post = getPost(postId);
        if (post != null) {
            // 将帖子推入删除历史栈，支持后续恢复
            stackEngine.pushToDeleteHistory(post);
        }
        // 从缓存中移除帖子
        hashMapEngine.removePostFromCache(postId);
        // 从数据库删除帖子
        postMapper.deleteById(postId);
    }

    /**
     * 恢复最近删除的帖子
     * <p>
     * 从删除历史栈中弹出最近删除的帖子，重新插入数据库并更新缓存
     * </p>
     */
    @Override
    public void restorePost() {
        // 从删除历史栈中弹出最近删除的帖子
        Post post = stackEngine.popFromDeleteHistory();
        if (post == null) {
            return;
        }
        // 重新插入数据库
        postMapper.insert(post);
        // 更新缓存
        hashMapEngine.refreshPostCache(post);
        // 如果帖子是已发布状态，添加到热门队列
        if ("PUBLISHED".equals(post.getStatus())) {
            priorityQueueEngine.addToTrending(post);
        }
    }

    /**
     * 获取指定作者的所有帖子
     *
     * @param userId 作者用户ID
     * @return 该作者的所有帖子列表
     */
    @Override
    public List<Post> getPostsByAuthor(Long userId) {
        return postMapper.findByAuthorId(userId);
    }

    /**
     * 记录用户浏览历史
     * <p>
     * 将浏览记录添加到浏览历史栈和数据库
     * </p>
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    @Override
    public void recordBrowse(Long userId, Long postId) {
        // 添加到浏览历史栈
        stackEngine.addToBrowseHistory(userId, postId);
        // 保存到数据库
        BrowseHistory history = new BrowseHistory();
        history.setUserId(userId);
        history.setPostId(postId);
        browseHistoryMapper.insert(history);
    }

    /**
     * 获取用户浏览历史
     * <p>
     * 从浏览历史栈获取帖子ID列表，再从缓存获取帖子详情
     * </p>
     *
     * @param userId 用户ID
     * @return 用户浏览过的帖子列表
     */
    @Override
    public List<Post> getBrowseHistory(Long userId) {
        List<Post> historyPosts = new ArrayList<>();
        // 从浏览历史栈获取帖子ID列表
        List<Long> postIds = stackEngine.getBrowseHistory(userId);
        // 遍历获取帖子详情
        for (Long postId : postIds) {
            Post post = hashMapEngine.getPost(postId);
            if (post != null) {
                historyPosts.add(post);
            }
        }
        return historyPosts;
    }

    /**
     * 审核通过帖子
     * <p>
     * 将帖子状态从PENDING更新为PUBLISHED，更新缓存并加入热门队列
     * </p>
     *
     * @param postId 帖子ID
     */
    @Override
    public void approvePost(Long postId) {
        Post post = getPost(postId);
        if (post == null) {
            return;
        }
        // 更新帖子状态为已发布
        postMapper.updateStatus(postId, "PUBLISHED");
        post.setStatus("PUBLISHED");
        // 刷新缓存
        hashMapEngine.refreshPostCache(post);
        // 添加到热门帖子队列
        priorityQueueEngine.addToTrending(post);
    }

    /**
     * 隐藏帖子
     * <p>
     * 将帖子状态更新为HIDDEN，使其对普通用户不可见
     * </p>
     *
     * @param postId 帖子ID
     */
    @Override
    public void hidePost(Long postId) {
        Post post = getPost(postId);
        if (post == null) {
            return;
        }
        // 更新帖子状态为已隐藏
        postMapper.updateStatus(postId, "HIDDEN");
        post.setStatus("HIDDEN");
        // 刷新缓存
        hashMapEngine.refreshPostCache(post);
    }

    /**
     * 刷新缓存
     * <p>
     * 重新加载所有用户、帖子和相关数据到缓存中，包括：
     * - 用户缓存
     * - 帖子缓存
     * - 热门帖子队列
     * - 用户收藏栈
     * - 举报审核队列
     * </p>
     */
    @Override
    public void refreshCache() {
        // 加载所有用户到缓存
        List<User> users = userMapper.findAll();
        for (User user : users) {
            hashMapEngine.refreshUserCache(user);
        }

        // 加载所有帖子到缓存（排除已删除的）
        List<Post> allPosts = postMapper.findAllAdmin();
        for (Post post : allPosts) {
            if (!"DELETED".equals(post.getStatus())) {
                hashMapEngine.refreshPostCache(post);
            }
        }

        // 构建已发布帖子列表并刷新热门队列
        List<Post> publishedPosts = new ArrayList<>();
        for (Post post : allPosts) {
            if ("PUBLISHED".equals(post.getStatus())) {
                publishedPosts.add(post);
            }
        }
        priorityQueueEngine.refreshTrending(publishedPosts);

        // 加载用户收藏数据到栈
        for (User user : users) {
            stackEngine.clearUserFavorites(user.getId());
            List<Save> userSaves = saveMapper.findByUserId(user.getId());
            if (userSaves != null) {
                for (Save save : userSaves) {
                    stackEngine.addToFavorites(save.getUserId(), save.getPostId());
                }
            }
        }

        // 加载待处理的举报到审核队列
        List<Report> pendingReports = reportMapper.findByStatus("PENDING");
        if (pendingReports != null) {
            for (Report report : pendingReports) {
                priorityQueueEngine.addToReviewQueue(report);
            }
        }
    }
}