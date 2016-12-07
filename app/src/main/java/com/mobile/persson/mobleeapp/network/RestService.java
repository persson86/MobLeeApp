package com.mobile.persson.mobleeapp.network;

import com.mobile.persson.mobleeapp.BuildConfig;
import com.mobile.persson.mobleeapp.database.SearchTagModel;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by persson on 07/12/16.
 */

public interface RestService {
    @GET("search")
    Call<SearchTagModel> getQuestionsByTag(@Query("order") String order,
                                           @Query("sort") String sort,
                                           @Query("tagged") String tagged,
                                           @Query("site") String site);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.API_END_POINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
