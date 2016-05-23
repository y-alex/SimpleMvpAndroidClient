package com.yanovich.alex.androidmvpsimpleclient.ui.users;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.SimpleClientApplication;
import com.yanovich.alex.androidmvpsimpleclient.data.DataManager;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.injection.component.ActivityComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.component.DaggerActivityComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.module.ActivityModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class AddUserActivity extends AppCompatActivity {

    @Inject
    DataManager mDataManager;
    private ActivityComponent mActivityComponent;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.edit_first_name)  EditText mEditFirstName;
    @BindView(R.id.edit_last_name) EditText mEditLastName;
    @BindView(R.id.edit_adress) EditText mEditAdress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add user");

        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    createNewUser();
                }
        });

    }

    public void createNewUser(){
        User user = new User();
        user.mUserName = mEditFirstName.getText().toString().equals("")?"DefaultName":mEditFirstName.getText().toString();
        user.mUserLastName = mEditLastName.getText().toString();
        user.mUserAdress = mEditAdress.getText().toString();
        mDataManager.createUser(user).
                enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // response.isSuccessful();
                        if(response.isSuccessful()){
                            Toast.makeText(getApplication(),"User sucssesfuly created!",Toast.LENGTH_LONG).show();
                        }
                        Timber.i("OnResponse method(): " + response.message());
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Timber.i("OnFailre method(): " + t.toString());
                        Toast.makeText(getApplication(), "Cant create new User!", Toast.LENGTH_LONG).show();
                    }
                });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
