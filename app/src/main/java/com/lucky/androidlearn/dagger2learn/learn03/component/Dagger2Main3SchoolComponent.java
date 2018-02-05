package com.lucky.androidlearn.dagger2learn.learn03.component;

import com.lucky.androidlearn.dagger2learn.learn03.module.SchoolModule;

import dagger.Component;
import dagger.Subcomponent;

/**
 *
 * 学校
 * 班级
 * 学生
 * @author zfz
 * */

@Subcomponent(modules = SchoolModule.class)
public interface Dagger2Main3SchoolComponent {

    Dagger2Main3ActivityComponent plus();

}
