package com.yanovich.alex.androidmvpsimpleclient.ui.shopsmap;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.MvpView;

import java.util.List;
import java.util.Set;

/**
 * Created by Alex on 30.04.2016.
 */
public interface ShopsMapMvpView extends MvpView {

    void addShopsMarkers(Set<MarkerOptions> setMarkers);

    void moveCamera(CameraPosition cameraPosition);

    void swapShopsPager(List<Shop> list);

    void moveShopsPager(int position);

    void showError();

    void showShopsEmpty();

}
