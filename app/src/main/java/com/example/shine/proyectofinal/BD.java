package com.example.shine.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BD extends SQLiteOpenHelper {

    //Declaracion del nombre de la base de datos
    public static final int DATABASE_VERSION = 1;

    //Declaracion global de la version de la base de datos
    public static final String DATABASE_NAME = "BD";

    //Declaracion del nombre de la tabla
    public static final String BD_TABLE ="BD";

    //sentencia global de creacion de la base de datos
    public static final String BD_TABLE_CREATE = "CREATE TABLE " + BD_TABLE + " (Username TEXT PRIMARY KEY UNIQUE, Password TEXT, BestPoints INTEGER, NotificationMode INTEGER, Avatar TEXT);";



    public BD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BD_TABLE_CREATE);

    }

    //obtener una lista de usuarios
    public Cursor getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(
                BD_TABLE,                               // The table to query
                null,                                   // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                "BestPoints ASC"                        // The sort order
        );
        return c;
    }

    public Cursor getAllinfofromUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"Username", "Password", "BestPoints", "NotificationMode", "Avatar"};
        String[] where = {username};
        Cursor c = db.query(
                BD_TABLE,                                   // The table to query
                columns,                                    // The columns to return
                "Username=?",                               // The columns for the WHERE clause
                where,                                      // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );
        return c;
    }

    public Cursor getUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"Username"};
        String[] where = {username};
        Cursor c = db.query(
                BD_TABLE,                                   // The table to query
                columns,                                    // The columns to return
                "Username=?",                               // The columns for the WHERE clause
                where,                                      // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );
        return c;
    }

    public Cursor getPasswordByUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"Password"};
        String[] where = {username};
        Cursor c = db.query(
                BD_TABLE,                                   // The table to query
                columns,                                    // The columns to return
                "Username=?",                               // The columns for the WHERE clause
                where,                                      // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );
        return c;
    }

    public long setPoints(String username, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {username};
        long p = db.update(
                BD_TABLE,                                   // The table to query
                contentValues,
                "Username=?",                               // The columns for the WHERE clause
                where                                       // The values for the WHERE clause
        );
        return p;
    }

    public long setNotimode(String username, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {username};
        long p = db.update(
                BD_TABLE,                                   // The table to query
                contentValues,
                "Username=?",                               // The columns for the WHERE clause
                where                                       // The values for the WHERE clause
        );
        return p;
    }

    public void createUser (ContentValues values, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(
                tableName,
                null,
                values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public long changeAvatar(String username, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {username};
        long p = db.update(
                BD_TABLE,                                   // The table to query
                contentValues,
                "Username=?",                               // The columns for the WHERE clause
                where                                       // The values for the WHERE clause
        );
        return p;
    }
}