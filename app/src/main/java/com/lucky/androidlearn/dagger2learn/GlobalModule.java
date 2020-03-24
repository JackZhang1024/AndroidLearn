package com.lucky.androidlearn.dagger2learn;


import dagger.Module;
import dagger.Provides;

@Module
public class GlobalModule {


    @Provides
    GlobalData provideGlobalData() {
        return new GlobalData("lisi");
    }

    class GlobalData {

        private String name;

        GlobalData(String name) {
            this.name = name;
        }

        public void showName() {
            System.out.println("name " + this.name);
        }
    }


}
