package com.yanovich.alex.androidmvpsimpleclient.injection.component;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;
import com.yanovich.alex.androidmvpsimpleclient.data.DataManager;
import com.yanovich.alex.androidmvpsimpleclient.data.SyncService;
import com.yanovich.alex.androidmvpsimpleclient.data.local.DatabaseHelper;
import com.yanovich.alex.androidmvpsimpleclient.data.local.PreferencesHelper;
import com.yanovich.alex.androidmvpsimpleclient.data.remote.UsersService;
import com.yanovich.alex.androidmvpsimpleclient.injection.ApplicationContext;
import com.yanovich.alex.androidmvpsimpleclient.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Alex on 27.04.2016.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    //if we want inject in other activities, serivices , then need
    //void inject(AnotherServ someServService);
    // important only type: f e: SyncService

    void inject(SyncService syncService);

    @ApplicationContext
    Context context();
    Application application();
    UsersService usersService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    Bus eventBus();

}

