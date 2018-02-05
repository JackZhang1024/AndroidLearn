package com.lucky.androidlearn.dagger2learn.learn00.model;

import javax.inject.Inject;

public class Rose {

    @Inject
    public Rose(){

    }


    public String whisper(){
        return "热恋";
    }

}
