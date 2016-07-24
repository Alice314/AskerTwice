package com.wusui.askertwice.Utils;

import com.google.gson.Gson;
import com.wusui.askertwice.model.AnswerBean;
import com.wusui.askertwice.model.ApiWrapper;
import com.wusui.askertwice.model.LoginWrapper;
import com.wusui.askertwice.model.QuestionsBean;
import com.wusui.askertwice.model.StudentBean;
import com.wusui.askertwice.model.TeacherBean;
import com.wusui.askertwice.model.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fg on 2016/7/22.
 */

public class JSONObjectUtils {

    public static List<QuestionsBean> parseQuestion(String response){
        Gson gson = new Gson();
        ApiWrapper<List<QuestionsBean>> status = gson.fromJson(response,ApiWrapper.class);
        List questions = new ArrayList();
        questions.addAll(status.getData());
        return questions;
    }

    public static List<AnswerBean> pareseAnswer(String response){
        Gson gson = new Gson();
        ApiWrapper<List<AnswerBean>> status = gson.fromJson(response,ApiWrapper.class);
        List answers = new ArrayList();
        answers.addAll(status.getData());
        return answers;
    }

    public static UserBean pareseUser(String response){
        Gson gson = new Gson();
        LoginWrapper<UserBean> status = gson.fromJson(response,LoginWrapper.class);
        UserBean user = status.getData();
        return user;
    }

    public static StudentBean pareseStudent(String response){
        Gson gson = new Gson();
        LoginWrapper<StudentBean> status = gson.fromJson(response,LoginWrapper.class);
        StudentBean student = status.getData();
        return student;
    }

    public static List<TeacherBean> pareseTeacher(String response){
        Gson gson = new Gson();
        LoginWrapper<List<TeacherBean>> status = gson.fromJson(response,LoginWrapper.class);
        List teacher = new ArrayList();
        teacher.addAll(status.getData());
        return teacher;
    }

    public static int pareseString(String response){
        Gson gson = new Gson();
        LoginWrapper<String>status = gson.fromJson(response, LoginWrapper.class);
        int datas = status.getState();
        return datas;
    }

}
