package com.kosign.push.configs;

import java.io.Serializable;

import com.kosign.push.apps.AppService;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.GlobalMethod;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class SpelAddition  {
    
    @Autowired
    AppService appService;
    Logger logger = LoggerFactory.getLogger(SpelAddition.class);
    
    // @Pointcut("execution(* com.kosign.push.publics.PublicRestController.*(..)) and args(appId,..) && !execution(com.kosign.push.publics.PublicRestController.create(..)) ")
    // @Pointcut("@args(* com.kosign.push.publics.PublicRestController.*(..)) && !execution(* com.kosign.push.publics.PublicRestController.create(..))  and args(appId,..) ")
    // private void selectAll(){}
    
    @Before("execution(* com.kosign.push.publics.PublicRestController.*(..)) && !execution(* com.kosign.push.publics.PublicRestController.create(..))  and args(appId,..) ")
    public void beforeAdvice(JoinPoint joinPoint, String appId) throws Exception {
        
        logger.info("AOP Before method:" + joinPoint.getSignature());
        System.out.println(appId);
        // System.out.println(authKey);
        try {
                UserDetail userDetail = GlobalMethod.getUserCredential();
            
            
                if(userDetail.getId().equals(appService.getOwnerIdByAppId(appId)) ){
                    logger.info("User "+userDetail.getId()+" is valid");
                }else{
                    throw new Exception("Permisson Denied");
                }
        } catch (Exception e) {
            logger.info("{Error Occur}");
            throw e;
        }
	}
   

    // public boolean isOwner(String userId,String appId){
         
    //     if(userId.equals(appService.getOwnerIdByAppId(appId))){
    //         return true;
    //      }else{
    //         return false;
    //      }
            
            
    // }
}