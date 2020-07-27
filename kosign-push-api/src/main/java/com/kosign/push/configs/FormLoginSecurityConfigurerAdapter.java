package com.kosign.push.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// @EnableWebSecurity
//  @Configuration
//  @Order(1)
public class FormLoginSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception 
        {

            System.out.println("/*****************************************************************************/");

                http
                    .authorizeRequests()
                   
                    .antMatchers("/swagger-ui.html")
                    .authenticated()
//                     .and()
// //                    .httpBasic(Customizer.withDefaults());
//                     .formLogin()
//                     .permitAll()
//                     .and()
//                     .rememberMe()
                    .and()
                    .csrf()
                    .disable();
        }
    

}