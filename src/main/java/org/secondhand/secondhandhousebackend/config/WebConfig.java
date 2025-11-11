package org.secondhand.secondhandhousebackend.config;

import org.secondhand.secondhandhousebackend.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 所有路径都经过拦截器，由拦截器内部判断是否需要JWT验证
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**");
    }
}