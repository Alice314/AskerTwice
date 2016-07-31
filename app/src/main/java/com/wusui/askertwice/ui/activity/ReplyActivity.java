package com.wusui.askertwice.ui.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wusui.askertwice.App;
import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;

import java.io.DataOutputStream;

/**
 * Created by fg on 2016/7/30.
 */

public class ReplyActivity extends BaseActivity {

    private String token;
    private int questionId;
    private EditText content;

    private  final int REPLY_SUCCESS = 7;
    private  final int REPLY_ERROR = -7;
    private  final int REPLY_CANCLE = -8;

    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REPLY_SUCCESS:
                    if (msg.arg1 == 200){
                        Toast.makeText(App.getContext(),"回复成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("reply_state",msg.arg1);
                        setResult(RESULT_FIRST_USER,intent);
                        finish();
                    }

            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("回答");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite,null));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        content = (EditText) findViewById(R.id.replay);



        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_reply){
                    questionId = getIntent().getIntExtra("questionId",questionId);
                    Log.e("ReplyActivity",questionId + "");
                    String address = " http://api.moinut.com/asker/answer.php";
                    HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
                        @Override
                        public void onSucceed(DataOutputStream out) {
                            try {
                                token = App.getUser(ReplyActivity.this).getToken();
                                out.writeBytes("token=" + token + "&questionId=" + questionId +"&content=" + content.getText().toString());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                                Message message = Message.obtain();
                                message.what = REPLY_SUCCESS;
                            message.arg1 = JSONObjectUtils.pareseString(response);
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reply,menu);
        return true;
    }
}
