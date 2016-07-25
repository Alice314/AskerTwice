package com.wusui.askertwice.ui.fragment;

import android.content.Intent;
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
import android.widget.Toast;

import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.model.QuestionsBean;
import com.wusui.askertwice.ui.activity.AskerActivity;
import com.wusui.askertwice.ui.adapter.QuestionsAdapter;

import java.io.DataOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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
    public static final String ARGUMENT = "argument";


    private RecyclerView mRecyclerView;
    private static QuestionsAdapter mAdapter;
    private static List<QuestionsBean> sQuestions = new ArrayList<>();

    private static class MyHandler extends Handler{
        private final WeakReference<UpToDateFragment>mFragment;

        public MyHandler(UpToDateFragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case READ_SUCCESS:
                    int position = msg.arg1;
                    if (position != 1){
                        sQuestions.add(null);
                        return;
                    }
                    QuestionsBean questions = (QuestionsBean) msg.obj;
                    sQuestions.set(position,questions);
                    mAdapter.notifyDataSetChanged();
                    break;
                case READ_ERROR:
                    break;
            }
        }
    }
    private final MyHandler mHandler = new MyHandler(this);


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            token = bundle.getString(ARGUMENT);
    }
    public static UpToDateFragment newInstance(String argument)
    {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        UpToDateFragment contentFragment = new UpToDateFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uptodate,container,false);
        initDatas();
        Toast.makeText(getContext(),"这里是最新界面",Toast.LENGTH_SHORT).show();
        mRecyclerView  = (RecyclerView) view.findViewById(R.id.recycler_view);
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
                message.arg1 = 1;
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
        mAdapter = new QuestionsAdapter(getActivity(),sQuestions);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new QuestionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), AskerActivity.class);
               // intent.putExtra("sQuestions",sQuestions);
                startActivity(intent);
            }
        });
    }
}
