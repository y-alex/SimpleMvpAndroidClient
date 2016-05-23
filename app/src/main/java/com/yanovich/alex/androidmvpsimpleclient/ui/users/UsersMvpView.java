package com.yanovich.alex.androidmvpsimpleclient.ui.users;

import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.MvpView;

import java.util.List;

/**
 * Created by Alex on 27.04.2016.
 */
public interface UsersMvpView  extends MvpView{

    void showUsers(List<User> users);

    void showUsersEmpty();

    void showError();
}
