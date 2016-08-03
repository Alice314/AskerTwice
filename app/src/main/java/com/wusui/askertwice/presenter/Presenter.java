package com.wusui.askertwice.presenter;

/**
 * Created by fg on 2016/8/2.
 */

public interface Presenter<V> {
    void attachView(V view);
    void detachView();
}
