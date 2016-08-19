package com.wusui.askertwice.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by fg on 2016/8/8.
 */

public interface QuestionsServices {
    @GET("/10/{page}")
    Call<List<QuestionsBean>> questionsBean (@Path("page")int page);
}
