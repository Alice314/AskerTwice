package com.wusui.askertwice.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wusui.askertwice.model.AnswerBean;
import com.wusui.askertwice.model.ApiWrapper;
import com.wusui.askertwice.model.LoginWrapper;
import com.wusui.askertwice.model.QuestionsBean;
import com.wusui.askertwice.model.StudentBean;
import com.wusui.askertwice.model.TeacherBean;
import com.wusui.askertwice.model.UserBean;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by fg on 2016/7/22.
 */

public class JSONObjectUtils {

    public static List<QuestionsBean> parseQuestion(String response){
        Gson gson = new Gson();
        Type jsonType = new TypeToken<ApiWrapper<List<QuestionsBean>>>(){}.getType();
        ApiWrapper<List<QuestionsBean>> status = gson.fromJson(response,jsonType);
        return status.getData();
    }

    public static List<AnswerBean> pareseAnswer(String response){
        Gson gson = new Gson();
        Type jsonType = new TypeToken<ApiWrapper<List<AnswerBean>>>(){}.getType();
        ApiWrapper<List<AnswerBean>> status = gson.fromJson(response,jsonType);
        return status.getData();
    }

    public static UserBean pareseUser(String response){
        Gson gson = new Gson();
        Type jsonType = new TypeToken<LoginWrapper<UserBean>>(){}.getType();
        LoginWrapper<UserBean> status = gson.fromJson(response,jsonType);
        return status.getData();
    }

    public static StudentBean pareseStudent(String response){
        Gson gson = new Gson();
        Type jsonType = new TypeToken<LoginWrapper<List<StudentBean>>>(){}.getType();
        LoginWrapper<StudentBean> status = gson.fromJson(response,jsonType);
        return status.getData();
    }

    public static List<TeacherBean> pareseTeacher(String response){
        Gson gson = new Gson();
        Type jsonType = new TypeToken<LoginWrapper<List<TeacherBean>>>(){}.getType();
        LoginWrapper<List<TeacherBean>> status = gson.fromJson(response,jsonType);
        return status.getData();
    }

    public static int pareseString(String response){
        Gson gson = new Gson();
        Type jsonType = new TypeToken<LoginWrapper<String>>(){}.getType();
        LoginWrapper<String>status = gson.fromJson(response, jsonType);
        return status.getState();
    }

}
