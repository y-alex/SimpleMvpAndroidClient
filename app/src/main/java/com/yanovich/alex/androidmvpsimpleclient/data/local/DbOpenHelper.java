package com.yanovich.alex.androidmvpsimpleclient.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yanovich.alex.androidmvpsimpleclient.injection.ApplicationContext;

import javax.inject.Inject;

/**
 * Created by Alex on 28.04.2016.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "androidmvpsimpleclient.db";
    public static final int DATABASE_VERSION = 1;

    @Inject
    public DbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            //Uncomment line below if you want to enable foreign keys
            //db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL(DbContract.UsersTable.CREATE);
            db.execSQL(DbContract.ShopsTable.CREATE);
            //Add other tables here
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                //  db.execSQL("Some statement");
            case 2:
                //  db.execSQL("Some statement");

        }

    }
}
