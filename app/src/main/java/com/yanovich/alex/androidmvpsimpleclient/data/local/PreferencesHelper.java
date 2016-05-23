package com.yanovich.alex.androidmvpsimpleclient.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Alex on 27.04.2016.
 */
@Singleton
public class PreferencesHelper {
    public static String SPLASH_TIME_PREF;
    public static String EMAIL_PREF;

    public static final String PREF_FILE_NAME = "android_simpleclient_pref_file";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SPLASH_TIME_PREF = context.getString(R.string.pref_splash_time_key);
        EMAIL_PREF = context.getString(R.string.pref_email_key);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void putSplashTime(long time){
        SharedPreferences.Editor spe = mPref.edit();
        spe.putLong(SPLASH_TIME_PREF, time);
        spe.commit();
    }

    public long getSplashTime(){
       return  Long.parseLong(mPref.getString(SPLASH_TIME_PREF, "3000"));
    }

    public String getEmail(){
        return  mPref.getString(EMAIL_PREF, "default@mail.com");
    }

}