package com.yanovich.alex.androidmvpsimpleclient.ui.shops;

import android.os.AsyncTask;

import com.yanovich.alex.androidmvpsimpleclient.data.DataManager;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Alex on 29.04.2016.
 */
public class ShopsPresenter extends BasePresenter<ShopsMvpView> {
    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public ShopsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ShopsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }


    public void loadUserShops() {
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
                        Timber.e(e, "There was an error loading the users.");
                        getMvpView().showToast("Errorrrrrrrrrr in loadUserShops");
                    }

                    @Override
                    public void onNext(List<Shop> shops) {
                        if (shops.isEmpty()) {
                            getMvpView().showShopEmpty();
                        } else {
                            getMvpView().showShops(shops);
                        }
                    }
                });
    }

    public void deleteShop(final Shop shop) {
        Timber.i("ShopsPresenter: deleteShop() triggered.");
        mDataManager.deleteShop(shop.mId, shop.mShopUser.mId).
                enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                       if(response.code() == 200 ) {
                           getMvpView().deletedShop(shop);
                           Timber.i("OnResponse deleteShop() : " + "Shop MId:" + shop.mId);
                       }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Timber.i("OnFailure method deleteshop(): " + t.toString());
                        getMvpView().showToast("Shop has not been deleted,!");
                    }
                });
    }

}
