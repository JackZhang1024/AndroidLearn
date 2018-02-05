package com.lucky.androidlearn.dagger2learn.learn03;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lucky.androidlearn.dagger2learn.learn03.component.DaggerDagger2Main3StudentComponent;
import com.lucky.androidlearn.dagger2learn.learn03.model.Klazz;
import com.lucky.androidlearn.dagger2learn.learn03.model.School;
import com.lucky.androidlearn.dagger2learn.learn03.model.Student;
import com.lucky.androidlearn.dagger2learn.learn03.module.ClassModule;
import com.lucky.androidlearn.dagger2learn.learn03.module.SchoolModule;

import java.util.List;

import javax.inject.Inject;


/**
 * @author zfz
 */
public class Dagger2Main3Activity extends AppCompatActivity {
    private static final String TAG = "Dagger2Main3Activity";
    @Inject
    School mSchool;

    @Inject
    Klazz mKlazz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerDagger2Main3StudentComponent.create()
                .plus(new ClassModule())
                .plus(new SchoolModule())
                .plus()
                .inject(this);
        List<Student> students = mSchool.getKlazz().getStudents();
        for (Student student: students){
            Log.e(TAG, "onCreate: "+student.showGender());
        }

        List<Student> klazzStudents = mKlazz.getStudents();
        for (Student student: students){
            Log.e(TAG, "onCreate: klazz Student "+student.showGender());
        }
    }

}
