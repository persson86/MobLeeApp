package com.mobile.persson.mobleeapp.network;

import com.mobile.persson.mobleeapp.BuildConfig;
import com.mobile.persson.mobleeapp.database.models.AnswerModel;
import com.mobile.persson.mobleeapp.database.models.QuestionModel;
import com.mobile.persson.mobleeapp.database.models.TagModel;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by persson on 07/12/16.
 */

public interface RestService {
    @GET("search")
    Call<QuestionModel> getQuestionsByTag(@Query("order") String order,
                                          @Query("sort") String sort,
                                          @Query("tagged") String tagged,
                                          @Query("site") String site,
                                          @Query("pagesize") String pagesize,
                                          @Query("filter") String filter);

    @GET("questions/{questionId}/answers")
    Call<AnswerModel> getAnswers(@Path("questionId") int questiondId,
                                 @Query("order") String order,
                                 @Query("sort") String sort,
                                 @Query("site") String site,
                                 @Query("filter") String filter);

    @GET("tags/{tag}/related")
    Call<TagModel> getRelatedTags(@Path("tag") String tag,
                                  @Query("site") String site,
                                  @Query("pagesize") String pagesize);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.API_END_POINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
