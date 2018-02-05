package com.lucky.androidlearn.dagger2learn;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by zfz on 2017/2/9.
 */
/**
 * 自定义作用域
 * */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {

}
