package com.lucky.androidlearn.dagger2learn.learn03.model;

import java.util.List;

public class Klazz {

    List<Student> mStudents;

    public Klazz(List<Student> students){
        this.mStudents = students;
    }

    public List<Student> getStudents() {
        return mStudents;
    }
}
