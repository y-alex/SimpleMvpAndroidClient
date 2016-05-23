package com.yanovich.alex.androidmvpsimpleclient.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.yanovich.alex.androidmvpsimpleclient.data.local.DbContract;

import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Alex on 21.05.2016.
 */
public class TestUtilities extends AndroidTestCase {

    static ContentValues createUserValues(){
        ContentValues testValues = new ContentValues();
        int randomUserId =  new Random().nextInt(9) + 1;

        testValues.put(DbContract.UsersTable.USER_FIRST_NAME, "UserFirstName"+randomUserId);
        testValues.put(DbContract.UsersTable.USER_LAST_NAME, "UserLastName"+randomUserId);
        testValues.put(DbContract.UsersTable.USER_ADRESS, "UserAdress" + randomUserId);

        return testValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createShopValues(long userRowId) {

        ContentValues shopValues = new ContentValues();
        int randomShopId =  new Random().nextInt(9) + 1;
        shopValues.put(DbContract.ShopsTable.SHOP_NAME, "ShopName" + randomShopId);
        shopValues.put(DbContract.ShopsTable.SHOP_LAT_COORD, "22.5485");
        shopValues.put(DbContract.ShopsTable.SHOP_LON_COORD, "22.5485");
        shopValues.put(DbContract.ShopsTable.SHOP_IMG_URI, "ShopUri" + randomShopId);
        shopValues.put(DbContract.ShopsTable.SHOP_USER_ID, userRowId);


        return shopValues;
    }
}
