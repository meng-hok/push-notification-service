package com.kosign.push.configs;

import com.kosign.push.utils.FileStorageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class EnableInterceptorConfigure implements WebMvcConfigurer{
    
    // @Autowired
    // private InterceptorConfiguration interceptorConfiguration;

    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(interceptorConfiguration).addPathPatterns("/api/v1/**");
    // }
    @Value("${base.file.client}")
    public String clientPath;
    @Value("${base.file.server}")
    public String serverPath ;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       
        System.out.println("File Configuration Working ******");
        System.out.println(clientPath);
        System.out.println(serverPath);
        registry.addResourceHandler(clientPath+"/**").addResourceLocations("file:"+serverPath+"/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
}