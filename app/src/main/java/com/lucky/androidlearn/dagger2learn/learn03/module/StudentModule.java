package com.lucky.androidlearn.dagger2learn.learn03.module;


import com.lucky.androidlearn.dagger2learn.learn03.model.BoyStudent;
import com.lucky.androidlearn.dagger2learn.learn03.model.GrilStudent;
import com.lucky.androidlearn.dagger2learn.learn03.model.Student;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class StudentModule {

    @Provides
    List<Student> provideStudents(){
        List<Student> students = new ArrayList<>();
        students.add(new GrilStudent());
        students.add(new GrilStudent());
        students.add(new GrilStudent());
        students.add(new BoyStudent());
        students.add(new BoyStudent());
        students.add(new GrilStudent());
        return students;
    }

}
