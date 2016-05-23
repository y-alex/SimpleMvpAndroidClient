package com.yanovich.alex.androidmvpsimpleclient.ui.shops;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alex on 14.05.2016.
 */
public class ShopDetailFragment extends Fragment {
    Shop mShop;
    @BindView(R.id.item_shop_image)
    ImageView mShopImage;

    @BindView(R.id.text_shop_name)
    TextView mShopName;

    @BindView(R.id.text_shop_lat_coord)
    TextView mShopLatCoord;

    @BindView(R.id.text_shop_lon_coord)
    TextView mShopLonCoord;

    @BindView(R.id.text_shop_owner_name)
    TextView mShopUserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
             mShop = arguments.getParcelable("shop_fragment");

        }
        View rootView = inflater.inflate(R.layout.detail_shop_custom, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mShop != null){
            try{
                Uri uri;
                try {
                    uri = Uri.parse(mShop.mShopImgUri);
                }catch (NullPointerException e){
                    uri = Utils.getUriToDrawable(getActivity(), R.drawable.ic_shops_24dp);
                }

                Glide.with(this).load(uri).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_shops_24dp).into(mShopImage);
                mShopName.setText(mShop.mShopName);
                mShopLatCoord.setText("Latitude:" + mShop.mShopLatCoord);
                mShopLonCoord.setText("Longitude: " + mShop.mShopLonCoord);
                User user = mShop.mShopUser;

                if(user != null) {
                    mShopUserName.setText("User name: " + user.mUserName);
                }
            }catch(NullPointerException e){
                Toast.makeText(getActivity(), "Cant open shop", Toast.LENGTH_LONG);
            }
        }
    }
}
