package com.wusui.askertwice.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wusui.askertwice.App;
import com.wusui.askertwice.R;
import com.wusui.askertwice.Utils.HttpUtils;
import com.wusui.askertwice.Utils.JSONObjectUtils;
import com.wusui.askertwice.callback.HttpCallbackListener;
import com.wusui.askertwice.callback.ParamsCallbackListener;
import com.wusui.askertwice.model.StudentBean;
import com.wusui.askertwice.model.TeacherBean;
import com.wusui.askertwice.ui.adapter.QuestionsAdapter;
import com.wusui.askertwice.ui.fragment.CollectFragment;
import com.wusui.askertwice.ui.fragment.MineFragment;
import com.wusui.askertwice.ui.fragment.ResearchFragment;
import com.wusui.askertwice.ui.fragment.UpToDateFragment;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.wusui.askertwice.R.id.student;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private  TextView nav_text;
    private NavigationView navigationView;
    private String token = null;
    private String type = null;
    private StudentBean studentBean;
    private TeacherBean teacherBean;

    private static final int RESULT_LOGIN_FAB = 1;
    private static final int RESULT_LOGIN_TEXTVIEW = 2;
    private static final int MAIN_RE_LOGIN = 3;
    private static final int DETAILS_SUCCESS = 4;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DETAILS_SUCCESS:
                if (type.equals("student")){
                    StudentBean student = (StudentBean) msg.obj;
                    if (student.getNickName() != null){
                        nav_text.setText(student.getNickName());
                        setOnClick(student.getNickName(),token);
                    }else {
                        nav_text.setText("点击完善用户信息");
                        setOnClick("点击完善用户信息",token);
                    }
                }else {
                    TeacherBean teacher = (TeacherBean) msg.obj;
                    if (teacher.getNickName() != null){
                        nav_text.setText(teacher.getNickName());
                        setOnClick(teacher.getNickName(),token);
                    }else {
                        nav_text.setText("点击完善用户信息");
                        setOnClick("点击完善用户信息",token);
                    }
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initToolBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /**这里的token用来判断点击fab以后是登录成功——>去提问
         * 还是登录未成功——>去登陆/注册*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent,RESULT_LOGIN_FAB);
                }else {
                    Intent intent = new Intent(MainActivity.this,AskerActivity.class);
                    startActivity(intent);
                }
            }
        });
        navigationView.setCheckedItem(0);

    }

    private void initView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        nav_text = (TextView) headerView.findViewById(R.id.nav_text);
        nav_text.setText("点击登录");
        setOnClick("点击登录",null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Asker");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite,null));
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.mipmap.ic_menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_search){
                   Toast.makeText(MainActivity.this,"搜索功能暂未开放",Toast.LENGTH_SHORT).show();
                    //搜索功能
                }
                return true;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.newest:
                UpToDateFragment fragment = new UpToDateFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content,fragment);
                transaction.commit();
                break;
            case R.id.search:
                ResearchFragment fragment1 = new ResearchFragment();
                mToolbar.setTitle("搜索");
                FragmentManager fragmentManager1= getSupportFragmentManager();
                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                transaction1.replace(R.id.content,fragment1);
                transaction1.commit();
                break;
            case R.id.collect:
                CollectFragment fragment2 = new CollectFragment();
                mToolbar.setTitle("收藏");
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                transaction2.replace(R.id.content,fragment2);
                transaction2.commit();
                break;
            case R.id.mine:
                MineFragment fragment3 = new MineFragment();
                mToolbar.setTitle("我的");
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                FragmentTransaction transaction3 = fragmentManager3.beginTransaction();
                transaction3.replace(R.id.content,fragment3);
                transaction3.commit();
                break;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.ask:
                if (token == null) {
                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent1,RESULT_LOGIN_FAB);
                }else {
                    Intent intent1 = new Intent(MainActivity.this,AskerActivity.class);
                    startActivity(intent1);
                }
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String address = "http://api.moinut.com/asker/getUserInfo.php";
        switch (requestCode){
            case RESULT_LOGIN_FAB:
            case RESULT_LOGIN_TEXTVIEW:
                if (resultCode == RESULT_OK){
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    navigationView.setCheckedItem(0);
                    token = App.getUser(MainActivity.this).getToken();
                    type = data.getStringExtra("type");
                    Log.e("MainActivity","这里被执行了吗");

                    HttpUtils.sendRequestFor(address, new ParamsCallbackListener() {
                        @Override
                        public void onSucceed(DataOutputStream out) {
                            try {
                                out.writeBytes("type=" + type + "&token=" + token);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Message message = Message.obtain();
                            message.what = DETAILS_SUCCESS;
                            if (type.equals("student")){
                                studentBean = JSONObjectUtils.pareseStudent(response);
                                message.obj = studentBean;
                            }else {
                                teacherBean = JSONObjectUtils.pareseTeacher(response);
                                message.obj = teacherBean;
                            }
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void onError(Exception e) {
                        e.printStackTrace();
                        }
                    });

                }
                break;
            case MAIN_RE_LOGIN:
                if (resultCode == RESULT_CANCELED){
                    String relogin = data.getStringExtra("relogin");
                    nav_text.setText(relogin);
                }
        }
    }
    private void setOnClick(final String s, final String token){
        nav_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.equals("点击登录")){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivityForResult(intent,RESULT_LOGIN_TEXTVIEW);

                }else if (s.equals("点击完善用户信息")){
                    Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                    if (token != null) {
                        intent.putExtra("data_type",type);
                        startActivityForResult(intent,MAIN_RE_LOGIN);
                    }
                }else {
                    Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                    if (type.equals("student")) {
                        intent.putExtra("studentBean", studentBean);
                    }else {
                        intent.putExtra("teacherBean",teacherBean);
                    }
                    startActivity(intent);
                }
            }
        });


    }

}
