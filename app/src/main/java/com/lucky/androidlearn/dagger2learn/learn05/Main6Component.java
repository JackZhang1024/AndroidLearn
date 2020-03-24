package com.lucky.androidlearn.dagger2learn.learn05;

import com.lucky.androidlearn.dagger2learn.ActivityScope;

import dagger.Component;

// 如果Main6Module中有被@ActivityScope标注的方法 则在@Component中必须也要有同样的标记 否则会报错
@ActivityScope
@Component(modules = {Main6Module.class})
public interface Main6Component {

    void inject(Dagger2Main6Activity main6Activity);

}
