package com.yanovich.alex.androidmvpsimpleclient.util;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alex on 01.05.2016.
 */
public class MapsHelper {
    public static CameraPosition getCameraPositionByShop(Shop shop){
        double latitude = 0;
        double longitude = 0;
        try {
             latitude = Double.parseDouble(shop.mShopLatCoord);
             longitude = Double.parseDouble(shop.mShopLonCoord);
        }catch (NumberFormatException e){
        }
        CameraPosition newPosition =
                new CameraPosition.Builder().target(new LatLng(latitude, longitude))
                        .zoom(13f)
                        .bearing(300)
                        .build();
        return newPosition;
    }

    public static Set<MarkerOptions> getMarkersByShopsList(List<Shop> list){
        Set<MarkerOptions> set = new HashSet<>();
        for (int i = 0; i <list.size() ; i++) {
            Shop s = list.get(i);
            LatLng latlng = new LatLng(Double.parseDouble(s.mShopLatCoord) ,Double.parseDouble(s.mShopLonCoord));
            set.add(new MarkerOptions().position(latlng).title(s.mShopName));
        }
      return set;
    }
}
