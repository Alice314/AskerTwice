package com.wusui.askertwice.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;

import java.io.DataOutputStream;

/**
 * Created by fg on 2016/7/20.
 */

public class AskerActivity extends AppCompatActivity{

    private EditText title;
    private EditText type;
    private EditText content;
    private Toolbar mToolbar;
    private static final int ASK_SUCCESS = 3;
    private static final int ASK_ERROR = -3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asker);

        initToolBar();
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
                                    out.writeBytes("");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {

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
