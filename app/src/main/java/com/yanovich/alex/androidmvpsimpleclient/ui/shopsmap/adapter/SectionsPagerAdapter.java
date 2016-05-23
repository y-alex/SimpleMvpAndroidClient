package com.yanovich.alex.androidmvpsimpleclient.ui.shopsmap.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.ui.shopsmap.PlaceHolderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 30.04.2016.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    private List<Shop> mShopsList = new ArrayList<>();


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
         return PlaceHolderFragment.newInstance(position, mShopsList.get(position));
    }

    @Override
    public int getCount() {
        return mShopsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mShopsList.get(position).mShopName;
    }

    public void swapShopsList(List<Shop> shops){
        mShopsList = shops;
        notifyDataSetChanged();
    }


//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}