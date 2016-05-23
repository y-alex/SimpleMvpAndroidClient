package com.yanovich.alex.androidmvpsimpleclient.ui.shops;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.SimpleClientApplication;
import com.yanovich.alex.androidmvpsimpleclient.data.DataManager;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.injection.component.ActivityComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.component.DaggerActivityComponent;
import com.yanovich.alex.androidmvpsimpleclient.injection.module.ActivityModule;
import com.yanovich.alex.androidmvpsimpleclient.ui.shops.adapter.AddShopUserSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AddShopActivity extends AppCompatActivity implements AddShopMvpView {
    private final static int PLACE_PICKER_REQUEST = 9090;
    private ActivityComponent mActivityComponent;
    private List<User> mUserList = new ArrayList<>();
    private AddShopUserSpinnerAdapter mUserSpinnerAdapter;
    private double mLongitude;
    private double mLatitude;
    private User mUser;

    @Inject AddShopPresenter mAddShopPresenter;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.edit_shop_name) EditText mEditShopName;
    @BindView(R.id.image_shop_coords) ImageView mImageShopCoord ;
    @BindView(R.id.text_shop_coords) TextView mTextShopCoord;
   // @BindView(R.id.image_spinner) Spinner mImageSpinner ;
    @BindView(R.id.user_spinner) Spinner mUserSpinner ;
    @BindView(R.id.text_empty_user_spinner) TextView mTextEmptyUserSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add new shop");

        }
        mAddShopPresenter.attachView(this);
        mAddShopPresenter.loadUsers();


        mUserSpinnerAdapter = new AddShopUserSpinnerAdapter(this,R.layout.item_user, mUserList);
        mUserSpinner.setAdapter(mUserSpinnerAdapter);
        setUserSpinnerListener();

        mImageShopCoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startPlacePicker();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUser == null){
                    Snackbar.make(view, "You need user owner to create new shop!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Shop shop = new Shop();
                    shop.mShopName = mEditShopName.getText().toString().equals("")? "DefaultName":mEditShopName.getText().toString();
                    shop.mShopLatCoord = Double.toString(mLatitude);
                    shop.mShopLonCoord = Double.toString(mLongitude);
                    shop.mShopImgUri = "http://thumbs.dreamstime.com/x/mountain-range-logo-icon-distance-38850368.jpg";
                    mAddShopPresenter.createShop(shop,mUser.mId);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
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
    protected void onDestroy() {
        super.onDestroy();
        mAddShopPresenter.detachView();

    }

    @Override
    public void showError() {
        Toast.makeText(this,"Error, Cant add new Shop!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUsersEmpty() {
        //Toast.makeText(getApplication(),"userslist size: "+users.size(), Toast.LENGTH_LONG).show();
        mTextEmptyUserSpinner.setText("Sorry, no user yet! You can add user in user screen.");
    }

    @Override
    public void showUsers(List<User> users) {
        mUserList = users;

        mUserSpinnerAdapter.updateUserList(users);
    }

    @Override
    public void showCreationIsSucsessful(boolean successful) {
        if(successful) {
            setResult(RESULT_OK);
            finish();
        }
    }

    public void startPlacePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(
                    builder.build(this), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            Timber.i("No gogle play services!"+e);
            Toast.makeText(getApplication(),"No gogle play services!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check to see if the result is from our Place Picker intent
        if (requestCode == PLACE_PICKER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                LatLng latLong = place.getLatLng();

                mLatitude =  latLong.latitude;
                mLongitude = latLong.longitude;
                String coord = String.format("Picked Lat: %f; Lon: %f", mLatitude, mLongitude);

                mTextShopCoord.setText("Coordinates:"+coord);


                Toast.makeText(getApplication(),coord, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setUserSpinnerListener(){
        mUserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(position< mUserSpinnerAdapter.getCount()) {
                   Timber.e("Spinner adapter count "+mUserSpinnerAdapter.getCount());
                   Timber.e("Position selected "+position);
                   mUser = (User) parent.getItemAtPosition(position);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplication(), "Please, select owner of shop", Toast.LENGTH_LONG).show();
            }
        });
    }
}
