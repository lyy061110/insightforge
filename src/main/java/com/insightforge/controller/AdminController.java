package com.insightforge.controller;

import com.insightforge.entity.Post;
import com.insightforge.entity.Report;
import com.insightforge.entity.User;
import com.insightforge.service.PostService;
import com.insightforge.service.ReportService;
import com.insightforge.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 管理员控制器
 * 负责处理后台管理功能，包括管理员登录、举报处理、帖子审核等
 */
@Controller
public class AdminController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    /**
     * 显示管理员登录页面
     * 
     * @return 管理员登录页面视图名称
     */
    @GetMapping("/admin/login")
    public String adminLoginForm() {
        return "admin-login";
    }

    /**
     * 处理管理员登录请求
     * 
     * @param username 管理员用户名
     * @param password 管理员密码
     * @param session HTTP会话，用于存储管理员登录状态
     * @param model Spring MVC模型，用于传递错误信息
     * @return 登录成功重定向到管理后台首页，失败返回登录页面并显示错误
     */
    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {
        // 验证是否为管理员账号
        if (isAdminAccount(username, password)) {
            User user = userService.login(username, password);
            if (user != null) {
                // 登录成功，保存管理员信息到会话
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("isAdmin", true);
                return "redirect:/admin";
            }
        }
        // 登录失败，返回错误提示
        model.addAttribute("error", "管理员账号或密码错误");
        return "admin-login";
    }

    /**
     * 验证是否为管理员账号
     * 
     * @param username 用户名
     * @param password 密码
     * @return 是否为管理员账号
     */
    private boolean isAdminAccount(String username, String password) {
        return ("admin".equals(username) && "admin123".equals(password))
                || ("admin123".equals(username) && "061110".equals(password));
    }

    /**
     * 显示管理后台首页
     * 
     * @param model Spring MVC模型，用于传递举报列表和帖子数据
     * @return 管理后台首页视图名称
     */
    @GetMapping("/admin")
    public String admin(Model model) {
        // 获取所有举报记录
        List<Report> reports = reportService.getAllReports();
        // 获取待处理的举报
        List<Report> pendingReports = reportService.getPendingReports();
        // 获取所有帖子（用于管理）
        List<Post> allPosts = postService.getAllPostsForAdmin();
        model.addAttribute("reports", reports);
        model.addAttribute("pendingReports", pendingReports);
        model.addAttribute("allPosts", allPosts);
        return "admin";
    }

    /**
     * 处理举报记录
     * 
     * @param id 举报记录ID
     * @param action 处理动作（如忽略、删除帖子等）
     * @return 重定向到管理后台首页
     */
    @PostMapping("/admin/report/{id}/resolve")
    public String resolveReport(@PathVariable Long id, @RequestParam String action) {
        reportService.resolveReport(id, action);
        return "redirect:/admin";
    }

    /**
     * 审核通过帖子
     * 
     * @param id 帖子ID
     * @return 重定向到管理后台首页
     */
    @PostMapping("/admin/post/{id}/approve")
    public String approvePost(@PathVariable Long id) {
        postService.approvePost(id);
        return "redirect:/admin";
    }

    /**
     * 隐藏帖子
     * 
     * @param id 帖子ID
     * @return 重定向到管理后台首页
     */
    @PostMapping("/admin/post/{id}/hide")
    public String hidePost(@PathVariable Long id) {
        postService.hidePost(id);
        return "redirect:/admin";
    }

    /**
     * 显示帖子管理页面
     * 
     * @param model Spring MVC模型，用于传递帖子列表
     * @return 管理后台页面视图名称
     */
    @GetMapping("/admin/posts")
    public String adminPosts(Model model) {
        List<Post> posts = postService.getAllPostsForAdmin();
        model.addAttribute("allPosts", posts);
        return "admin";
    }
}
