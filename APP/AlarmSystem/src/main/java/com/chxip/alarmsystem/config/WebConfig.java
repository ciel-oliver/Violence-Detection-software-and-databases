package com.chxip.alarmsystem.config;

import com.chxip.alarmsystem.interceptor.FilterConfig;
import com.chxip.alarmsystem.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 启用跨域配置
 * 编写SpringMVCConfig类使用FilterConfig中的配置
 * @author Administrator
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {


    private final FilterConfig filterConfig;

    private final LoginInterceptor loginInterceptor;

    public WebConfig(FilterConfig filterConfig,LoginInterceptor loginInterceptor) {
        this.filterConfig = filterConfig;
        this.loginInterceptor = loginInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(filterConfig).addPathPatterns("/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(
                //添加不拦截路径
                "/**/login/**","/user/login",           //登录
                "/api/**",            //html静态资源
                "/css/**",
                "/images/**",
                "/js/**",
                "/lib/**",
                "/static/**",
                "/favicon.ico",
                "/files/**",
                "/**/api/**"
        );

    }

    //配置支持跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {

    }
}
