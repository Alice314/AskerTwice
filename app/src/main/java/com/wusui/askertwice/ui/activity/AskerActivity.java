package com.wusui.askertwice.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;

import java.io.DataOutputStream;

/**
 * Created by fg on 2016/7/20.
 */

public class AskerActivity extends BaseActivity{

    private String token;
    private EditText title;
    private EditText type;
    private EditText content;
    private Toolbar mToolbar;
    private static final int ASK_SUCCESS = 3;
    private static final int ASK_ERROR = -3;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ASK_SUCCESS:
                    if (msg.arg1 == 200){
                        Toast.makeText(AskerActivity.this,"提问成功",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ASK_ERROR:
                    break;

            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asker);

        initToolBar();
        token = getIntent().getStringExtra("ask_token");
        title = (EditText) findViewById(R.id.ask_title);
        type = (EditText) findViewById(R.id.ask_type);
        content = (EditText) findViewById(R.id.ask_content);
    }


    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Asker");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_ask:
                        String address = " http://api.moinut.com/asker/askQuestion.php";
                        HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
                            @Override
                            public void onSucceed(DataOutputStream out) {
                                try {
                                    out.writeBytes("token="+token+"&title="+title.getText().toString()+
                                    "&content="+content.getText().toString()+"&type="+type);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                Message message = Message.obtain();
                                message.what = ASK_SUCCESS;
                                message.arg1 = JSONObjectUtils.pareseString(response);
                                mHandler.sendMessage(message);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                }
                return true;
            }
        });

    }
}
