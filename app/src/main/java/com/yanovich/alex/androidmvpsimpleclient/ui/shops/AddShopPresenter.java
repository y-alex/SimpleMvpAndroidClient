package com.yanovich.alex.androidmvpsimpleclient.ui.shops;

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
 * Created by Alex on 15.05.2016.
 */
public class AddShopPresenter extends BasePresenter<AddShopMvpView> {
    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public AddShopPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(AddShopMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadUsers() {
        checkViewAttached();
        mSubscription = mDataManager.getUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the users.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<User> users) {
                        if (users.isEmpty()) {
                            getMvpView().showUsersEmpty();
                        } else {
                            getMvpView().showUsers(users);
                        }
                    }
                });
    }

    public void createShop(Shop s, int userId){
        mDataManager.createShop(s, userId).
        enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // response.isSuccessful();
                getMvpView().showCreationIsSucsessful(response.isSuccessful());
                Timber.i("OnResponse method(): " + response.message());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Timber.i("OnFailre method(): " + t.toString());
                getMvpView().showError();
            }
        });
    }
}
