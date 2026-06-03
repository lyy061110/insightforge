package com.insightforge.controller;

import com.insightforge.entity.Post;
import com.insightforge.entity.User;
import com.insightforge.service.PostService;
import com.insightforge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户控制器
 * 负责处理用户认证、注册、个人信息管理等相关功能
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    /**
     * 显示登录页面
     * 
     * @return 登录页面视图名称
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    /**
     * 处理用户登录请求
     * 
     * @param username 用户名
     * @param password 密码
     * @param session HTTP会话，用于存储用户登录状态
     * @param model Spring MVC模型，用于传递错误信息
     * @return 登录成功重定向到首页，失败返回登录页面并显示错误
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            // 登录成功，保存用户信息到会话
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            // 判断是否为管理员账号
            session.setAttribute("isAdmin", "admin".equals(user.getUsername()) || "admin123".equals(user.getUsername()));
            return "redirect:/";
        }
        // 登录失败，返回错误提示
        model.addAttribute("error", "用户名或密码错误");
        return "login";
    }

    /**
     * 显示注册页面
     * 
     * @return 注册页面视图名称
     */
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    /**
     * 处理用户注册请求
     * 
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱地址
     * @return 重定向到登录页面
     */
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email) {
        // 构建用户对象并注册
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userService.register(user);
        return "redirect:/login";
    }

    /**
     * 用户登出
     * 
     * @param session HTTP会话，用于清除登录状态
     * @return 重定向到首页
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 使会话失效，清除所有登录信息
        session.invalidate();
        return "redirect:/";
    }

    /**
     * 显示用户个人主页
     * 
     * @param id 用户ID
     * @param model Spring MVC模型，用于传递用户信息和帖子数据
     * @return 用户个人主页视图名称
     */
    @GetMapping("/user/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        // 获取用户基本信息
        User profileUser = userService.getUser(id);
        // 获取用户发布的帖子
        List<Post> posts = postService.getPostsByAuthor(id);
        // 获取用户收藏的帖子
        List<Post> savedPosts = postService.getSavedPosts(id);
        // 获取用户浏览历史
        List<Post> browseHistory = postService.getBrowseHistory(id);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("posts", posts);
        model.addAttribute("savedPosts", savedPosts);
        model.addAttribute("browseHistory", browseHistory);
        return "user-profile";
    }

    /**
     * 显示用户设置页面
     * 
     * @param session HTTP会话，用于获取当前登录用户
     * @param model Spring MVC模型，用于传递用户信息
     * @return 用户设置页面视图名称，未登录则重定向到登录页
     */
    @GetMapping("/user/settings")
    public String userSettings(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        // 未登录检查
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userService.getUser(userId);
        model.addAttribute("user", user);
        return "user-settings";
    }

    /**
     * 处理用户设置更新请求
     * 
     * @param email 新邮箱地址
     * @param password 新密码（可选，为空则不修改）
     * @param session HTTP会话，用于获取当前登录用户
     * @param model Spring MVC模型，用于传递用户信息和成功消息
     * @return 用户设置页面视图名称，未登录则重定向到登录页
     */
    @PostMapping("/user/settings")
    public String updateUserSettings(@RequestParam String email,
                                     @RequestParam(required = false) String password,
                                     HttpSession session,
                                     Model model) {
        Long userId = (Long) session.getAttribute("userId");
        // 未登录检查
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userService.getUser(userId);
        user.setEmail(email);
        // 仅当密码不为空时才更新密码
        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }
        userService.updateUser(user);
        model.addAttribute("user", user);
        model.addAttribute("success", "Settings updated successfully");
        return "user-settings";
    }
}
