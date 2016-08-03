package com.wusui.askertwice.view;

import com.wusui.askertwice.model.QuestionsBean;

import java.util.List;

/**
 * Created by fg on 2016/8/2.
 */

public interface UpToDateFragmentView {
    void showDatas(List<QuestionsBean> questionsBean);

    void getQuestions(int page);
}
