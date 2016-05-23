package com.yanovich.alex.androidmvpsimpleclient.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alex on 27.04.2016.
 */
public class Shop implements Parcelable,Comparable<Shop>{
    public int mId;
    public String mShopName;
    public String mShopImgUri;
    public String mShopLonCoord;
    public String mShopLatCoord;
    public User mShopUser;

    public Shop(){
    }


    public Shop(int mId, String mShopLatCoord, String mShopLonCoord, String mShopImgUri, String mShopName) {
        this.mId = mId;
        this.mShopLatCoord = mShopLatCoord;
        this.mShopLonCoord = mShopLonCoord;
        this.mShopImgUri = mShopImgUri;
        this.mShopName = mShopName;
    }

    protected Shop(Parcel in) {
        this.mId = in.readInt();
        this.mShopName =  in.readString();
        this.mShopImgUri =  in.readString();
        this.mShopLonCoord =  in.readString();
        this.mShopLatCoord = in.readString();
        this.mShopUser = in.readParcelable(User.class.getClassLoader());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mShopName);
        dest.writeString(this.mShopImgUri);
        dest.writeString(this.mShopLonCoord);
        dest.writeString(this.mShopLatCoord);
        dest.writeParcelable(this.mShopUser, 0);
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        public Shop createFromParcel(Parcel source) {
            return new Shop(source);
        }

        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shop)) return false;

        Shop shop = (Shop) o;

        if (mId != shop.mId) return false;
        if (mShopName != null ? !mShopName.equals(shop.mShopName) : shop.mShopName != null)
            return false;
        if (mShopImgUri != null ? !mShopImgUri.equals(shop.mShopImgUri) : shop.mShopImgUri != null)
            return false;
        if (mShopLonCoord != null ? !mShopLonCoord.equals(shop.mShopLonCoord) : shop.mShopLonCoord != null)
            return false;
        return !(mShopLatCoord != null ? !mShopLatCoord.equals(shop.mShopLatCoord) : shop.mShopLatCoord != null);

    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mShopName != null ? mShopName.hashCode() : 0);
        result = 31 * result + (mShopImgUri != null ? mShopImgUri.hashCode() : 0);
        result = 31 * result + (mShopLonCoord != null ? mShopLonCoord.hashCode() : 0);
        result = 31 * result + (mShopLatCoord != null ? mShopLatCoord.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Shop someShop) {
        return mShopName.compareToIgnoreCase(someShop.mShopName);
    }
}
