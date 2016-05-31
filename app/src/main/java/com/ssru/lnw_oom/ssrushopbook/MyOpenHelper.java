package com.ssru.lnw_oom.ssrushopbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pc on 31/5/2559.
 */

public class MyOpenHelper extends SQLiteOpenHelper {

    // Explicit
    public static final String database_name = "Ssru.db";
    private static final int data_version = 1;
    private static final String create_user_table = "create table userTABLE (" +
            "_id integer primary key," +
            "Name text," +
            "Sruname text," +
            "User text," +
            "Password text," +
            "Money text);";

    public MyOpenHelper(Context context) {
        super(context, database_name, null, data_version);


    }   // Constructor

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_user_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}   // Main Class
