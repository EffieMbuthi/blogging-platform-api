package com.BlogApp2.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Aspect // only describe it as--> contains cross-cutting advice. #Not a bean
@Component
@Slf4j
public class LoggingAspect {
    // first * = matches return types of the methods.
    //second * = all files in the impl folder.
    //third *= matches any method name in those classes
    // (..)= matches any parameters in those methods.
    //execution = matches a method actually being called/executed
    @Pointcut("execution(* com.BlogApp2.service.impl.*.*(..))")
    public void serviceLayerMethods(){
    }

    @Before("serviceLayerMethods()")
    public void logBefore(JoinPoint joinPoint){ //JoinPoint is just a data-holder object built for each matched call
        //jointpoint= small information describing "the method call currently in progress.
                 //Which method is being called (its name, its declaring class, its parameter types)
                 //What actual argument values were passed in for this specific call
                //What object the method is being called on
        String methodName= joinPoint.getSignature().getName(); //method's signature = its name + parameter types
        log.info("Method called: {}", methodName);
    }

    // @After is "after (finally)" advice: it runs whether the method returns normally or
    // throws. That's a genuinely different guarantee than @Around's timing log below, which
    // only fires on the success path, so it's logged at debug level as a distinct, lower-noise
    // signal that a call reached completion at all, success or failure, not a duplicate of it.
    @After("serviceLayerMethods()")
    public void logAfter(JoinPoint joinPoint){
        String methodName= joinPoint.getSignature().getName();
        log.debug("Method reached completion (normally or via exception): {}", methodName);
    }

    // createPost returns PostDetailDto; deleteUser might return void....etc
    // Since one single @Around method has to work for all of them, its return type has to be something that can represent any possible return type

    //throws Throwable --> the real method being wrapped might throw any kind of exception;
    @Around("serviceLayerMethods()")
    public Object logAround (ProceedingJoinPoint joinPoint) throws Throwable{
        String methodName= joinPoint.getSignature().getName();
        long startTime= System.currentTimeMillis();
        try {
            return joinPoint.proceed(); //causes the real method to run/ whatever matches the pointcut
        } finally {
            // finally guarantees the duration is still logged when the method throws,
            // e.g. every *NotFoundException path, which proceed() alone would otherwise skip
            long duration= System.currentTimeMillis()- startTime;
            log.info("Method {} executed in {} ms", methodName, duration);
        }
    }
}
