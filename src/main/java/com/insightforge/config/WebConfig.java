package com.insightforge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Web配置类
 * 用于配置Spring MVC相关的设置，如拦截器等
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 注册拦截器
     * 将自定义的请求URI拦截器添加到拦截器链中
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册请求URI拦截器，用于记录当前请求的URI
        registry.addInterceptor(new RequestUriInterceptor());
    }

    /**
     * 请求URI拦截器
     * 内部静态类，用于在请求中存储当前请求的URI路径
     */
    public static class RequestUriInterceptor implements HandlerInterceptor {
        
        /**
         * 请求预处理方法
         * 在请求处理之前执行，将当前请求的URI存储到请求属性中
         *
         * @param request  HTTP请求对象
         * @param response HTTP响应对象
         * @param handler  处理器对象
         * @return 返回true表示继续执行后续处理，false表示中断请求
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // 将当前请求URI存入请求属性，便于后续使用
            request.setAttribute("currentUri", request.getRequestURI());
            return true;
        }
    }
}