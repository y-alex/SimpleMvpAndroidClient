package com.yanovich.alex.androidmvpsimpleclient.ui.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.SimpleClientApplication;
import com.yanovich.alex.androidmvpsimpleclient.injection.component.ActivityComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.component.DaggerActivityComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.module.ActivityModule;
import com.yanovich.alex.androidmvpsimpleclient.ui.settings.SettingsActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.shops.ShopsActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.shopsmap.ShopsMapActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.users.UsersActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Alex on 27.04.2016.
 */
public class BaseActivity extends AppCompatActivity  implements
        NavigationView.OnNavigationItemSelectedListener{
    public static final String SPLASH_ACT_KEY = "splash_act_key";

    private ActivityComponent mActivityComponent;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mNavDrawer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ||
                newConfig.orientation == Configuration.ORIENTATION_PORTRAIT   ) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.putExtra(SPLASH_ACT_KEY, true);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_shops) {
            Intent intent = new Intent(this, ShopsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_shops_map) {
            Intent intent = new Intent(this, ShopsMapActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_users) {
           Intent intent = new Intent(this, UsersActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}