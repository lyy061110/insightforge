package com.insightforge.service;

import com.insightforge.entity.Post;

import java.util.List;

/**
 * 帖子服务接口
 * <p>
 * 提供帖子相关的核心业务功能，包括帖子的创建、查询、点赞、收藏、举报等操作。
 * 该接口定义了帖子管理的完整生命周期方法，支持普通用户和管理员的各项操作。
 * </p>
 */
public interface PostService {

    /**
     * 创建新帖子
     *
     * @param post 帖子对象，包含帖子的基本信息
     * @return 创建成功后的帖子对象，包含生成的ID和默认状态
     */
    Post createPost(Post post);

    /**
     * 根据ID获取帖子详情
     *
     * @param id 帖子ID
     * @return 帖子对象，如果不存在则返回null
     */
    Post getPost(Long id);

    /**
     * 获取所有已发布的帖子列表
     * <p>
     * 仅返回状态为PUBLISHED的帖子，供普通用户浏览
     * </p>
     *
     * @return 已发布的帖子列表
     */
    List<Post> getAllPosts();

    /**
     * 获取所有帖子列表（管理员用）
     * <p>
     * 返回所有状态的帖子，包括待审核、已发布、已隐藏等，供管理员管理
     * </p>
     *
     * @return 所有帖子列表
     */
    List<Post> getAllPostsForAdmin();

    /**
     * 根据分类获取帖子列表
     *
     * @param category 分类名称
     * @return 指定分类下的已发布帖子列表
     */
    List<Post> getPostsByCategory(String category);

    /**
     * 根据关键词搜索帖子
     *
     * @param keyword 搜索关键词
     * @return 匹配关键词的已发布帖子列表
     */
    List<Post> searchPosts(String keyword);

    /**
     * 获取热门帖子列表
     * <p>
     * 根据帖子的权重（点赞、评论、收藏综合计算）排序返回热门帖子
     * </p>
     *
     * @param limit 返回的帖子数量上限
     * @return 热门帖子列表
     */
    List<Post> getTrendingPosts(int limit);

    /**
     * 获取最新帖子列表
     * <p>
     * 按创建时间倒序返回最新的帖子
     * </p>
     *
     * @param limit 返回的帖子数量上限
     * @return 最新帖子列表
     */
    List<Post> getLatestPosts(int limit);

    /**
     * 点赞帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    void likePost(Long postId, Long userId);

    /**
     * 取消点赞帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    void unlikePost(Long postId, Long userId);

    /**
     * 检查用户是否已点赞该帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 如果已点赞返回true，否则返回false
     */
    boolean isLiked(Long postId, Long userId);

    /**
     * 收藏帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    void savePost(Long postId, Long userId);

    /**
     * 取消收藏帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     */
    void unsavePost(Long postId, Long userId);

    /**
     * 检查用户是否已收藏该帖子
     *
     * @param postId 帖子ID
     * @param userId 用户ID
     * @return 如果已收藏返回true，否则返回false
     */
    boolean isSaved(Long postId, Long userId);

    /**
     * 获取用户收藏的帖子列表
     *
     * @param userId 用户ID
     * @return 用户收藏的帖子列表
     */
    List<Post> getSavedPosts(Long userId);

    /**
     * 举报帖子
     *
     * @param postId      帖子ID
     * @param userId      举报用户ID
     * @param reason      举报原因
     * @param description 举报详细描述
     */
    void reportPost(Long postId, Long userId, String reason, String description);

    /**
     * 删除帖子
     * <p>
     * 将帖子移入删除历史记录，支持后续恢复
     * </p>
     *
     * @param postId 帖子ID
     */
    void deletePost(Long postId);

    /**
     * 恢复最近删除的帖子
     * <p>
     * 从删除历史记录中恢复最近删除的帖子
     * </p>
     */
    void restorePost();

    /**
     * 获取指定作者的所有帖子
     *
     * @param userId 作者用户ID
     * @return 该作者的所有帖子列表
     */
    List<Post> getPostsByAuthor(Long userId);

    /**
     * 记录用户浏览历史
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void recordBrowse(Long userId, Long postId);

    /**
     * 获取用户浏览历史
     *
     * @param userId 用户ID
     * @return 用户浏览过的帖子列表
     */
    List<Post> getBrowseHistory(Long userId);

    /**
     * 审核通过帖子
     * <p>
     * 将帖子状态从PENDING更新为PUBLISHED，使其对用户可见
     * </p>
     *
     * @param postId 帖子ID
     */
    void approvePost(Long postId);

    /**
     * 隐藏帖子
     * <p>
     * 将帖子状态更新为HIDDEN，使其对用户不可见
     * </p>
     *
     * @param postId 帖子ID
     */
    void hidePost(Long postId);

    /**
     * 刷新缓存
     * <p>
     * 重新加载所有用户、帖子和相关数据到缓存中
     * </p>
     */
    void refreshCache();
}