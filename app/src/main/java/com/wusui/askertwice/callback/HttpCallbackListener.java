package com.wusui.askertwice.callback;

/**
 * Created by fg on 2016/7/21.
 * 你看，这个不报错也
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
