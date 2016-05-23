package com.yanovich.alex.androidmvpsimpleclient.injection.component;

import com.yanovich.alex.androidmvpsimpleclient.injection.PerActivity;
import com.yanovich.alex.androidmvpsimpleclient.injection.module.ActivityModule;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.SplashActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.settings.SettingsActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.shops.AddShopActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.shops.ShopsActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.shopsmap.ShopsMapActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.users.AddUserActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.users.UsersActivity;

import dagger.Component;

/**
 * Created by Alex on 27.04.2016.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(UsersActivity usersActivity);
    void inject(ShopsMapActivity usersActivity);
    void inject(ShopsActivity shopsActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(SplashActivity splashActivity);
    void inject(AddShopActivity addShopActivity);
    void inject(AddUserActivity addUserActivity);

}