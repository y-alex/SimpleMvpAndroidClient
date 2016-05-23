package com.yanovich.alex.androidmvpsimpleclient;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.yanovich.alex.androidmvpsimpleclient.injection.component.ApplicationComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.component.DaggerApplicationComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.module.ApplicationModule;
import com.yanovich.alex.androidmvpsimpleclient.util.Utils;

import timber.log.Timber;

/**
 * Created by Alex on 27.04.2016.
 */
public class SimpleClientApplication extends Application{
    ApplicationComponent mApplicationComponent;
   // private boolean isTablet;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

      //  isTablet = Utils.isTabletDevice(getApplicationContext());

    }

    public static SimpleClientApplication get(Context context) {
        return (SimpleClientApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //public boolean  isDeviceTablet(){
       // return isTablet;
   // }
}
