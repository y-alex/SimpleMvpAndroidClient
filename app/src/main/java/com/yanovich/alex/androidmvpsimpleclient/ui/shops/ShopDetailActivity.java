package com.yanovich.alex.androidmvpsimpleclient.ui.shops;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import timber.log.Timber;

public class ShopDetailActivity extends AppCompatActivity {
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

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Shop details");

        }
        showShopDetails();
    }

    public void showShopDetails(){
        try{
            Shop  shop = getIntent().getExtras().getParcelable("shop_obj");
            Uri uri;
            try {
                uri = Uri.parse(shop.mShopImgUri);
            }catch (NullPointerException e){
                uri = Utils.getUriToDrawable(this, R.drawable.ic_shops_24dp);
            }

            Glide.with(this).load(uri).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_shops_24dp).into(mShopImage);
            mShopName.setText(shop.mShopName);
            mShopLatCoord.setText("Latitude: " + shop.mShopLatCoord);
            mShopLonCoord.setText("Longitude: " + shop.mShopLonCoord);
            User user = shop.mShopUser;

            if(user != null) {
                mShopUserName.setText("User name: " + user.mUserName);
            }
        }catch(NullPointerException e){
            Toast.makeText(this, "Cant open shop",Toast.LENGTH_LONG);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
