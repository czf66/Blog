package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 描述:
 * 作者： xq
 * 日期： 2020/5/8 13:28
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //加自定义拦截器AuthInterceptor，设置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/api/**")
                //排除哪几个页面不需要拦截
                .excludePathPatterns("/api/user/login", "/api/user/register","/api/user/reset");
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
}
