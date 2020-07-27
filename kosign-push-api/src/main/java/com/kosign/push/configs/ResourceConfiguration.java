package com.kosign.push.configs;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
// @Order(3)
@Configuration
@EnableResourceServer
public class ResourceConfiguration extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
   
         http.cors().and()
            .authorizeRequests()
            .anyRequest()
            .permitAll()
            // .and()
            // .authorizeRequests()
            // .antMatchers("/swagger-ui.html")
            // .authenticated()
            .and().csrf().disable(); 
          
    }

}