package com.yanovich.alex.androidmvpsimpleclient.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.SimpleClientApplication;
import com.yanovich.alex.androidmvpsimpleclient.data.DataManager;
import com.yanovich.alex.androidmvpsimpleclient.injection.component.ActivityComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.component.DaggerActivityComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.module.ActivityModule;
import com.yanovich.alex.androidmvpsimpleclient.ui.users.UsersActivity;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Alex on 01.05.2016.
 */
public class SplashActivity extends AppCompatActivity{
    private ActivityComponent mActivityComponent;
    @Inject
    DataManager mDataManager;
    // Splash screen timer
   // private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, UsersActivity.class);
                if (getIntent().getExtras() == null) {
                    startActivity(i);
                } else if (!getIntent().getExtras().getBoolean(BaseActivity.SPLASH_ACT_KEY, false)) {
                    startActivity(i);
                } else {
                    finish();
                }


            }
        }, mDataManager.getPreferencesHelper().getSplashTime());

        Timber.i("Splash activity oncreate time: "+mDataManager.getPreferencesHelper().getSplashTime());
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(SimpleClientApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }
}
