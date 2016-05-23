package com.yanovich.alex.androidmvpsimpleclient.ui.users;

import com.yanovich.alex.androidmvpsimpleclient.data.DataManager;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Alex on 27.04.2016.
 */
public class UsersPresenter extends BasePresenter<UsersMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public UsersPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(UsersMvpView mvpView) {
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

    public void addUsersBunch(){
        List<User> bunch = new ArrayList<>();
       // bunch.add(new User(0,))
       // mDataManager.addBunch();
    }
}
