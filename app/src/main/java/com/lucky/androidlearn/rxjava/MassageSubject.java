package com.lucky.androidlearn.rxjava;

/**
 * @author zfz
 * Created by zfz on 2018/3/13.
 */

public class MassageSubject extends Subject {

    public void change(String state){
        notifyStateChange(state);
    }

}
