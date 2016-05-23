package com.yanovich.alex.androidmvpsimpleclient.ui.settings;

import com.yanovich.alex.androidmvpsimpleclient.data.DataManager;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Alex on 01.05.2016.
 */
public class SettingsPresenter extends BasePresenter<SettingsMvpView> {
    private final DataManager mDataManager;

    @Inject
    public SettingsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SettingsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    public void getEmail() {
        getMvpView().sendEmail(mDataManager.getPreferencesHelper().getEmail());

    }

}