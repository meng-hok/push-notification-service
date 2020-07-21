package com.kosign.push.configs;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kosign.push.utils.messages.Response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;



@Component
public class InterceptorConfiguration implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       Object user = request.getSession().getAttribute("user") ;
        if(ObjectUtils.isEmpty(user)) {
            response.sendRedirect("signin");
        }
        return true;
    }

    // @Override
    // public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
     
    // }

    // @Override
    // public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        
    // }


}