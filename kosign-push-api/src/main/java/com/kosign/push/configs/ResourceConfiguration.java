package com.kosign.push.configs;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
/**
 * File Name                : 
 * File Path                : 
 * File Description         : 
 * File Author              : 
 * Created Date             :
 * Developed By             :
 * Modified Date            :
 * Modified By              :
 */
@Configuration
@EnableResourceServer
public class ResourceConfiguration extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
   
         http
            .authorizeRequests()
            .anyRequest()
            .permitAll()
            .and().csrf().disable(); 
          
    }

}