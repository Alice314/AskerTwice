package com.wusui.askertwice.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wusui.askertwice.App;
import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.onRcvScrollListener;
import com.wusui.askertwice.model.QuestionsBean;
import com.wusui.askertwice.ui.activity.AnswersActivity;
import com.wusui.askertwice.ui.adapter.QuestionsAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fg on 2016/7/20.
 */

public class UpToDateFragment extends Fragment {



    private static final int READ_SUCCESS = 4;
    private static final int READ_ERROR = -4;
    private static final int ASK_SUCCESS = 9;
    private static final int ASK_ERROR = -9;
    private static final int ANSWER_SUCCESS = 7;
    private static final int ANSWER_ERROR = -7;

    private static int page = 0;
    private static final int count = 4;
    private String token = null;
    private int state;
    private static final String ARGUMENT = "argument";
    private static final String ARGU = "argu";


    private RecyclerView mRecyclerView;
    private static QuestionsAdapter mAdapter;
    private static List<QuestionsBean> sQuestions = new ArrayList<>();

    private  class MyHandler extends Handler{
        private final WeakReference<UpToDateFragment>mFragment;

         MyHandler(UpToDateFragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case READ_SUCCESS:
                    List<QuestionsBean> questions = (List<QuestionsBean>) msg.obj;
                    sQuestions.addAll(questions);
                    mAdapter.notifyDataSetChanged();
                    break;
                case READ_ERROR:
                    break;
                case ASK_SUCCESS:
                     questions = (List<QuestionsBean>) msg.obj;
                    sQuestions.addAll(questions);
                    mAdapter.notifyItemChanged(0);
                    UpQuestion();
            }
        }
    }
    private final MyHandler mHandler = new MyHandler(this);

    private void UpQuestion() {
        if (state != 0){
            String address = "http://api.moinut.com/asker/getAllQuestions.php";

            HttpUtils.sendRequestFor(address,page,count,token, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Log.e("UpToDateFragment",response);
                    Message message = Message.obtain();
                    message.what = ASK_SUCCESS;
                    message.arg1 = 1;
                    message.obj = JSONObjectUtils.parseQuestion(response);
                    Log.e("UpToDateFragment",JSONObjectUtils.parseQuestion(response).toString());
                    mHandler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("UpToDateFragment",e.toString());
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uptodate,container,false);
        initDatas(0);
        Toast.makeText(getContext(),"这里是最新界面",Toast.LENGTH_SHORT).show();
        mRecyclerView  = (RecyclerView) view.findViewById(R.id.question_recycler_view);
        initView();

        return view;
    }
    private void getQuestions(int page){
        initDatas(page);
    }
    private void initDatas(int page) {
        String address = "http://api.moinut.com/asker/getAllQuestions.php";

        HttpUtils.sendRequestFor(address,page,count,token, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.e("UpToDateFragment",response);
                Message message = Message.obtain();
                message.what = READ_SUCCESS;
                message.arg1 = 1;
                message.obj = JSONObjectUtils.parseQuestion(response);
                Log.e("UpToDateFragment",JSONObjectUtils.parseQuestion(response).toString());
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Log.e("UpToDateFragment",e.toString());
            }
        });
    }

    private void initView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new QuestionsAdapter(getActivity(),sQuestions);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new QuestionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), AnswersActivity.class);
                QuestionsBean questionsBean = sQuestions.get(position);
                intent.putExtra("sQuestions",questionsBean);
                startActivity(intent);
            }
        });
        mRecyclerView.addOnScrollListener(new onRcvScrollListener(){
            @Override
            public void onBottom() {
                Toast.makeText(getContext(), "滑动到底了", Toast.LENGTH_SHORT).show();
                getQuestions(++page);
            }
        });
    }
}
