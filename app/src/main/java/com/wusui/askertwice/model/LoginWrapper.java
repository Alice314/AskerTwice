package com.wusui.askertwice.model;

/**
 * Created by fg on 2016/7/23.
 */

public class LoginWrapper<T> {


    private int state;
    private String info;
    private T data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
