package com.kosign.push.configs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 *
 * For validate request parameter if exist
 * */
// @Aspect
// @Component
public class ValidationAspect {

    // @Pointcut("execution(* com.kosign.push.publics.PublicController.saveFcmTest (..)) ")
    // public void customPointCut() {}

    // @Around("customPointCut()")
    // public void validateApplicationPlatformSettingNewSaving (JoinPoint joinPoint){
    //     final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());
    //     logger.info("[ "+joinPoint.getSignature().getName()+" starts ]");

    // }
}
