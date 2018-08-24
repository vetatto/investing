package ru.vetatto.investing.investing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Виталий on 19.09.2017.
 */

class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "investing";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "investing", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table settings ("
                + "id integer primary key autoincrement,"
                + "token_type text,"
                + "token_expires_in text,"
                + "access_token text,"
                + "refresh_token text,"
                + "date_acess_token"
                +");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
