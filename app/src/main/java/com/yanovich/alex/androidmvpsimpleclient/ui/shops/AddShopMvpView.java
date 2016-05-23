package com.yanovich.alex.androidmvpsimpleclient.ui.shops;

import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.MvpView;

import java.util.List;

/**
 * Created by Alex on 15.05.2016.
 */
public interface AddShopMvpView extends MvpView {


    void showError();

    void showUsersEmpty();

    void showUsers(List<User> users);

    void showCreationIsSucsessful(boolean successful);
}
