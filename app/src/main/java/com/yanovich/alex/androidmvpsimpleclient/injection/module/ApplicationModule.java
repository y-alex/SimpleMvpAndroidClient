package com.yanovich.alex.androidmvpsimpleclient.injection.module;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;
import com.yanovich.alex.androidmvpsimpleclient.data.remote.UsersService;
import com.yanovich.alex.androidmvpsimpleclient.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alex on 27.04.2016.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    UsersService provideUsersService() {
        return UsersService.Creator.newUsersService();
    }

}