package com.lucky.androidlearn.dagger2learn.learn01.qualifer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface LilyFlower {

}
