package com.wusui.askertwice.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.model.StudentBean;
import com.wusui.askertwice.model.TeacherBean;

import java.io.DataOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fg on 2016/7/23.
 */

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.nickname)EditText nickName;
    @BindView(R.id.tel)EditText tel;
    @BindView(R.id.email)EditText email;
    @BindView(R.id.college)EditText college;
    @BindView(R.id.college_teacher)EditText college_tea;
    @BindView(R.id.year)EditText year;
    @BindView(R.id.academy)EditText academy;
    @BindView(R.id.major)EditText major;
    @BindView(R.id.academy_teacher)EditText academy_tea;
    @BindView(R.id.name)EditText name;
    @BindView(R.id.toolbar)Toolbar mToolbar;
    @BindView(R.id.sex_male)RadioButton male;
    @BindView(R.id.sex_female)RadioButton female;
    @BindView(R.id.radio_group)RadioGroup radiogroup;
    private String token,type;
    private String sex;

    private static final int CHANGE_SUCCESS = 5;
    private static final int CHANGE_ERROR = -5;
    private static final int USER_INFO_SUCCESS = 6;
    private static final int USER_INFO_ERROR = -6;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CHANGE_SUCCESS:
                    ProgressDialog progressDialog = new ProgressDialog(DetailsActivity.this);
                    progressDialog.setTitle("更新信息中");
                    progressDialog.setMessage("请稍后。。。");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    if (msg.arg1 == 200){
                        progressDialog.dismiss();
                     new AlertDialog.Builder(DetailsActivity.this).setTitle("成功").setMessage("资料更新成功").setCancelable(false).setPositiveButton("好的", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                         sendData();
                         }
                     });
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

    private void updateData(Object o) {
        if (type == "student"){
            StudentBean student = (StudentBean) o;
            String show_sex = student.getSex();
            if (show_sex == "male"){
                male.setChecked(true);
            }else {
                female.setChecked(true);
            }
            nickName.setText(student.getNickName());
            tel.setText(student.getTel());
            email.setText(student.getEmail());
            college.setText(student.getCollege());
            academy.setText(student.getAcademy());
            major.setText(student.getMajor());
        }else {
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
                   out.writeBytes("type="+type+"token="+token);
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        }, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = USER_INFO_SUCCESS;
                if (type == "student") {
                    message.obj = JSONObjectUtils.pareseStudent(response);
                }else {
                    message.obj = JSONObjectUtils.pareseTeacher(response);
                }
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Intent intent = getIntent();
        token = intent.getStringExtra("data_token");
        type = intent.getStringExtra("date_type");
        if (type == "student") {
            setContentView(R.layout.activity_details_student);
        }else {
            setContentView(R.layout.activity_details_teacher);
        }

        ButterKnife.bind(this);
        initToolbar();
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == male.getId()){
                    sex = "男";
                }else {
                    sex = "女";
                }
            }
        });
    }

    private void initToolbar() {
        mToolbar.setTitle("详细");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_finish:
                        String address = " http://api.moinut.com/asker/updateUserInfo.php";
                        HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
                            @Override
                            public void onSucceed(DataOutputStream out) {
                                try {
                                    if (type == "student"){
                                   out.writeBytes("type="+type+"token="+token+"nickName="+nickName.getText().toString()+
                                    "sex="+sex+"tel="+tel.getText().toString()+"email="+email.getText().toString()+"college="+college.getText().toString()+
                                    "academy="+academy.getText().toString()+"year="+year.getText().toString()+"major="+major.getText().toString());
                                    }else {
                                        out.writeBytes("type="+type+"token="+token+"nickName="+nickName.getText().toString()+
                                                "sex="+sex+"tel="+tel.getText().toString()+"email="+email.getText().toString()+"college="+college_tea.getText().toString()+
                                                "academy="+academy_tea.getText().toString()+"realName="+name.getText().toString());
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                Message message = Message.obtain();
                                message.what = CHANGE_SUCCESS;
                                message.arg1 = JSONObjectUtils.pareseString(response);
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

    @OnClick(R.id.details_button) void onClick(){
        Intent intent = new Intent(DetailsActivity.this,MainActivity.class);
        intent.putExtra("relogin","点击登录");
        setResult(RESULT_CANCELED,intent);
        finish();
    }



}
