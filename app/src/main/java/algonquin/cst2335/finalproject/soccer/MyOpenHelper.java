package algonquin.cst2335.finalproject.soccer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "DatabaseFile";
    public static final int version = 1;
    public static final String TABLE_NAME = "RssItems";
    public static final String id = "_id";
    public static final String title = "title";
    public static final String description = "description";
    public static final String pubDate = "pubDate";
    public static final String url =  "url";
    public static final String imageUrl =  "imageUrl";
    public static final String thumbUrl =  "thumbUrl";

    public MyOpenHelper(Context context) {
        super(context, name, null, version);
        //that's all the constructor does
    }

    //sql creation statement:
    @Override       //sqLiteDatabase interprets SQL commands
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE Table " + TABLE_NAME +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + title + " TEXT,"
                + description + " TEXT,"
                + pubDate + " TEXT,"
                + url + " TEXT,"
                + imageUrl + " TEXT,"
                + thumbUrl + " TEXT);"
        ); //run some SQL


    }//CREATE Table WORDS (_id INTEGER PRIMARY KEY AUTOINCREMENT, Password text, pubDate text);

    @Override
    public void onUpgrade(SQLiteDatabase db, int currentVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME); //delete the table
        onCreate(db);//creating a new one
    }
}


