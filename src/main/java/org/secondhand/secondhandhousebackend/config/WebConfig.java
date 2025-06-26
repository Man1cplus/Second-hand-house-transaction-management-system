package org.secondhand.secondhandhousebackend.config;

import org.secondhand.secondhandhousebackend.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 排除登录接口和静态资源
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/users/login",      // 登录接口
                        "/users/register",   // 注册接口
                        "/error",           // 错误页面
                        "/css/**",          // CSS静态资源
                        "/js/**",           // JS静态资源
                        "/images/**",       // 图片资源
                        "/favicon.ico"      // 网站图标
                );
    }
}