package com.insightforge.controller;

import com.insightforge.entity.Post;
import com.insightforge.service.PostService;
import com.insightforge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页控制器
 * 负责处理网站首页、搜索和分类浏览等核心展示功能
 */
@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    /**
     * 显示网站首页
     * 
     * @param model Spring MVC模型，用于传递帖子列表和分类数据
     * @param pending 待审核提示参数，值为"1"时显示待审核消息
     * @return 首页视图名称
     */
    @GetMapping("/")
    public String index(Model model, @RequestParam(required = false) String pending) {
        // 获取所有已发布的帖子
        List<Post> posts = postService.getAllPosts();
        // 获取热门帖子（前5篇）
        List<Post> trendingPosts = postService.getTrendingPosts(5);
        // 从帖子中提取所有不重复的分类
        List<String> categories = posts.stream()
                .map(Post::getCategory)
                .filter(c -> c != null && !c.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("posts", posts);
        model.addAttribute("trendingPosts", trendingPosts);
        model.addAttribute("categories", categories);
        // 判断是否显示待审核提示消息
        if ("1".equals(pending)) {
            model.addAttribute("showPendingMessage", true);
        }
        return "index";
    }

    /**
     * 搜索帖子
     * 
     * @param q 搜索关键词
     * @param model Spring MVC模型，用于传递搜索结果
     * @return 搜索结果页面视图名称
     */
    @GetMapping("/search")
    public String search(@RequestParam String q, Model model) {
        // 根据关键词搜索帖子
        List<Post> posts = postService.searchPosts(q);
        model.addAttribute("posts", posts);
        model.addAttribute("keyword", q);
        return "search";
    }

    /**
     * 按分类浏览帖子
     * 
     * @param category 分类名称
     * @param model Spring MVC模型，用于传递分类下的帖子列表
     * @return 分类页面视图名称
     */
    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, Model model) {
        // 获取指定分类下的所有帖子
        List<Post> posts = postService.getPostsByCategory(category);
        model.addAttribute("posts", posts);
        model.addAttribute("category", category);
        return "category";
    }
}
