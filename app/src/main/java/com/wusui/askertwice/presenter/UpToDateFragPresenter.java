package com.wusui.askertwice.presenter;

import android.widget.Toast;

import com.wusui.askertwice.model.QuestionsBean;
import com.wusui.askertwice.model.QuestionsModel;
import com.wusui.askertwice.view.UpToDateFragmentView;

import java.util.List;

/**
 * Created by fg on 2016/8/2.
 */

public class UpToDateFragPresenter implements Presenter<UpToDateFragmentView>,IMainPresenter{
    private UpToDateFragmentView toDateFragmentView;
    private QuestionsModel mQuestionsModel;

    public UpToDateFragPresenter(UpToDateFragmentView view){
        attachView(view);
        mQuestionsModel = new QuestionsModel(this);
    }

    @Override
    public void attachView(UpToDateFragmentView view) {
        this.toDateFragmentView = view;
    }

    @Override
    public void detachView() {
        this.toDateFragmentView = null;
    }

    @Override
    public void loadDataSuccess(List<QuestionsBean> questionsBean) {
        toDateFragmentView.showDatas(questionsBean);
    }

    public void loadData(int page){
        mQuestionsModel.getQuestions(page);
    }
    @Override
    public void loadDataFailure(String Exception) {
    }
}
