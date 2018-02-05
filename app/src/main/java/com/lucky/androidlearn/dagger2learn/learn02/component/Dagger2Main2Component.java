package com.lucky.androidlearn.dagger2learn.learn02.component;

import com.lucky.androidlearn.dagger2learn.learn02.Dagger2Main2Activity;
import com.lucky.androidlearn.dagger2learn.learn02.component.PotComponent;

import dagger.Component;

/**
 * 子组件的依赖
 * @author zfz
 */

@Component(dependencies =PotComponent.class)
public interface Dagger2Main2Component {

    void inject(Dagger2Main2Activity secondActivity);

}
