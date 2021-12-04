package com.product.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.concurrent.TimeUnit;

@Order(0)
@Aspect
@Configuration
@Slf4j
public class LogAspect {


    @Around(value = "com.product.aop.ApplicationPointcuts.combinedPointcut()")
    public Object calculateMethodTimeAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodTiming(joinPoint);
    }

    @SneakyThrows
    private Object logMethodTiming(ProceedingJoinPoint joinPoint) {
        final Logger classLogger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        if (!classLogger.isDebugEnabled()) {
            return joinPoint.proceed();
        }

        String className = ((MethodSignature) joinPoint.getSignature()).getMethod()
                .getDeclaringClass()
                .getSimpleName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        long startTimer = System.nanoTime();
        Object result = joinPoint.proceed();
        long elapsedTime = System.nanoTime() - startTimer;
        try {
            classLogger.debug("LogAspect : {}", LogMessage.builder()
                    .className(className)
                    .methodName(methodName)
                    .elapsedTimeInMillis(TimeUnit.NANOSECONDS.toMillis(elapsedTime))
                    .elapsedTimeInMicros(TimeUnit.NANOSECONDS.toMicros(elapsedTime))
                    .result(result)
                    .build());
        } catch (Exception ex) {
            log.error("Unexpected error occurred", ex);
        }
        return result;
    }
}
