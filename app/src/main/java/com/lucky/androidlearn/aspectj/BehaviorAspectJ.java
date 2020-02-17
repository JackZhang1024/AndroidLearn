package com.lucky.androidlearn.aspectj;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
public class BehaviorAspectJ {

    private static final String TAG = "BehaviorAspectJ";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 注解 注意 后面是 * *（..） 格式务必是这样的 否则就有问题了
    // 切出那些蛋糕
    @Pointcut("execution(@com.lucky.androidlearn.aspectj.BehaviorTrace * *(..))")
    public void annoBehavior(){

    }


    // 怎么吃蛋糕
    @Around("annoBehavior()")
    public Object dealPoint(ProceedingJoinPoint point) throws Throwable{
        // 1. 获取到注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        BehaviorTrace behaviorTrace = method.getAnnotation(BehaviorTrace.class);
        // 2. 获取到注解的值
        String value = behaviorTrace.value();
        Log.d(TAG, "dealPoint: value "+value+" date "+simpleDateFormat.format(new Date()));
        long beginTime = System.currentTimeMillis();
        // 3. 调用被注解的方法
        Object object = point.proceed();
        // 4. 执行切面编程加入的方法
        Log.d(TAG, "dealPoint: costTime "+(System.currentTimeMillis() - beginTime));
        return object;
    }





}
