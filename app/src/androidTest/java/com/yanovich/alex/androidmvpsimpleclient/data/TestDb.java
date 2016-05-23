package com.yanovich.alex.androidmvpsimpleclient.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.yanovich.alex.androidmvpsimpleclient.data.local.DbContract;
import com.yanovich.alex.androidmvpsimpleclient.data.local.DbOpenHelper;

import java.util.HashSet;

/**
 * Created by Alex on 20.05.2016.
 */
public class TestDb extends AndroidTestCase {
    // each test to start with a clean database
    void deleteTheDatabase() {
        mContext.deleteDatabase(DbOpenHelper.DATABASE_NAME);
    }

    /*
       This function gets called before each test is executed to delete the database.
    */
    public void setUp() {
        deleteTheDatabase();
    }


    public void testCreateDb() throws Throwable {
        // HashSet of all of the table names we wish to look for
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(DbContract.UsersTable.TABLE_NAME);
        tableNameHashSet.add(DbContract.ShopsTable.TABLE_NAME);

        mContext.deleteDatabase(DbOpenHelper.DATABASE_NAME);
        SQLiteDatabase db = new DbOpenHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // check tables created
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both tables
        assertTrue("Error: Your database was created without both tables",
                tableNameHashSet.isEmpty());

        // check columns users table
        c = db.rawQuery("PRAGMA table_info(" + DbContract.UsersTable.TABLE_NAME + ")",null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> userColumnHashSet = new HashSet<String>();
        userColumnHashSet.add(DbContract.UsersTable._ID);
        userColumnHashSet.add(DbContract.UsersTable.USER_FIRST_NAME);
        userColumnHashSet.add(DbContract.UsersTable.USER_LAST_NAME);
        userColumnHashSet.add(DbContract.UsersTable.USER_ADRESS);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            userColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that user database doesn't contain all  columns
        assertTrue("Error: The users table doesn't contain all columns",
                userColumnHashSet.isEmpty());


        // check columns users table
        c = db.rawQuery("PRAGMA table_info(" + DbContract.ShopsTable.TABLE_NAME + ")",null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());
        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> shopsColumnHashSet = new HashSet<String>();
        userColumnHashSet.add(DbContract.ShopsTable._ID);
        userColumnHashSet.add(DbContract.ShopsTable.SHOP_NAME);
        userColumnHashSet.add(DbContract.ShopsTable.SHOP_LAT_COORD);
        userColumnHashSet.add(DbContract.ShopsTable.SHOP_LON_COORD);
        userColumnHashSet.add(DbContract.ShopsTable.SHOP_IMG_URI);
        userColumnHashSet.add(DbContract.ShopsTable.SHOP_USER_ID);

        do {
            String columnName = c.getString(columnNameIndex);
            userColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that shops database doesn't contain all  columns
        assertTrue("Error: The users table doesn't contain all columns",
                userColumnHashSet.isEmpty());

        db.close();
    }

    public void testUserTable() {
        insertUser();
    }

    public void testShopsTable() {

        long userRowId = insertUser();
        assertFalse("Error: User Not Inserted Correctly", userRowId == -1L);

        SQLiteDatabase db = new DbOpenHelper(this.mContext).getWritableDatabase();
        ContentValues shopValues = TestUtilities.createShopValues(userRowId);
        //insert shop
        long shopRowId = db.insert(DbContract.ShopsTable.TABLE_NAME, null, shopValues);
        assertTrue(shopRowId != -1);
        //get cursor
        Cursor shopCursor = db.query(
                DbContract.ShopsTable.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from shop query", shopCursor.moveToFirst() );
        //Validate the location Query
        TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                shopCursor, shopValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from shop query",
                shopCursor.moveToNext());

        shopCursor.close();
        db.close();


    }


    public long insertUser(){
        SQLiteDatabase db = new DbOpenHelper(this.mContext).getWritableDatabase();

        ContentValues testValues = TestUtilities.createUserValues();

        //insert to db
        long userRowId = db.insert(DbContract.UsersTable.TABLE_NAME, null, testValues);
        assertTrue(userRowId != -1);

        Cursor cursor = db.query(
                DbContract.UsersTable.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue( "Error: No Records returned from users query", cursor.moveToFirst() );

        //Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Error: Users Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from location query",
                cursor.moveToNext());

            cursor.close();
        db.close();
        return userRowId;
    }
}



























