package com.yanovich.alex.androidmvpsimpleclient.injection.module;

import android.app.Activity;
import android.content.Context;

import com.yanovich.alex.androidmvpsimpleclient.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alex on 27.04.2016.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }
}
