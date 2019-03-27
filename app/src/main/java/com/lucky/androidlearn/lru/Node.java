package com.lucky.androidlearn.lru;

public class Node {

    public Node pre;
    public Node next;
    public String key;
    public String value;

    Node(String key, String value){
        this.key = key;
        this.value = value;
    }
}
