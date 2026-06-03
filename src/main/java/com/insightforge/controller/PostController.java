package com.insightforge.controller;

import com.insightforge.entity.Comment;
import com.insightforge.entity.Post;
import com.insightforge.service.CommentService;
import com.insightforge.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.insightforge.util.MarkdownUtil;

import java.util.List;

/**
 * 帖子控制器
 * 负责处理帖子相关的所有HTTP请求，包括帖子详情查看、创建、点赞、收藏、评论等功能
 */
@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    /**
     * 显示帖子详情页面
     * 
     * @param id 帖子ID
     * @param model Spring MVC模型，用于向视图传递数据
     * @param session HTTP会话，用于获取当前登录用户信息
     * @return 帖子详情页面视图名称
     */
    @GetMapping("/post/{id}")
    public String postDetail(@PathVariable Long id, Model model, HttpSession session) {
        Post post = postService.getPost(id);
        // 权限校验：非发布状态的帖子仅作者可见
        if (post != null && !"PUBLISHED".equals(post.getStatus())) {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null || !userId.equals(post.getAuthorId())) {
                post = null;
            }
        }
        // 获取帖子评论列表
        List<Comment> comments = commentService.getCommentsByPostId(id);
        // 记录浏览历史
        postService.recordBrowse(1L, id);
        model.addAttribute("post", post);
        if (post != null) {
            // 将Markdown内容转换为HTML以便前端展示
            String htmlContent = MarkdownUtil.markdownToHtml(post.getContent());
            model.addAttribute("postContentHtml", htmlContent);
        }
        model.addAttribute("comments", comments);
        // 标记当前用户是否已点赞和收藏
        model.addAttribute("isLiked", postService.isLiked(id, 1L));
        model.addAttribute("isSaved", postService.isSaved(id, 1L));
        return "post-detail";
    }

    /**
     * 显示创建帖子页面
     * 
     * @return 创建帖子页面视图名称
     */
    @GetMapping("/post/create")
    public String createPostForm() {
        return "create-post";
    }

    /**
     * 处理创建帖子请求
     * 
     * @param title 帖子标题
     * @param content 帖子内容（Markdown格式）
     * @param category 帖子分类
     * @param session HTTP会话，用于获取当前用户ID
     * @return 重定向到首页并显示待审核提示
     */
    @PostMapping("/post/create")
    public String createPost(@RequestParam String title,
                             @RequestParam String content,
                             @RequestParam String category,
                             HttpSession session) {
        Long authorId = (Long) session.getAttribute("userId");
        if (authorId == null) {
            authorId = 1L; // 未登录用户使用默认用户ID
        }
        // 构建帖子对象并保存
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);
        post.setAuthorId(authorId);
        postService.createPost(post);
        return "redirect:/?pending=1";
    }

    /**
     * 点赞帖子
     * 
     * @param id 帖子ID
     * @return 重定向到帖子详情页
     */
    @PostMapping("/post/{id}/like")
    public String likePost(@PathVariable Long id) {
        postService.likePost(id, 1L);
        return "redirect:/post/" + id;
    }

    /**
     * 取消点赞帖子
     * 
     * @param id 帖子ID
     * @return 重定向到帖子详情页
     */
    @PostMapping("/post/{id}/unlike")
    public String unlikePost(@PathVariable Long id) {
        postService.unlikePost(id, 1L);
        return "redirect:/post/" + id;
    }

    /**
     * 收藏帖子
     * 
     * @param id 帖子ID
     * @return 重定向到帖子详情页
     */
    @PostMapping("/post/{id}/save")
    public String savePost(@PathVariable Long id) {
        postService.savePost(id, 1L);
        return "redirect:/post/" + id;
    }

    /**
     * 取消收藏帖子
     * 
     * @param id 帖子ID
     * @return 重定向到帖子详情页
     */
    @PostMapping("/post/{id}/unsave")
    public String unsavePost(@PathVariable Long id) {
        postService.unsavePost(id, 1L);
        return "redirect:/post/" + id;
    }

    /**
     * 添加评论
     * 
     * @param id 帖子ID
     * @param content 评论内容
     * @return 重定向到帖子详情页
     */
    @PostMapping("/post/{id}/comment")
    public String addComment(@PathVariable Long id,
                            @RequestParam String content) {
        commentService.addComment(id, 1L, content);
        return "redirect:/post/" + id;
    }

    /**
     * 删除帖子
     * 
     * @param id 帖子ID
     * @return 重定向到首页
     */
    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }

    /**
     * 恢复已删除的帖子
     * 
     * @return 重定向到首页
     */
    @PostMapping("/post/{id}/restore")
    public String restorePost() {
        postService.restorePost();
        return "redirect:/";
    }
}
