package com.kosign.push.configs;

import com.kosign.push.utils.FileStorageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebMvc
@Configuration
public class EnableInterceptorConfigure implements WebMvcConfigurer{
    
    @Autowired
    private InterceptorConfiguration interceptorConfiguration;

   
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
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorConfiguration).addPathPatterns("/swagger-ui.html");
    }
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //             .allowedOrigins("*")
    //             .allowedMethods("POST, GET, OPTIONS, DELETE")
    //             .maxAge(0)
    //             .allowedHeaders("Access-Control-Allow-Methods","Access-Control-Allow-Origin","Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept,authorization,Authorization, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
    // }
}