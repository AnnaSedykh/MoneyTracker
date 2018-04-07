package com.annasedykh.moneytracker.api;

import com.annasedykh.moneytracker.items.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("auth")
    Call<AuthResult> auth(@Query("social_user_id") String userId);

    @GET("logout")
    Call<Result> logout();

    @GET("items")
    Call<List<Item>> getItems(@Query("type") String type);

    @POST("items/add")
    Call<ItemsResult> addItem(@Query("price") String price, @Query("name") String name, @Query("type") String type);

    @POST("items/remove")
    Call<ItemsResult> removeItem(@Query("id") int id);
}


