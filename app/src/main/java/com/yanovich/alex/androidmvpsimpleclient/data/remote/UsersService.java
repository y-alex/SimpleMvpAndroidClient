package com.yanovich.alex.androidmvpsimpleclient.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Alex on 27.04.2016.
 */
public interface UsersService {

    String ENDPOINT = "http://wildfly-yalek.rhcloud.com/";

    @GET("users")
    Observable<List<User>> getUsers();

    @Headers("Content-Type: application/json")
    @POST("users/{id}/shops")
    Call<Void> createShop(@Path("id") int userId,@Body Shop shop);

    @Headers("Content-Type: application/json")
    @POST("users")
    Call<Void> createUser(@Body User user);

    @DELETE("users/{userId}/shops/{shopId}")
    Call<Void> deleteShop(@Path("shopId") int shopId, @Path("userId") int userId );

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static UsersService newUsersService() {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(UsersService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(UsersService.class);
        }
    }
}
