package com.yanovich.alex.androidmvpsimpleclient.ui.shops;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.SimpleClientApplication;
import com.yanovich.alex.androidmvpsimpleclient.data.SyncService;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.BaseActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.shops.adapter.ShopsAdapter;
import com.yanovich.alex.androidmvpsimpleclient.ui.users.adapter.UsersAdapter;
import com.yanovich.alex.androidmvpsimpleclient.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Alex on 29.04.2016.
 */
public class ShopsActivity extends BaseActivity implements ShopsMvpView {
    private boolean isTablet;
    private static final int ADD_SHOP_CODE = 100;
    @Inject ShopsPresenter mShopsPresenter;
    @Inject ShopsAdapter mShopsAdapter;
    @Inject Bus mBus;
    @BindView(R.id.recycler_view)  RecyclerView mRecyclerView;
    @BindView(R.id.shop_detail_frame_layout)   FrameLayout mFrameLayout;
    @BindView(R.id.fab) FloatingActionButton mFab;
//    @BindView(R.id.shops_right_layout)
  //  LinearLayout mShop_right_layout;
  //  @BindView(R.id.shops_left_layout)
  //  LinearLayout mShop_left_layout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_shops);
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Shops");
        }

        mRecyclerView.setAdapter(mShopsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mShopsPresenter.attachView(this);
        mShopsPresenter.loadUserShops();
        startService(SyncService.getStartIntent(this));


        //This is programmatical settings for tablet mode
        isTablet = Utils.isTabletDevice(this);

        if(isTablet && mFrameLayout !=null){
            LinearLayout.LayoutParams paramsRecycler = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT);
            paramsRecycler.weight = 1.0f;
            mRecyclerView.setLayoutParams(paramsRecycler);

            LinearLayout.LayoutParams paramsFrame = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT);
            paramsFrame.weight = 2.0f;
            mFrameLayout.setLayoutParams(paramsFrame);

            if (savedInstanceState != null) {
              //  mShopsAdapter.onRestoreInstanceState(savedInstanceState);
            }else{
              //  mShopsAdapter.setFirstItem();
            }
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Fab button clicked");
                Intent intent = new Intent(getApplicationContext(), AddShopActivity.class);
                startActivityForResult(intent, ADD_SHOP_CODE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShopsPresenter.detachView();

    }
    @Override
    public void showShopEmpty() {
        Toast.makeText(this, "No shops yet!", Toast.LENGTH_LONG).show();
        mShopsAdapter.setShops(new ArrayList<Shop>());
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showShops(List<Shop> shops) {
        mShopsAdapter.setShops(shops);
       // showToast(shops.toString());
    }

    @Override
    public void deletedShop(Shop shop) {
        mShopsAdapter.deleteShop(shop);
        showToast("Shop has been successfuly deleted!");
    }

    @Subscribe
    public void getMessage(HashMap<String, Shop> map) {
        final Shop shop;
        if(!map.isEmpty()){
            if(map.containsKey("open")){
                shop = map.get("open");
                if(isTablet){
                    replaceFragment(shop);
                }else {
                    Intent intent = new Intent(this, ShopDetailActivity.class);
                    intent.putExtra("shop_obj", shop);
                    startActivity(intent);
                }
            }else if(map.containsKey("delete")) {
                shop = map.get("delete");

                mShopsPresenter.deleteShop(shop);
            }
        }


    }

    public void replaceFragment(Shop s){
        ShopDetailFragment detailFragment = new ShopDetailFragment();
        Bundle arguments = new Bundle();
        //add Shop
        arguments.putParcelable("shop_fragment", s);
        detailFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.shop_detail_frame_layout, detailFragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_SHOP_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplication(),"New shop successfully created!", Toast.LENGTH_LONG).show();
                startService(SyncService.getStartIntent(this));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
