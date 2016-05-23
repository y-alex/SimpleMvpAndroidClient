package com.yanovich.alex.androidmvpsimpleclient.data;

import com.yanovich.alex.androidmvpsimpleclient.data.local.DatabaseHelper;
import com.yanovich.alex.androidmvpsimpleclient.data.local.PreferencesHelper;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.data.remote.UsersService;
import com.yanovich.alex.androidmvpsimpleclient.util.EventPosterHelper;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by Alex on 27.04.2016.
 */
@Singleton
public class DataManager {

    private final UsersService mUsersService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final EventPosterHelper mEventPoster;

    @Inject
    public DataManager(UsersService mUsersService, DatabaseHelper mDatabaseHelper
            , PreferencesHelper mPreferencesHelper, EventPosterHelper mEventPoster) {
        this.mUsersService = mUsersService;
        this.mDatabaseHelper = mDatabaseHelper;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mEventPoster = mEventPoster;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<User> syncUsers() {
        //FlatMap may interleave emissions(look marbel) of Observables while ConcatMap
        // will save order of emissions
        return mUsersService.getUsers()
                .concatMap(new Func1<List<User>, Observable<User>>() {
                    @Override
                    public Observable<User> call(List<User> users) {
                        return mDatabaseHelper.setUsers(users);
                    }
                });
    }

    public Call<Void> createShop(Shop s, int userId){
       return mUsersService.createShop(userId,s);

    }

    public Call<Void> createUser(User u){
        return mUsersService.createUser(u);
    }


//public void createShop(Shop s, int userId){
//    try {
//        mUsersService.createShop(userId,s).execute();
//    }catch (IOException e){
//        Timber.i("IO Exception in createShop(): "+e.toString());
//    }
//
//}


    public Observable<List<User>> getUsers() {
        //distinct suppress duplicate items emitted by an Observable
        return mDatabaseHelper.getUsers().distinct();
    }

    public Observable<List<Shop>> getShops() {
        //distinct suppress duplicate items emitted by an Observable
        return mDatabaseHelper.getShops().distinct();
    }

    public Observable<List<Shop>> getShopsByUser(long userId) {
        //distinct suppress duplicate items emitted by an Observable
        return mDatabaseHelper.getShopsByUser(userId);
    }

    public Observable<User> getUserById(long userId) {
        return mDatabaseHelper.getUserById(userId);
    }

    /// Helper method to post events from doOnCompleted.
    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                mEventPoster.postEventSafely(event);
            }
        };
    }

    public Call<Void> deleteShop(int shopId, int userId) {
        return mUsersService.deleteShop(shopId,userId);
    }
}
