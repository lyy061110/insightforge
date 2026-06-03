package com.insightforge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * InsightForge应用程序主入口类
 * Spring Boot应用程序的启动类，负责初始化和启动整个应用
 */
@SpringBootApplication
public class InsightforgeApplication {

    /**
     * 应用程序主入口方法
     * 启动Spring Boot应用程序
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动Spring Boot应用，传入主类和命令行参数
        SpringApplication.run(InsightforgeApplication.class, args);
    }
}