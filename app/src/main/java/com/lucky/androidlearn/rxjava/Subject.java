package com.lucky.androidlearn.rxjava;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zfz
 * Created by zfz on 2018/3/13.
 */

public abstract class Subject {

    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer){
        observers.add(observer);
    }


    public void detach(Observer observer){
        observers.remove(observer);
    }

    public void notifyStateChange(String state){
         for (Observer observer: observers){
              observer.update(state);
         }
    }


}
