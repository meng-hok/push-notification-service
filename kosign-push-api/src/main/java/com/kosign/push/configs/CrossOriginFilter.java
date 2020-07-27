package com.kosign.push.configs;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter("/*")
public class CrossOriginFilter implements Filter{

    private static final Logger log = LoggerFactory.getLogger(CrossOriginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        // Called by the web container to indicate to a filter that it is being
        // placed into service.
        // We do not want to do anything here.
    }

    /**
     |---------------------------------------------------------------------------------------- 
     |  Method Name              => 
     |  Parameters               => 
     |  Developed By             =>
     |  Created Date             =>
     |  Updated By               =>
     |  Updated Date             =>
     |----------------------------------------------------------------------------------------
     |  CORS configuration : origin , method , max-age , headers
     |
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
                
        log.info("Applying CORS filter");
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT,PATCH");
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Headers, Origin,Accept,authorization,Authorization, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        System.out.println(req.getContentType());
        System.out.println(req);
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

        // Called by the web container to indicate to a filter that it is being
        // taken out of service.
        // We do not want to do anything here.
    }
}
