package com.yanovich.alex.androidmvpsimpleclient.data;

import com.yanovich.alex.androidmvpsimpleclient.data.local.DatabaseHelper;
import com.yanovich.alex.androidmvpsimpleclient.data.local.PreferencesHelper;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.data.remote.UsersService;
import com.yanovich.alex.androidmvpsimpleclient.util.EventPosterHelper;

import java.lang.Object;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

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
        List<User> fakeUsers = new ArrayList<>();
        fakeUsers.add(new User(4, new ArrayList<Shop>(),"User1", "User1Last", "user1 adress"));
        fakeUsers.add(new User(5, new ArrayList<Shop>(),"User2", "User2Last", "user2 adress"));

                Observable<List<User>> observable = Observable.just(fakeUsers);
        return observable
                .concatMap(new Func1<List<User>, Observable<User>>() {
                    @Override
                    public Observable<User> call(List<User> users) {
                        return mDatabaseHelper.setUsers(users);
                    }
                });
    }

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

    /// Helper method to post events from doOnCompleted.
    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                mEventPoster.postEventSafely(event);
            }
        };
    }
}
