package com.wusui.askertwice.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wusui.askertwice.App;
import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.Utils.SPUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.model.StudentBean;
import com.wusui.askertwice.model.TeacherBean;

import java.io.DataOutputStream;

/**
 * Created by fg on 2016/7/23.
 */

public class DetailsActivity extends BaseActivity {


    private String type;
    private String sex;
    private EditText nickName ;
    private EditText tel;
    private EditText email;
    private EditText college;
    private EditText college_tea;
    private EditText year ;
    private EditText academy;
    private EditText major;
    private EditText academy_tea ;
    private EditText name ;
    private Toolbar mToolbar;
    private RadioButton male ;
    private RadioButton female;
    private RadioGroup radiogroup;
    private Button relogin;

    private static final int CHANGE_SUCCESS = 5;
    private static final int CHANGE_ERROR = -5;
    private static final int USER_INFO_SUCCESS = 6;
    private static final int USER_INFO_ERROR = -6;
    private String token = App.getUser(DetailsActivity.this).getToken();
    private TeacherBean teacherBean;
    private StudentBean studentBean;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_SUCCESS:
                    Log.e("DetailsActivity","成功了吗？");
                    ProgressDialog progressDialog = new ProgressDialog(DetailsActivity.this);
                    progressDialog.setTitle("更新信息中");
                    progressDialog.setMessage("请稍后。。。");
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                    if (msg.arg1 == 200) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(DetailsActivity.this).setTitle("成功").setMessage("资料更新成功").setCancelable(false).setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendData();
                            }
                        }).show();
                    }
                    break;
                case CHANGE_ERROR:
                    break;
                case USER_INFO_SUCCESS:
                    updateData(msg.obj);
                    break;
                case USER_INFO_ERROR:
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        type = intent.getStringExtra("data_type");
        studentBean = (StudentBean) intent.getSerializableExtra("studentBean");
        teacherBean = (TeacherBean) intent.getSerializableExtra("teacherBean");
        if (type.equals("student") ) {
            setContentView(R.layout.activity_details_student);
        } else if(type.equals("teacher")) {
            setContentView(R.layout.activity_details_teacher);
        }

        initView();
        initToolbar();
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == male.getId()) {
                    sex = "男";
                } else {
                    sex = "女";
                }
            }
        });

        relogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.remove(DetailsActivity.this,"account");
                SPUtils.remove(DetailsActivity.this,"password");
                SPUtils.remove(DetailsActivity.this,"state");
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                intent.putExtra("relogin", "点击登录");
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        if (studentBean != null){
            updateData(studentBean);
        }else if (teacherBean != null){
            updateData(teacherBean);
        }
    }

    private void initView() {

        nickName = (EditText) findViewById((R.id.nickname));
        tel = (EditText) findViewById((R.id.tel));
        email = (EditText) findViewById(R.id.email);
        college = (EditText) findViewById(R.id.college);
        college_tea = (EditText) findViewById(R.id.college_teacher);
        year = (EditText) findViewById(R.id.year);
        academy = (EditText) findViewById(R.id.academy);
        major = (EditText) findViewById(R.id.major);
        academy_tea = (EditText) findViewById(R.id.academy_teacher);
        name = (EditText) findViewById(R.id.name);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        male = (RadioButton) findViewById(R.id.sex_male);
        female = (RadioButton) findViewById(R.id.sex_female);
        radiogroup = (RadioGroup) findViewById(R.id.radio_group_sex);
        relogin = (Button) findViewById(R.id.details_button);
    }

    private void initToolbar() {
        mToolbar.setTitle("详细");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_finish:
                        String address = " http://api.moinut.com/asker/updateUserInfo.php";
                        HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
                            @Override
                            public void onSucceed(DataOutputStream out) {
                                try {
                                    if (type.equals("student")) {
                                        out.writeBytes("type=" + type + "&token=" + token + "&nickName=" + nickName.getText().toString() +
                                                "&sex=" + sex + "&tel=" + tel.getText().toString() + "&email=" + email.getText().toString() + "&college=" + college.getText().toString() +
                                                "&academy=" + academy.getText().toString() + "&year=" + year.getText().toString() + "&major=" + major.getText().toString());
                                    } else {
                                        out.writeBytes("type=" + type + "&token=" + token + "&nickName=" + nickName.getText().toString() +
                                                "&sex=" + sex + "&tel=" + tel.getText().toString() + "&email=" + email.getText().toString() + "&college=" + college_tea.getText().toString() +
                                                "&academy=" + academy_tea.getText().toString() + "&realName=" + name.getText().toString());
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                Message message = Message.obtain();
                                message.what = CHANGE_SUCCESS;
                                message.arg1 = JSONObjectUtils.pareseString(response);
                                Log.e("DetailsActivity",response);
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
        getMenuInflater().inflate(R.menu.menu_details,menu);
        return true;
    }
    private void updateData(Object o) {
        if (type.equals("student")) {
            StudentBean student = (StudentBean) o;
            String show_sex = student.getSex();
            if (show_sex.equals( "male")) {
                male.setChecked(true);
            } else {
                female.setChecked(true);
            }
            nickName.setText(student.getNickName());
            tel.setText(student.getTel());
            email.setText(student.getEmail());
            college.setText(student.getCollege());
            academy.setText(student.getAcademy());
            major.setText(student.getMajor());
        } else {
            TeacherBean teacher = (TeacherBean) o;
            nickName.setText(teacher.getNickName());
            tel.setText(teacher.getTel());
            email.setText(teacher.getEmail());
            college_tea.setText(teacher.getCollege());
            academy_tea.setText(teacher.getAcademy());
            name.setText(teacher.getRealName());
        }
    }

    private void sendData() {
        String address = " http://api.moinut.com/asker/getUserInfo.php";
        HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
            @Override
            public void onSucceed(DataOutputStream out) {
                try {
                    out.writeBytes("type=" + type + "&token=" + token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = USER_INFO_SUCCESS;
                if (type.equals("student")) {
                    message.obj = JSONObjectUtils.pareseStudent(response);
                } else {
                    message.obj = JSONObjectUtils.pareseTeacher(response);
                }
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
