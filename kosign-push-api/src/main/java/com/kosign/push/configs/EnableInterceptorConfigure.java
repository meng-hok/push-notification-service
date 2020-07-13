package com.kosign.push.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @EnableWebMvc
// @Configuration
public class EnableInterceptorConfigure implements WebMvcConfigurer{
    
    @Autowired
    private InterceptorConfiguration interceptorConfiguration;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorConfiguration).addPathPatterns("/api/v1/**");
    }

    
}