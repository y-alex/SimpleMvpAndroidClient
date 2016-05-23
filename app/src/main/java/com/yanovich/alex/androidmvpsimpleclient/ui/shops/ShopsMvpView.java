package com.yanovich.alex.androidmvpsimpleclient.ui.shops;


import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.MvpView;

import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 29.04.2016.
 */
public interface ShopsMvpView extends MvpView {

  void showShopEmpty();

  void showToast(String message);

    void showShops(List<Shop> shops);

  void deletedShop(Shop shop);

}
