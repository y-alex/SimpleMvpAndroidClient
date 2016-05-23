package com.yanovich.alex.androidmvpsimpleclient.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;

import java.util.ArrayList;
/**
 * Created by Alex on 28.04.2016.
 */
public class DbContract {

    public DbContract() {
    }

    public abstract static class UsersTable implements BaseColumns{
        public static final String TABLE_NAME = "users";

        public static final String USER_FIRST_NAME = "first_name";
        public static final String USER_LAST_NAME = "last_name";
        public static final String USER_ADRESS = "adress";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        USER_FIRST_NAME + " TEXT NOT NULL, " +
                        USER_LAST_NAME + " TEXT NOT NULL, " +
                        USER_ADRESS + " TEXT NOT NULL "  + " ); ";

        public static ContentValues toContentValues(User user) {
            ContentValues values = new ContentValues();
            values.put(_ID, user.mId);
            values.put(USER_FIRST_NAME, user.mUserName );
            values.put(USER_LAST_NAME, user.mUserLastName);
            values.put(USER_ADRESS, user.mUserAdress);
            return values;
        }

        public static User parseCursor(Cursor cursor) {
            User user = new User();
            user.mId = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            user.mUserName = cursor.getString(cursor.getColumnIndex(USER_FIRST_NAME));
            user.mUserLastName = cursor.getString(cursor.getColumnIndex(USER_LAST_NAME));
            user.mUserAdress = cursor.getString(cursor.getColumnIndex(USER_ADRESS));


           return user;
        }
    }

    public abstract static class ShopsTable implements BaseColumns{
        public static final String TABLE_NAME = "shops";

        public static final String SHOP_NAME = "name";
        public static final String SHOP_IMG_URI = "img_uri";
        public static final String SHOP_LON_COORD = "lon_coord";
        public static final String SHOP_LAT_COORD = "lat_coord";
        //Column with the foreign key - reference to users table
        public static final String SHOP_USER_ID = "user_id";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        SHOP_NAME + " TEXT NOT NULL, " +
                        SHOP_IMG_URI +" TEXT NOT NULL, " +
                        SHOP_LON_COORD + " TEXT NOT NULL, " +
                        SHOP_LAT_COORD + " TEXT NOT NULL, " +
                        SHOP_USER_ID + " INTEGER NOT NULL, " +
                        " FOREIGN KEY (" + SHOP_USER_ID + ") REFERENCES " +
                        UsersTable.TABLE_NAME + " (" + UsersTable._ID + ") " + " );";




        public static final String QUERY_GET_SHOPS = "SELECT " +
                TABLE_NAME+"."+_ID +
                ", "+TABLE_NAME+"."+SHOP_NAME+
                ", "+ TABLE_NAME + "." + SHOP_IMG_URI +
                ", "+TABLE_NAME +"." +SHOP_LON_COORD +
                ", "+ TABLE_NAME +"." + SHOP_LAT_COORD +

                ", "+ TABLE_NAME +"." + SHOP_USER_ID +

               ", "+ UsersTable.TABLE_NAME +"." + UsersTable.USER_FIRST_NAME +
                ", "+ UsersTable.TABLE_NAME +"." + UsersTable.USER_LAST_NAME +
                ", "+ UsersTable.TABLE_NAME +"." + UsersTable.USER_ADRESS +
                " FROM " +
                UsersTable.TABLE_NAME + " INNER JOIN " + TABLE_NAME + " ON "+ UsersTable.TABLE_NAME +
                "." + UsersTable._ID + " = " +TABLE_NAME +"." + SHOP_USER_ID ;

        public static final String QUERY_GET_SHOPS_BY_USER = QUERY_GET_SHOPS + " WHERE " +
                TABLE_NAME +"." + SHOP_USER_ID+ " = ?";


        public static ContentValues toContentValues(Shop shop, long userId) {
            ContentValues values = new ContentValues();
            values.put(_ID, shop.mId);
            values.put(SHOP_NAME, shop.mShopName );
            values.put(SHOP_IMG_URI, shop.mShopImgUri);
            values.put(SHOP_LON_COORD, shop.mShopLonCoord);
            values.put(SHOP_LAT_COORD, shop.mShopLatCoord);
            values.put(SHOP_USER_ID, userId);
            return values;
        }

        public static Shop parseCursor(Cursor cursor) {
            Shop shop = new Shop();
            shop.mId = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            shop.mShopName = cursor.getString(cursor.getColumnIndex(SHOP_NAME));
            shop.mShopImgUri = cursor.getString(cursor.getColumnIndex(SHOP_IMG_URI));
            shop.mShopLonCoord = cursor.getString(cursor.getColumnIndex(SHOP_LON_COORD));
            shop.mShopLatCoord = cursor.getString(cursor.getColumnIndex(SHOP_LAT_COORD));

            User user = new User();
            user.mId = cursor.getInt(cursor.getColumnIndexOrThrow(SHOP_USER_ID));
            user.mUserName = cursor.getString(cursor.getColumnIndex(UsersTable.USER_FIRST_NAME));
            user.mUserLastName = cursor.getString(cursor.getColumnIndex(UsersTable.USER_LAST_NAME));
            user.mUserAdress = cursor.getString(cursor.getColumnIndex(UsersTable.USER_ADRESS));

            shop.mShopUser = user;
            return shop;
        }

    }


}
