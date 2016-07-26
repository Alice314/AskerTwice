package com.wusui.askertwice.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.model.UserBean;

import java.io.DataOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by fg on 2016/7/22.
 */

public class LoginActivity extends BaseActivity {

    private static final int REGISTER_SUCCESS = 1;
    private static final int REGISTER_ERROR = -1;
    private static final int LOGIN_SUCCESS = 2;
    private static final int LOGIN_ERROR = -2;

    private EditText accountId;
    private EditText password;
    private Button login;
    private RadioButton student;
    private RadioButton teacher;
    private RadioGroup mRadioGroup;
    private SharedPreferences pref;
    private SharedPreferences.Editor mEditor;

    private String address;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toast.makeText(this, "This is LoginActivity!", Toast.LENGTH_SHORT).show();
        initView();

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(accountId.getText())){
                    Toast.makeText(LoginActivity.this,"账号不能为空",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password.getText())){
                    Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    if (token == null){
                        Toast.makeText(LoginActivity.this,accountId.getText().toString(),Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                        dialog.setView(R.layout.dialog_login).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();//选择注册的类型
                    }else {
                        setLogin();
                    }
                }
            }
        });
    }

    private void initView() {
        accountId = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        student = (RadioButton) findViewById(R.id.student);
        teacher = (RadioButton) findViewById(R.id.teacher);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group_type);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == student.getId()){
                    type = "student";
                }
                else {
                    type = "teacher";
                }
                    String address = "http://api.moinut.com/asker/register.php";
                    HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
                        @Override
                        public void onSucceed(DataOutputStream out) {
                            try {
                                out.writeBytes("accountId="+accountId.getText().toString()+"&password="+password.getText().toString()+"&type="+type);
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

                });
    }


    public void setLogin(){
        String account = accountId.getText().toString();
        String password_num = password.getText().toString();

        mEditor = getSharedPreferences("data",MODE_PRIVATE).edit();

        Set<String> insert = new HashSet<>();
        insert.add(account);
        insert.add(password_num);

        mEditor.putStringSet(account,insert);

        pref = getSharedPreferences("data",MODE_PRIVATE);
        Map<String,?>data = pref.getAll();
        for (String s:data.keySet()){
           if (s == account){
                address = "http://api.moinut.com/asker/login.php";
           }else {
               address = " http://api.moinut.com/asker/register.php";
           }
        }

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
