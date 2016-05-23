package com.yanovich.alex.androidmvpsimpleclient.ui.base;

/**
 * Created by Alex on 27.04.2016.
 */
public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
