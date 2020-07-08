package com.kosign.push.configs;

import java.io.Serializable;

import com.kosign.push.apps.AppRepository;
import com.kosign.push.apps.AppService;
import com.kosign.push.notificationHistory.NotificationHistoryEntity;
import com.kosign.push.notificationHistory.NotificationHistoryRepository;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.KeyConf;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
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
    AppRepository appRepo;
    Logger logger = LoggerFactory.getLogger(SpelAddition.class);
    @Autowired
    private NotificationHistoryRepository notificationHistoryRepository;


    @Pointcut("!execution(* com.kosign.push.publics.BackendController.getHistory(..)) && !execution(* com.kosign.push.publics.BackendController.approval(..))")
    public void notToExcute(){}
    // @Pointcut("!execution(* com.kosign.push.publics.BackendController.getHistory(..))")
    // public void notToExcuteApproval(){}
    @Before("execution(* com.kosign.push.publics.BackendController.*(..))  && notToExcute() && !execution(* com.kosign.push.publics.BackendController.create(..)) && !execution(* com.kosign.push.publics.*.getYourApplication(..))  and args(appId,..) ")
    public void beforeAdvice(JoinPoint joinPoint, String appId) throws Exception {
        
        logger.info("AOP Before method:" + joinPoint.getSignature());
        System.out.println(appId);
        // System.out.println(authKey);
        try {
                UserDetail userDetail = GlobalMethod.getUserCredential();
            
                // String ownerId = appService.getOwnerIdByAppId(appId);
                String ownerId = appRepo.findUserIdByAppId(appId,KeyConf.Status.ACTIVE);
                if(userDetail.getId().equals(ownerId) ){
                    logger.info("User "+userDetail.getId()+" is valid");
                }else{
                    throw ownerId == null ? new NullPointerException(" Application not available") : new Exception("Permisson Denied");
                }
        } catch (Exception e) {
            logger.info("{Error Occur}");
            throw e;
        }
	}


    @Before("execution(* com.kosign.push.*.*.*(..)) ")
    public void beforeAllMethod (JoinPoint joinPoint){
        final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());
        logger.info("[ "+joinPoint.getSignature().getName()+" starts ]");

    }

//    @AfterReturning(value = "execution(* com.kosign.push.notifications.NotificationService.sendNotificationToFCM(..) )",
//            returning = "result")
//    public void afterFcmReturning(JoinPoint joinPoint, NotificationHistory result) {
//        logger.info( "{ Aspect "+joinPoint.getSignature().getName()+ " starts :  }" );
//        System.out.println(result);
//        NotificationHistory notificationHistory = result ;
//
//        notificationHistoryRepository.save(notificationHistory);
//
//       logger.info(" [Save Success] ");
//    }
//
//    @AfterThrowing(value = "execution(* com.kosign.push.notifications.NotificationService.sendNotificationToFCM(..) )",
//            throwing =  "exception")
//    public void afterFcmReturningError(JoinPoint joinPoint, Throwable exception) {
//        logger.info( "{ Aspect "+joinPoint.getSignature().getName()+ " Error Occur :  }" );
//        System.out.println(exception);
//
//
//        logger.info(" [Save Fail] ");
//    }
//
//    @AfterReturning(value = "execution(* com.kosign.push.notifications.NotificationService.sendNotificationToIOS(..) )",
//            returning = "result")
//    public void afterApnsReturning(JoinPoint joinPoint, Object result) {
//        logger.info("{} returned with value {}", joinPoint, result);
//        NotificationHistory notificationHistory = new NotificationHistory();
//        notificationHistory.setMessage(result.toString());
//        notificationHistoryRepository.save(notificationHistory);
//        logger.info(" [Save Success] ");
//    }



}