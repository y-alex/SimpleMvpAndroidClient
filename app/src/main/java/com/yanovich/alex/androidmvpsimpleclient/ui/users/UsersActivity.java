package com.yanovich.alex.androidmvpsimpleclient.ui.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.data.SyncService;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.BaseActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.shops.AddShopActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.shops.ShopDetailActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.users.adapter.UsersAdapter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersActivity  extends BaseActivity implements UsersMvpView {
    public static final int ADD_USER_CODE = 101;

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.yanovich.alex.androidmvpsimpleclient.ui.users.UsersActivity.EXTRA_TRIGGER_SYNC_FLAG";
    @Inject
    Bus mBus;
    @Inject
    UsersPresenter mUsersPresenter;
    @Inject
    UsersAdapter mUsersAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mNavDrawer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Users");
        }

        mRecyclerView.setAdapter(mUsersAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsersPresenter.attachView(this);
        mUsersPresenter.loadUsers();

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddUserActivity.class);
                startActivityForResult(intent, ADD_USER_CODE);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUsersPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_users_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_bunch) {
            mUsersPresenter.addUsersBunch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showUsers(List<User> users) {
        mUsersAdapter.setUsers(users);
        mUsersAdapter.notifyDataSetChanged();
    }

    @Override
    public void showUsersEmpty() {
        mUsersAdapter.setUsers(Collections.<User>emptyList());
        mUsersAdapter.notifyDataSetChanged();
        Toast.makeText(this, "There are not any users", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        Toast.makeText(this, "There was a problem loading the users", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_USER_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplication(),"New shop successfully created!", Toast.LENGTH_LONG).show();
                startService(SyncService.getStartIntent(this));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe
    public void getMessage(User user) {
        //Toast.makeText(this, user.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra("user_obj", user);
            startActivity(intent);


    }

    @Override
    protected void onStop() {
        super.onStop();
        mBus.unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBus.register(this);
    }

}
