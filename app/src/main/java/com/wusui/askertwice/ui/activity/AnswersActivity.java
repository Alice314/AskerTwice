package com.wusui.askertwice.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.callback.onRcvScrollListener;
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
    private int questionId;
    private int page;


    private static final int ANSWER_SUCCESS = 1;
    private static final int ANSWER_ERROR = -1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ANSWER_SUCCESS:
                   List<AnswerBean> answers = (List<AnswerBean>) msg.obj;
                    if (answers != null) {
                        Log.e("AnswersActivity",answers.toString());
                        sAnswers.addAll(answers);
                    }
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
        initQuestionView();
        initDatas(0);
        initAnswersView();
    }


    private void getQuestions(int page){
        initDatas(page);
    }
    private void initDatas(int page) {
        String address = " http://api.moinut.com/asker/getAnswers.php";
         int count = 2;

        HttpUtils.sendRequestFor(address, page, count, questionId, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = ANSWER_SUCCESS;
                message.obj = JSONObjectUtils.pareseAnswer(response);
                Log.e("AnswerActivity", response);
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
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
        recyclerview.addOnScrollListener(new onRcvScrollListener(){
            @Override
            public void onBottom() {
                Toast.makeText(AnswersActivity.this, "滑动到底了", Toast.LENGTH_SHORT).show();
                getQuestions(++page);
            }
        });
    }
}
