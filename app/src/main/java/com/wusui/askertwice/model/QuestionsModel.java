package com.wusui.askertwice.model;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.presenter.IMainPresenter;
import com.wusui.askertwice.presenter.UpToDateFragPresenter;
import com.wusui.askertwice.ui.adapter.QuestionsAdapter;
import com.wusui.askertwice.ui.fragment.UpToDateFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fg on 2016/8/2.
 */

public class QuestionsModel {
    IMainPresenter mIMainPresenter;

    private static int page = 0;
    private static final int count = 4;
    private String token = null;
    private int state;
    private RecyclerView mRecyclerView;
    private QuestionsAdapter mAdapter;
    private  List<QuestionsBean> sQuestions = new ArrayList<>();
    private UpToDateFragPresenter mFragPresenter;


    public QuestionsModel(IMainPresenter iMainPresenter){
        this.mIMainPresenter = iMainPresenter;
    }



    public void UpQuestion() {
        if (state != 0){
            String address = "http://api.moinut.com/asker/getAllQuestions.php";
            HttpUtils.sendRequestFor(address,page,count,token, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    sQuestions = JSONObjectUtils.parseQuestion(response);
                    mIMainPresenter.loadDataSuccess(sQuestions);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    mIMainPresenter.loadDataFailure(e.toString());
                }
            });
        }
    }


    public void getQuestions(int page){
        initDatas(page);
    }
    public void initDatas(int page) {
        String address = "http://api.moinut.com/asker/getAllQuestions.php";

        HttpUtils.sendRequestFor(address,page,count,token, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
               sQuestions = JSONObjectUtils.parseQuestion(response);
                mIMainPresenter.loadDataSuccess(sQuestions);
                Log.e("UpToDateFragment",JSONObjectUtils.parseQuestion(response).toString());
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                mIMainPresenter.loadDataFailure(e.toString());
            }
        });
    }


}
