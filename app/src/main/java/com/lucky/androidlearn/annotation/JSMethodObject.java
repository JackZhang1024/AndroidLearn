package com.lucky.androidlearn.annotation;

public class JSMethodObject extends JSObject {

    public JSMethodObject(String name) {
        super(name);
    }

    @JSMethod(name = "helloWorld!")
    public void doSomethings(String content, boolean isTrue) {
        System.out.println("doSomethings");
    }

    @JSMethod(name = "otherThins!")
    public void doOtherThings(int count) {
        System.out.println("doOtherThings");
    }

}
