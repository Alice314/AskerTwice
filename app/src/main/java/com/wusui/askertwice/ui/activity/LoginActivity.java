package com.wusui.askertwice.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.model.UserBean;

import java.io.DataOutputStream;

import butterknife.BindView;

/**
 * Created by fg on 2016/7/22.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REGISTER_SUCCESS = 1;
    private static final int REGISTER_ERROR = -1;
    private static final int LOGIN_SUCCESS = 2;
    private static final int LOGIN_ERROR = -2;

    private @BindView(R.id.account)EditText accountId;
    private @BindView(R.id.password)EditText password;
    private @BindView(R.id.login)Button login;
    private @BindView(R.id.student)CheckBox student;
    private @BindView(R.id.teacher)CheckBox teacher;

    private String type;
    private static String token = null;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REGISTER_SUCCESS:
                        if (msg.what == 200) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("成功").setMessage("注册成功，是否登录？").setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setLogin();
                                }
                            });
                        }
                    else {
                            Toast.makeText(LoginActivity.this,"抱歉，注册失败",Toast.LENGTH_SHORT).show();
                        }
                    break;
                case REGISTER_ERROR:
                    Toast.makeText(LoginActivity.this,"抱歉，注册失败",Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_SUCCESS:
                    UserBean user = (UserBean) msg.obj;
                    if (user.getToken() != null){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("token",token);
                        intent.putExtra("type",user.getType());
                    setResult(RESULT_OK,intent);
                    finish();
                    }
                    break;
                case LOGIN_ERROR:
                    Toast.makeText(LoginActivity.this,"抱歉，登录失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token == null){
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_login,(ViewGroup)findViewById(R.id.dialog));
                new AlertDialog.Builder(LoginActivity.this).setView(R
                .layout.dialog_login).show();//选择注册的类型
                }else {
                    setLogin();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.student:
                if (student.isChecked()){
                    type = "student";
                    String address = "http://api.moinut.com/asker/register.php";
                    HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
                        @Override
                        public void onSucceed(DataOutputStream out) {
                            try {
                                out.writeBytes("accountId="+accountId.getText().toString()+"password="+password.getText().toString()+"type="+type);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Message message = Message.obtain();
                            message.what = REGISTER_SUCCESS;
                            message.obj = JSONObjectUtils.pareseString(response);
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void onError(Exception e) {
                        Message message = Message.obtain();
                            message.what = REGISTER_ERROR;
                            mHandler.sendMessage(message);
                        }
                    });
                }
            break;
            case R.id.teacher:
                if (teacher.isChecked()){

                }
        }
    }

    public void setLogin(){
        String address = "http://api.moinut.com/asker/login.php";
        HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
            @Override
            public void onSucceed(DataOutputStream out) {
                try {
                    out.writeBytes("accountId="+accountId.getText().toString()+"password="+password.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = LOGIN_SUCCESS;
                message.obj = JSONObjectUtils.pareseUser(response);
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                    Message message = Message.obtain();
                message.what = LOGIN_ERROR;
                mHandler.sendMessage(message);
            }
        });
    }
}
