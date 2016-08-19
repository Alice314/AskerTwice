package com.wusui.askertwice.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wusui.askertwice.R;
import com.wusui.askertwice.callback.onRcvScrollListener;
import com.wusui.askertwice.model.QuestionsBean;
import com.wusui.askertwice.presenter.UpToDateFragPresenter;
import com.wusui.askertwice.ui.activity.AnswersActivity;
import com.wusui.askertwice.ui.adapter.QuestionsAdapter;
import com.wusui.askertwice.view.UpToDateFragmentView;

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
    private int page = 0;


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
        sQuestions.addAll(questionsBean);
        mAdapter.notifyDataSetChanged();
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
