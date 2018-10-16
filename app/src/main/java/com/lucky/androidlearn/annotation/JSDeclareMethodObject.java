package com.lucky.androidlearn.annotation;

public class JSDeclareMethodObject extends JSObject {

    public JSDeclareMethodObject(String name) {
        super(name);
    }

    @JSMethod(name = "DeclareMethodDoSomeThings!")
    public void doSomethings(String content, boolean isTrue) {
        System.out.println("DeclareMethodDoSomeThings");
    }

    @JSMethod(name = "DeclareMethodDoOtherThings")
    public void doOtherThings(int count) {
        System.out.println("DeclareMethodDoOtherThings");
    }

}
