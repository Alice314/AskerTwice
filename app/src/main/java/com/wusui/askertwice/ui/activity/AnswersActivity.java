package com.wusui.askertwice.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.model.AnswerBean;
import com.wusui.askertwice.model.QuestionsBean;
import com.wusui.askertwice.ui.adapter.AnswersAdapter;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fg on 2016/7/24.
 */

public class AnswersActivity extends BaseActivity {

    private RecyclerView recyclerview;
    private static AnswersAdapter adapter;
    private static List<AnswerBean>sAnswers = new ArrayList<>();
    private String questionId;
    private int page = 0;
    private int count =2;

    private static final int ANSWER_SUCCESS = 1;
    private static final int ANSWER_ERROR = -1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ANSWER_SUCCESS:
                   List<AnswerBean> answers = (List<AnswerBean>) msg.obj;
                    sAnswers.addAll(answers);
                    adapter.notifyDataSetChanged();
                    break;

                case ANSWER_ERROR:
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        initDatas();
        initView() ;

    }

    private void initDatas() {
        String address = " http://api.moinut.com/asker/getAnswers.php";
        HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
            @Override
            public void onSucceed(DataOutputStream out) {
                try {
                    out.writeBytes("questionId=" + questionId + "&page=" + page + "&count=" + count);
                    Log.e("AnswerActivity",out.toString());
                    Log.e("AnswerActivity",questionId);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = ANSWER_SUCCESS;
                message.obj = JSONObjectUtils.pareseAnswer(response);
                Log.e("AnswerActivity",response.toString());
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initView() {
        initQuestionView();
        initAnswersView();
    }

    private void initQuestionView() {
        QuestionsBean questionsBean = (QuestionsBean) getIntent().getSerializableExtra("sQuestions");
        TextView type = (TextView) findViewById(R.id.q_tv_type);
        TextView title = (TextView) findViewById(R.id.q_tv_title);
        TextView time = (TextView) findViewById(R.id.q_tv_date);
        TextView name = (TextView)findViewById(R.id.q_tv_author);
        TextView text = (TextView)findViewById(R.id.q_tv_content);
        TextView comment = (TextView) findViewById(R.id.q_tv_answer_count);
        TextView star = (TextView) findViewById(R.id.q_tv_star_count);

        type.setText(questionsBean.getType());
        title.setText(questionsBean.getTitle());
        time.setText(questionsBean.getDate());
        name.setText(questionsBean.getAuthorName());
        text.setText(questionsBean.getContent());
        comment.setText(questionsBean.getAnswerCount());
        star.setText(questionsBean.getStarCount());
        questionId = questionsBean.getId();
    }

    private void initAnswersView() {
        recyclerview = (RecyclerView) findViewById(R.id.ans_recycler_view);
        recyclerview.setLayoutManager(new LinearLayoutManager(AnswersActivity.this));
        adapter = new AnswersAdapter(AnswersActivity.this,sAnswers);
        recyclerview.setAdapter(adapter);
    }
}
