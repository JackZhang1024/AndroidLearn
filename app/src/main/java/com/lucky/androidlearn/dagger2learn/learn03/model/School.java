package com.lucky.androidlearn.dagger2learn.learn03.model;

public class School {

    private Klazz mClass;

    public School(Klazz students){
        this.mClass = students;
    }

    public Klazz getKlazz() {
        return mClass;
    }
}
