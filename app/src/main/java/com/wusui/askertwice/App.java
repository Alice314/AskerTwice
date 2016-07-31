package com.wusui.askertwice;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.wusui.askertwice.Utils.SPUtils;
import com.wusui.askertwice.model.UserBean;

/**
 * Created by fg on 2016/7/30.
 */

public class App extends Application{

    private static Context mContext;
    private static UserBean mUser;
    private static String SP_KEY_USER = "user";
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG){

        }
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setUser(Context context, UserBean user){
        String userJson;
        mUser = user;
        if (user != null){
            userJson = new Gson().toJson(user);
        }else {
            userJson = "";
        }
        SPUtils.put(context,SP_KEY_USER,userJson);
    }

    public static UserBean getUser(Context context){
        if (mUser == null){
            String json = (String) SPUtils.get(context,SP_KEY_USER,"");
            mUser = new Gson().fromJson(json,UserBean.class);
        }
        return mUser;
    }
}
