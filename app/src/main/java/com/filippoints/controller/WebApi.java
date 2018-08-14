package com.filippoints.controller;

import com.filippoints.model.AssignedPoints;
import com.filippoints.model.AwardedPoints;
import com.filippoints.model.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hlib on 6/21/18.
 */

public interface WebApi {
    @GET("people/")
    Call<List<Person>> getPeople(@Query("count") int count);

    @GET("points/")
    Call<List<AwardedPoints>> getPoints(@Query("count") int count);

    @Headers("Content-type: application/json")
    @POST("add-points/")
    Call<Void> submitPoints(@Body AssignedPoints assignedPoints);
}
