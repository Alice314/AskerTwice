package com.wusui.askertwice.model;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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
    private IMainPresenter mIMainPresenter;

    private  int page = 0;
    private  final int count = 4;
    private String token = null;
    private int state;
    private  List<QuestionsBean> sQuestions = new ArrayList<>();
    private  final int READ_SUCCESS = 4;
    private  final int READ_ERROR = -4;
    private  final int ASK_SUCCESS = 9;

    public QuestionsModel(IMainPresenter iMainPresenter){
        this.mIMainPresenter = iMainPresenter;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case READ_SUCCESS:
                    List<QuestionsBean> questions = (List<QuestionsBean>) msg.obj;

                    sQuestions.addAll(questions);
                    mIMainPresenter.loadDataSuccess(sQuestions);
                    break;
                case READ_ERROR:
                    Exception e = (Exception) msg.obj;
                    mIMainPresenter.loadDataFailure(e.toString());
                    break;
                case ASK_SUCCESS:
                    questions = (List<QuestionsBean>) msg.obj;
                    sQuestions.addAll(questions);
                    //   UpQuestions();
            }

        }
    };


    public void UpQuestion() {
        if (state != 0){
            String address = "http://api.moinut.com/asker/getAllQuestions.php";
            HttpUtils.sendRequestFor(address,page,count,token, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Message message = Message.obtain();
                    message.obj = JSONObjectUtils.parseQuestion(response);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();

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
                Message message = Message.obtain();
                message.what = READ_SUCCESS;
                message.obj = JSONObjectUtils.parseQuestion(response);
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Message message = Message.obtain();
                message.what = READ_ERROR;
                message.obj = e;
                mHandler.sendMessage(message);
                e.printStackTrace();
            }
        });
    }


}
