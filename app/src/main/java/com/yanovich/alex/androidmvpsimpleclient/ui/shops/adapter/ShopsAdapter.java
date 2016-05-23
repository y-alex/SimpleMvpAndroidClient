package com.yanovich.alex.androidmvpsimpleclient.ui.shops.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.otto.Bus;
import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.data.DataManager;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.injection.ApplicationContext;
import com.yanovich.alex.androidmvpsimpleclient.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Alex on 02.05.2016.
 */
public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ShopViewHolder> {
    //private Map<Shop,User> mShopsUsers;
    private List<Shop> mListShops;
    private  Context mContext;
    //private DataManager mDataManager;
    private Bus mBus;

    @Inject
    public ShopsAdapter(@ApplicationContext Context context, Bus eventBus) {
        mContext = context;
     //   mShopsUsers = new HashMap<>();
        mListShops = new ArrayList<>();
        //mDataManager = dataManager;
        mBus = eventBus;
    }

    public void setShops(List<Shop> shopsList) {
        mListShops = shopsList;
        notifyDataSetChanged();
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, int position) {
        Shop shop = mListShops.get(position);
        User user = shop.mShopUser;
        if(user != null) {
            holder.shopUserName.setText("User name: " + user.mUserName);
        }
        Uri uri;
        try {
            uri = Uri.parse(shop.mShopImgUri);
        }catch (NullPointerException e){
            uri = Utils.getUriToDrawable(mContext, R.drawable.ic_shops_24dp);
        }

        Glide.with(mContext).load(uri).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_shops_24dp).into(holder.shopImage);
            holder.shopName.setText(shop.mShopName);
            holder.shopLatCoord.setText("Latitude: " + shop.mShopLatCoord);
            holder.shopLonCoord.setText("Longitude: " + shop.mShopLonCoord);

    }

    @Override
    public int getItemCount() {
      return   mListShops.size();

    }

    class ShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,  View.OnLongClickListener {

        @BindView(R.id.item_shop_image)
        ImageView shopImage;

        @BindView(R.id.text_shop_name)
        TextView shopName;

        @BindView(R.id.text_shop_lat_coord)
        TextView shopLatCoord;

        @BindView(R.id.text_shop_lon_coord)
        TextView shopLonCoord;

        @BindView(R.id.text_shop_owner_name)
        TextView shopUserName;

        public ShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
           Timber.i("Item was clicked pos:+" + getAdapterPosition());
            HashMap<String,Shop> mapToPost = new HashMap<String, Shop>();
            mapToPost.put("open", mListShops.get(getAdapterPosition()));
            busPost(mapToPost);
        }


        @Override
        public boolean onLongClick(View v) {
            final HashMap<String,Shop> mapToPost = new HashMap<String, Shop>();
            mapToPost.put("delete", mListShops.get(getAdapterPosition()));

            PopupMenu popup = new PopupMenu(mContext, v);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.popup_shop_delete:
                            busPost(mapToPost);
                            return true;
                        default:
                            return false;

                    }
                }
            });
            popup.inflate(R.menu.menu_popup_shops_activity);
            popup.show();

            return true;
        }
    }

    public void busPost(HashMap<String, Shop> map){
        mBus.post(map);
    }

    public void deleteShop(Shop shop){
        mListShops.remove(shop);
        notifyDataSetChanged();
    }

    public void setFirstItem(){
        mBus.post(mListShops.get(0));
    }
}
