package algonquin.cst2335.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class ChargeOpenHelper extends SQLiteOpenHelper {
    public static final String name = "Database";
    public static final int version = 1;
    public static final String TABLE_NAME = "ChargeInfo";
    public static final String col_title = "Title";
    public static final String col_latitude = "Latitude";
    public static final String col_longitude = "Longitude";
    public static final String col_telphone = "Tel";

    public ChargeOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + col_title + " TEXT,"
                + col_latitude + " TEXT,"
                + col_longitude + " TEXT,"
                + col_telphone + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
    }
}

