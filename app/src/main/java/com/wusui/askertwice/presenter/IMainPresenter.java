package com.wusui.askertwice.presenter;

import com.wusui.askertwice.model.QuestionsBean;

import java.util.List;

/**
 * Created by fg on 2016/8/2.
 */

public interface IMainPresenter {
    void loadDataSuccess(List<QuestionsBean> questionsBean);
    void loadDataFailure(String exception);
}
