package com.yanovich.alex.androidmvpsimpleclient.ui.shopsmap;

import android.content.Context;
import android.widget.Toast;

import com.yanovich.alex.androidmvpsimpleclient.data.DataManager;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.BasePresenter;
import com.yanovich.alex.androidmvpsimpleclient.util.MapsHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Alex on 30.04.2016.
 */
public class ShopsMapPresenter extends BasePresenter<ShopsMapMvpView> {
    private final DataManager mDataManager;
    private Subscription mSubscription;
    List<Shop> mAllShops = new ArrayList<>();

    @Inject
    public ShopsMapPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ShopsMapMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void searchTextChanged(String newText, Context context){
        if(!newText.equals("")) {
            for (int i = 0; i < mAllShops.size(); i++) {
                if (mAllShops.get(i).mShopName.toLowerCase().startsWith(newText.toLowerCase())) {
                    getMvpView().moveShopsPager(i);
                    getMvpView().moveCamera(MapsHelper.getCameraPositionByShop(mAllShops.get(i)));
                    Toast.makeText(context, "Move to shop: "+mAllShops.get(i).mShopName
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(context, "No shops names start like:"+newText, Toast.LENGTH_SHORT).show();
        }
    }

    public void viewPagerPositionChanged(int position){
        Timber.i("Method position: "+ position +" Allshops size:"+mAllShops.size());
        getMvpView().moveCamera(MapsHelper.getCameraPositionByShop(mAllShops.get(position)));
    }

    public void loadShops() {
        checkViewAttached();
        mSubscription = mDataManager.getShops()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Shop>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                       // Timber.e(e, "There was an error loading the shops.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Shop> shops) {
                        if (shops.isEmpty()) {
                            getMvpView().showShopsEmpty();
                        } else {
                            mAllShops = shops;
                           getMvpView().swapShopsPager(shops);
                            getMvpView().addShopsMarkers(MapsHelper.getMarkersByShopsList(shops));
                            getMvpView().moveCamera(MapsHelper.getCameraPositionByShop(shops.get(0)));

                        }
                    }
                });
    }
}
