package com.thumper.thumper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Ivan on 10/4/2017.
 */

public interface Communication {
    @GET("neopixels/strings/{id}")
    Call<NeoPixels> pixels(
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST("neopixels/strings/{id}")
    Call<NeoPixels> pixels(
            @Path("id") int id,
            @Field("red") int red,
            @Field("green") int green,
            @Field("blue") int blue
    );

    @FormUrlEncoded
    @POST("speed")
    Call<NeoPixels> go(
            @Field("left_speed") int left_speed,
            @Field("right_speed") int right_speed

    );




}
