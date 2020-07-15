package com.lucky.androidlearn.proxy;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicProxyNewActivity extends AppCompatActivity {

    private static final String TAG = "DynamicProxyNewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_new);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_dynamic)
    public void onDynamicProxyClick() {
        Student student = new Student();
        IStudy study = (IStudy) Proxy.newProxyInstance(student.getClass().getClassLoader(), new Class[]{IStudy.class}, new StudentHandler(student));
        // 用IStudy这个代理对象
        study.study();
    }

    interface IStudy {
        void study();
    }

    static class Student implements IStudy {

        @Override
        public void study() {
            Log.e(TAG, "study: 我要好好学习，天天向上");
        }

    }

    static class StudentHandler implements InvocationHandler {
        private Student student;

        public StudentHandler(Student student) {
            this.student = student;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            preStudy();
            Log.e(TAG, "invoke: methodName " + method.getName());
            Object result = method.invoke(student, args);
            postStudy();
            return result;
        }

        private void preStudy(){
            Log.e(TAG, "preStudy: 我要好好看书学习");
        }

        private void postStudy(){
            Log.e(TAG, "postStudy: 好好学习，迎娶白富美");
        }
    }


}
