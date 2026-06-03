package com.insightforge.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * 国际化配置类
 * 用于配置多语言支持，包括消息源、区域解析器和语言切换拦截器
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * 配置消息源
     * 用于加载国际化消息资源文件
     *
     * @return 配置好的消息源对象
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // 设置消息资源文件的基础名称（messages_zh_CN.properties, messages_en.properties等）
        messageSource.setBasename("messages");
        // 设置默认编码为UTF-8，确保中文等字符正确显示
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * 配置区域解析器
     * 使用Cookie存储用户的语言偏好设置
     *
     * @return 配置好的区域解析器对象
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        // 设置默认语言为简体中文
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        // 设置Cookie名称为"lang"
        resolver.setCookieName("lang");
        // 设置Cookie有效期为7天（604800秒）
        resolver.setCookieMaxAge(604800);
        return resolver;
    }

    /**
     * 配置语言切换拦截器
     * 允许通过URL参数切换语言
     *
     * @return 配置好的语言切换拦截器对象
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // 设置URL参数名为"lang"，如 ?lang=en 可切换到英文
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * 注册拦截器
     * 将语言切换拦截器添加到拦截器链中
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册语言切换拦截器，监听URL参数变化
        registry.addInterceptor(localeChangeInterceptor());
    }
}