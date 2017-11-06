package ai.amnoid.nira;

/**
 * Created by Amith Moorkoth on 10/19/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper/* extends SQLiteOpenHelper */{

/*    public static final String DATABASE_NAME = "gamebot.db";
    public static final String CONTACTS_TABLE_NAME = "b_ball";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int check_table_exixts(String tble_name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tble_name + "'", null);
        int num=cursor.getCount();
        cursor.close();
        db.close();
        return num;
    }

    public String check_table_value(String table,String col_value){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select value from "+table+" where key = '" + col_value + "' ORDER BY random() limit 1", null);
        String val="0";
        if (cursor.moveToFirst()) // data?
            val=cursor.getString(cursor.getColumnIndex("value"));
        cursor.close();
        db.close();
        return val;
    }


    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
*/}
