package com.yanovich.alex.androidmvpsimpleclient.ui.shopsmap;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.data.SyncService;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.BaseActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.shopsmap.adapter.SectionsPagerAdapter;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShopsMapActivity extends BaseActivity implements ShopsMapMvpView, OnMapReadyCallback {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @BindView(R.id.container)
    public ViewPager mViewPager;

    private GoogleMap mMap;

    @Inject
    ShopsMapPresenter mShopsMapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_shops_map);

        ButterKnife.bind(this);
        mShopsMapPresenter.attachView(this);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Shops map");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        if (mViewPager != null) {
            if (savedInstanceState != null) {
                mViewPager.setCurrentItem(savedInstanceState.getInt("currentshop", 0));
            }
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
                @Override public void onPageSelected(int position) {
                    mShopsMapPresenter.viewPagerPositionChanged(position);
                }
                @Override public void onPageScrollStateChanged(int state) {}
            });
        }
        mShopsMapPresenter.loadShops();
        startService(SyncService.getStartIntent(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab_search, menu);

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mShopsMapPresenter.searchTextChanged(newText,getApplicationContext());
                return false;
            }
        };
        SearchView searchView =(SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(listener);
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShopsMapPresenter.detachView();
    }

    @Override
    public void addShopsMarkers(Set<MarkerOptions> setMarkers) {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
        for(MarkerOptions markerOptions:setMarkers){
            mMap.addMarker(markerOptions);
        }
    }

    @Override
    public void moveCamera(CameraPosition cameraPosition) {
        if (!checkReady()) {
            return;
        }
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)
                , new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
               // Toast.makeText(getBaseContext(), "Animation to Shop finished", Toast.LENGTH_SHORT)
               //         .show();
            }
            @Override
            public void onCancel() {
              //  Toast.makeText(getBaseContext(), "Animation to Shop canceled", Toast.LENGTH_SHORT)
              //          .show();
            }
        });

    }

    @Override
    public void swapShopsPager(List<Shop> list) {
        mSectionsPagerAdapter.swapShopsList(list);

    }

    @Override
    public void moveShopsPager(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showShopsEmpty() {
        Toast.makeText(this, R.string.shops_list_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
