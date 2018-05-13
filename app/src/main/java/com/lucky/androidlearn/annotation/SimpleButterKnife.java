package com.lucky.androidlearn.annotation;

import android.app.Activity;
import android.view.View;

import java.io.InvalidObjectException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * SimpleButterKnife类利用bind方法只需要加载注解处理器自动生成的类并执行
 * 它的构造器就可以了
 */
public final class SimpleButterKnife {

    public static void bind(Activity target) {
        View sourceView = target.getWindow().getDecorView();
        Class<?> targetClass = target.getClass();
        String targetClassName = targetClass.getName();
        Constructor constructor;
        try {
            Class<?> bindingClass = targetClass.getClassLoader()
                    .loadClass(targetClassName + "_ViewBinding");
            constructor = bindingClass.getConstructor(targetClass, View.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Not found. Should try search its superclass of " + targetClassName, e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + targetClassName, e);
        }
        try {
            constructor.newInstance(target, sourceView);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create binding instance " + cause);
        }
    }
}
