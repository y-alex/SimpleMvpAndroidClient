package com.yanovich.alex.androidmvpsimpleclient.ui.users;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailActivity extends AppCompatActivity {
    @BindView(R.id.view_hex_color) View hexColorView;
    @BindView(R.id.text_name)
    TextView mNameTextView;
    @BindView(R.id.text_last_name) TextView mLastNameTextView;
    @BindView(R.id.text_shops_names) TextView mShopsNamesTextView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("User details");

        }
        showUserDetails();
    }

    private void showUserDetails() {

            User user = getIntent().getExtras().getParcelable("user_obj");
        mNameTextView.setText(String.format("%s %s",user.mUserName, user.mUserLastName));

        StringBuilder shopsNames = new StringBuilder("");
        for(Shop s :user.mShops) {
            shopsNames.append(s.mShopName);
            shopsNames.append('\n');
        }
        mShopsNamesTextView.setText(shopsNames);
        mLastNameTextView.setText(user.mUserAdress);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
