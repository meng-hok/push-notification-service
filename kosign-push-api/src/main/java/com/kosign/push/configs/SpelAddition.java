package com.kosign.push.configs;

import com.kosign.push.apps.AppRepository;
import com.kosign.push.notificationHistory.NotificationHistoryRepository;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.aspects.ApplicationAspect;
import com.kosign.push.utils.enums.ExceptionEnum;
import com.kosign.push.utils.enums.KeyConfEnum;
import com.kosign.push.utils.enums.ResponseEnum;
import com.kosign.push.utils.messages.Response;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class SpelAddition  {
    
    @Autowired
    AppRepository appRepo;
    Logger logger = LoggerFactory.getLogger(SpelAddition.class);
    @Autowired
    private NotificationHistoryRepository notificationHistoryRepository;
  
    @Before("execution(* com.kosign.push.*.*.*(..)) ")
    public void beforeAllMethod (JoinPoint joinPoint){
        final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());
        logger.info("[ "+joinPoint.getSignature().getName()+" starts ]");

    }

    // @Pointcut("!execution(* com.kosign.push.*.*.getHistory(..)) && !execution(* com.kosign.push.*.*.approval(..)) && !execution(* com.kosign.push.*.*.create(..)) && !execution(* com.kosign.push.*.*.getYourApplication(..)) ")
    // public void notToExecute(){}
   
    // @Pointcut("execution(* com.kosign.push.platformSetting.*.*(..))  ")
    // public void toExecute(){}

    // @Around(" toExecute() && notToExecute()  ")
    // public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        
    //     logger.info("AOP Before method:" + joinPoint.getSignature());
 
      
    //     try {
    //             Object object = (joinPoint.getArgs().length > 0) ? joinPoint.getArgs()[0] : null;
    //             if ( object instanceof ApplicationAspect ) { 

    //                 ApplicationAspect aspect = (ApplicationAspect) object;
                   
    //                 validateOwner(aspect.appId);
                  
    //             }

    //             return joinPoint.proceed();
    //     } catch (Exception e) {
    //         logger.info("{Error Occur}");
    //         return Response.getFailResponseNonDataBody(e.getLocalizedMessage().toUpperCase());
    //     }
	// }
    
    public void validateOwner(String appId) throws Exception { 
        UserDetail userDetail = GlobalMethod.getUserCredential();
        String ownerId = appRepo.findUserIdByAppId(appId, KeyConfEnum.Status.ACTIVE);
                    
        if(userDetail.getId().equals(ownerId) ){
            logger.info("User "+userDetail.getId()+" is valid");
      
        }else{
            throw ownerId == null ? new NullPointerException(ExceptionEnum.Message.APPLICATION_NOT_AVAIBLE) : new Exception(ExceptionEnum.Message.PERMISSION_DENIED);
        }   
    }
    

    @Pointcut("@annotation(com.kosign.push.configs.aspectAnnotation.AspectObjectApplicationID) ")
    public void annotatedMethod(){}
   
    @Pointcut("@within(com.kosign.push.configs.aspectAnnotation.AspectObjectApplicationID) ")
    public void annotatedClass(){}
    
    @Around("annotatedMethod() || annotatedClass()")
    public Object aroundApplicationObject(ProceedingJoinPoint joinPoint) throws Throwable {
        
        logger.info("AOP Before method:" + joinPoint.getSignature());
 
      
        try {
                Object object = (joinPoint.getArgs().length > 0) ? joinPoint.getArgs()[0] : null;
                if ( object instanceof ApplicationAspect ) { 

                    ApplicationAspect aspect = (ApplicationAspect) object;
                   
                    validateOwner(aspect.getApp_id());
                  
                }

                return joinPoint.proceed();
        } catch (Exception e) {
            logger.info("{Error Occur}");
            return Response.getFailResponseNonDataBody(e.getLocalizedMessage().toUpperCase());
        }
	}

    @Around("@annotation(com.kosign.push.configs.aspectAnnotation.AspectStringApplicationID)")
    public Object aroundApplicationAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        
        logger.info("AOP Before method:" + joinPoint.getSignature());
 
      
        try {
                Object str = (joinPoint.getArgs().length > 0) ? joinPoint.getArgs()[0] : null;
                if ( str instanceof String ) { 

                    String appId = (String) str;
                   
                    validateOwner(appId);
                  
                }

                return joinPoint.proceed();
        } catch (Exception e) {
            logger.info("{Error Occur}");
            return Response.getFailResponseNonDataBody(e.getLocalizedMessage().toUpperCase());
        }
	}



}