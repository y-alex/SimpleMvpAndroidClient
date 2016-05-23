package com.yanovich.alex.androidmvpsimpleclient.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Alex on 27.04.2016.
 */
@Singleton
public class DatabaseHelper {
    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    /**
     * Remove all the data from all the tables in the database.
     */
    public Observable<Void> clearTables() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    Cursor cursor = mDb.query("SELECT name FROM sqlite_master WHERE type='table'");
                    while (cursor.moveToNext()) {
                        mDb.delete(cursor.getString(cursor.getColumnIndex("name")), null);
                    }
                    cursor.close();
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<User> setUsers(final Collection<User> newUsers) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(DbContract.UsersTable.TABLE_NAME, null);
                    mDb.delete(DbContract.ShopsTable.TABLE_NAME, null);

                    for (User user : newUsers) {
                        long idUser = mDb.insert(DbContract.UsersTable.TABLE_NAME,
                                DbContract.UsersTable.toContentValues(user),
                                SQLiteDatabase.CONFLICT_REPLACE);

                        //add user shops to DB
                        for (Shop shop : user.mShops) {
                            long idShop = mDb.insert(DbContract.ShopsTable.TABLE_NAME,
                                    DbContract.ShopsTable.toContentValues(shop, idUser),
                                    SQLiteDatabase.CONFLICT_REPLACE);
                        }

                        if (idUser >= 0) subscriber.onNext(user);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<User>> getUsers() {
        return mDb.createQuery(DbContract.UsersTable.TABLE_NAME,
                "SELECT * FROM " + DbContract.UsersTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, User>() {
                    @Override
                    public User call(Cursor cursor) {
                        final User user = DbContract.UsersTable.parseCursor(cursor);

                        getShopsByUser(user.mId).subscribe(new Action1<List<Shop>>() {
                            @Override
                            public void call(List<Shop> shops) {
                                user.mShops = shops;
                            }
                        });
                        return user;
                    }
                });
    }

    public Observable<List<Shop>> getShops() {
        return mDb.createQuery(DbContract.ShopsTable.TABLE_NAME,
                DbContract.ShopsTable.QUERY_GET_SHOPS)
                .mapToList(new Func1<Cursor, Shop>() {
                    @Override
                    public Shop call(Cursor cursor) {
                        return DbContract.ShopsTable.parseCursor(cursor);

                    }
                });
    }
    public Observable<List<Shop>> getShopsByUser(long userId){

        return mDb.createQuery(DbContract.UsersTable.TABLE_NAME,
                DbContract.ShopsTable.QUERY_GET_SHOPS_BY_USER, Long.toString(userId))
                .mapToList(new Func1<Cursor, Shop>() {
                    @Override
                    public Shop call(Cursor cursor) {
                        return DbContract.ShopsTable.parseCursor(cursor);

                    }
                });

    }


    public Observable<User> getUserById(long userId) {
        return mDb.createQuery(DbContract.UsersTable.TABLE_NAME,
                "SELECT * FROM " + DbContract.UsersTable.TABLE_NAME + " WHERE " + DbContract.UsersTable._ID + " =?",
                Long.toString(userId)).mapToOne(new Func1<Cursor, User>() {
            @Override
            public User call(Cursor cursor) {
                return DbContract.UsersTable.parseCursor(cursor);
            }
        });

    }

    public Cursor getCursorUserById(long userId){
        return mDb.query( "SELECT * FROM " + DbContract.UsersTable.TABLE_NAME + " WHERE " + DbContract.UsersTable._ID + " =?",  Long.toString(userId));
    }
}
