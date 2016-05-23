package com.yanovich.alex.androidmvpsimpleclient.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 27.04.2016.
 */
public class User implements Parcelable, Comparable<User>{

        public int mId;
        public String mUserName;
        public String mUserLastName;
        public String mUserAdress;
        public List<Shop> mShops = new ArrayList<Shop>();

    public User() {
    }

    public User(int mId, List<Shop> mShops, String mUserName, String mUserLastName, String mUserAdress) {
        this.mId = mId;
        this.mShops = mShops;
        this.mUserName = mUserName;
        this.mUserLastName = mUserLastName;
        this.mUserAdress = mUserAdress;
    }

    public User(Parcel in) {
        this.mId = in.readInt();
        this.mUserName = in.readString();
        this.mUserLastName = in.readString();
        this.mUserAdress = in.readString();
        this.mShops = in.readArrayList(Shop.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mUserName);
        dest.writeString(this.mUserLastName);
        dest.writeString(this.mUserAdress);
        dest.writeList(this.mShops);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };


    @Override
    public int compareTo(User someUser) {
        return mUserName.compareToIgnoreCase(someUser.mUserName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (mId != user.mId) return false;
        if (mUserName != null ? !mUserName.equals(user.mUserName) : user.mUserName != null)
            return false;
        if (mUserLastName != null ? !mUserLastName.equals(user.mUserLastName) : user.mUserLastName != null)
            return false;
        if (mUserAdress != null ? !mUserAdress.equals(user.mUserAdress) : user.mUserAdress != null)
            return false;
        return !(mShops != null ? !mShops.equals(user.mShops) : user.mShops != null);

    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mUserName != null ? mUserName.hashCode() : 0);
        result = 31 * result + (mUserLastName != null ? mUserLastName.hashCode() : 0);
        result = 31 * result + (mUserAdress != null ? mUserAdress.hashCode() : 0);
        result = 31 * result + (mShops != null ? mShops.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "mShops=" + mShops +
                ", mId=" + mId +
                ", mUserName='" + mUserName + '\'' +
                ", mUserLastName='" + mUserLastName + '\'' +
                ", mUserAdress='" + mUserAdress + '\'' +
                '}';
    }
}
