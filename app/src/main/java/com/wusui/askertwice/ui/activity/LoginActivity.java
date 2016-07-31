package com.wusui.askertwice.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wusui.askertwice.App;
import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.Utils.SPUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.model.UserBean;
import com.wusui.askertwice.ui.fragment.LoginDialogFragment;

import java.io.DataOutputStream;

import butterknife.internal.Utils;

/**
 * Created by fg on 2016/7/22.
 */

public class LoginActivity extends BaseActivity implements LoginDialogFragment.LoginInputListener{

    private static final int REGISTER_SUCCESS = 1;
    private static final int REGISTER_ERROR = -1;
    private static final int LOGIN_SUCCESS = 2;
    private static final int LOGIN_ERROR = -2;

    private EditText et_accountId;
    private EditText et_password;
    private Button login;
    private Button register;

    private String account;
    private String password;
    private String type;

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
                                    setLogin(et_accountId.getText().toString(),et_password.getText().toString());
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
                    Log.e("LoginActivity",user.toString());
                    App.setUser(LoginActivity.this,user);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("type",user.getType());
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                case LOGIN_ERROR:
                    Toast.makeText(LoginActivity.this,"抱歉，登录失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setRegisterOrLogin();
    }

    /**初始化控件*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        Toolbar login_toolbar= (Toolbar) findViewById(R.id.toolbar);
        login_toolbar.setTitle("登录");
        login_toolbar.setTitleTextColor(getColor(R.color.colorWhite));
        setSupportActionBar(login_toolbar);
        login_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        login_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        et_accountId = (EditText) findViewById(R.id.account);
        et_password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);

        Boolean isRemember = (Boolean) SPUtils.get(this,"state",false);
        if (isRemember);
        {
            et_accountId.setText(SPUtils.get(this,"account","").toString());
            et_password.setText(SPUtils.get(this,"password","").toString());
           // setLogin(SPUtils.get(this,"account","").toString(),SPUtils.get(this,"password","").toString());
        }
    }

    /**判断是登录还是注册*/
    private void setRegisterOrLogin(){
                if (register != null) {
                    register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(et_accountId.getText())) {
                                Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(et_password.getText())) {
                                Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                            }else {
                                account = et_accountId.getText().toString();
                                password = et_password.getText().toString();
                                setRegister(account,password);
                            }
                        }
                    });
                }
                if (login != null){
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(et_accountId.getText())) {
                                Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(et_password.getText())) {
                                Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                            }else {
                                account = et_accountId.getText().toString();
                                password = et_password.getText().toString();
                                setLogin(account,password);
                            }

                        }
                    });
                }
        }
    /**获取注册的类型*/
    @Override
    public void onLoginInputComplete(String type) {
        this.type = type;
    }
    private void setRegister(final String account, final String password) {
        LoginDialogFragment dialogFragment = new LoginDialogFragment();
        dialogFragment.show(getSupportFragmentManager(),"loginDialog");

        String address = "http://api.moinut.com/asker/register.php";
        HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
            @Override
            public void onSucceed(DataOutputStream out) {
                try {
                    out.writeBytes("accountId="+account +"&password="+password+"&type="+type);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                SPUtils.put(LoginActivity.this,"account",account);
                SPUtils.put(LoginActivity.this,"password",password);
                SPUtils.put(LoginActivity.this,"state",true);

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


    private void setLogin(final String account, final String password){
        String address = " http://api.moinut.com/asker/login.php";
        HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
            @Override
            public void onSucceed(DataOutputStream out) {
                try {
                    out.writeBytes("accountId=" + account + "&password=" + password);
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
                e.printStackTrace();
                mHandler.sendMessage(message);
            }
        });
    }

}
