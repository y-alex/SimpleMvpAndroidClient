package com.yanovich.alex.androidmvpsimpleclient.ui.shopsmap;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Alex on 30.04.2016.
 */
public class PlaceHolderFragment extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceHolderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceHolderFragment newInstance(int sectionNumber, Shop shop) {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable("StringKey", shop);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Shop shop = (Shop)getArguments().getParcelable("StringKey");
        View rootView = inflater.inflate(R.layout.item_shop, container, false);

        TextView textViewShopName = (TextView)rootView.findViewById(R.id.text_shop_name);
        TextView textViewShopLat = (TextView)rootView.findViewById(R.id.text_shop_lat_coord);
        TextView textViewShopLon = (TextView)rootView.findViewById(R.id.text_shop_lon_coord);
        TextView textViewShopOwner = (TextView)rootView.findViewById(R.id.text_shop_owner_name);
        ImageView imageView = (ImageView)rootView.findViewById(R.id.item_shop_image);

        textViewShopName.setText(shop.mShopName);
        textViewShopLat.setText("Lat: "+shop.mShopLatCoord);
        textViewShopLon.setText("Lon: "+shop.mShopLonCoord);
        textViewShopOwner.setText("User: "+shop.mShopUser.mUserName);
        Uri uri;
        try {
            uri = Uri.parse(shop.mShopImgUri);
        }catch (NullPointerException e){
            uri = Utils.getUriToDrawable(getActivity(), R.drawable.ic_shops_24dp);
        }
        Glide.with(getActivity()).load(uri).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_shops_24dp).into(imageView);

        return rootView;
    }

}