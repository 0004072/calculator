package com.hsenid.calculator;

import java.util.*;

/**
 * Created by hsenid on 12/2/16.
 */
public class OpStack {
    private String name;
    private int top;
    private List<String> stackList;

    public OpStack(String name) {
        this.name = name;
        this.top = -1;
        this.stackList = new ArrayList<>();
    }

    public void push(String str){
        stackList.add(++top, str);
    }

    public String pop(){
        return stackList.remove(top--);
    }

    public String peek(){
        return stackList.get(top);
    }

    public boolean isEmpty(){
        return (top == -1);
    }

    public void flush(){
        top = -1;
        this.stackList.clear();
    }

    public void consoleLog(){
        System.out.println(this.stackList.toString());
    }
}
