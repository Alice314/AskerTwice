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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wusui.askertwice.App;
import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.onRcvScrollListener;
import com.wusui.askertwice.model.QuestionsBean;
import com.wusui.askertwice.presenter.UpToDateFragPresenter;
import com.wusui.askertwice.ui.activity.AnswersActivity;
import com.wusui.askertwice.ui.adapter.QuestionsAdapter;
import com.wusui.askertwice.view.UpToDateFragmentView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fg on 2016/7/20.
 */

public class UpToDateFragment extends Fragment implements UpToDateFragmentView {

    private RecyclerView mRecyclerView;
    private  QuestionsAdapter mAdapter;
    private  List<QuestionsBean> sQuestions = new ArrayList<>();
    private UpToDateFragPresenter mFragPresenter;


    private static final int READ_SUCCESS = 4;
    private static final int READ_ERROR = -4;
    private static final int ASK_SUCCESS = 9;
    private int page = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
                 //   UpQuestions();
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uptodate,container,false);
        mFragPresenter = new UpToDateFragPresenter(this);
        
        mFragPresenter.loadData(page);
        initView(view);
        return view;
    }


    @Override
    public void showDatas(List<QuestionsBean> questionsBean) {
        Message message = Message.obtain();
        message.what = READ_SUCCESS;
        message.obj = questionsBean;
        mHandler.sendMessage(message);
    }


    @Override
    public void getQuestions(int page) {
        mFragPresenter.loadData(page);
    }


    @Override
    public void onDestroy() {
        mFragPresenter.detachView();
        super.onDestroy();
    }

    private void initView(View view) {
        mRecyclerView  = (RecyclerView) view.findViewById(R.id.question_recycler_view);
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
