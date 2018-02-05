package com.lucky.androidlearn.dagger2learn.learn03.module;


import com.lucky.androidlearn.dagger2learn.learn03.model.Klazz;
import com.lucky.androidlearn.dagger2learn.learn03.model.Student;

import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class ClassModule {

    @Provides
    Klazz provideClass(List<Student> students) {
        return new Klazz(students);
    }

}
