package com.wusui.askertwice.model;

import com.wusui.askertwice.ui.adapter.QuestionsAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fg on 2016/8/8.
 */

public class QuestionsApi {
    private final QuestionsServices mServices;

    public QuestionsApi(){
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).build();
        mServices = retrofit.create(QuestionsServices.class);

    }

    public Call<List<QuestionsBean>>questionsBean(int page){
        return mServices.questionsBean(page);
    }
}
