package com.wusui.askertwice.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.ui.adapter.TextAdapter;

import java.io.DataOutputStream;
import java.lang.ref.WeakReference;

import butterknife.BindView;

/**
 * Created by fg on 2016/7/20.
 */

public class UpToDateFragment extends Fragment {



    private static final int READ_SUCCESS = 4;
    private static final int READ_ERROR = -4;
    private static final int STAR_SUCCESS = 9;
    private static final int STAR_ERROR = -9;
    private static final int ANSWER_SUCCESS = 7;
    private static final int ANSWER_ERROR = -7;

    private static final int page = 0;
    private static final int count = 4;
    private String token;

    private @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private static TextAdapter mAdapter;


    private static class MyHandler extends Handler{
        private final WeakReference<UpToDateFragment>mFragment;

        public MyHandler(UpToDateFragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case READ_SUCCESS:
                    mAdapter.notifyDataSetChanged();
                    break;
                case READ_ERROR:
                    break;
            }
        }
    }
    private final MyHandler mHandler = new MyHandler(this);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uptodate,container,false);
        initDatas();
        initView();
        return view;
    }

    private void initDatas() {
        String address = "http://api.moinut.com/asker/getAllQuestions.php";
        HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
            @Override
            public void onSucceed(DataOutputStream out) {
                try {
                    out.writeBytes("page="+page+"count="+count+"token="+token);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = READ_SUCCESS;
                message.obj = JSONObjectUtils.parseQuestion(response);
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void initView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TextAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
}
