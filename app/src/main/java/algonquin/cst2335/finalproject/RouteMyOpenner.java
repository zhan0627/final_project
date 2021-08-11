package algonquin.cst2335.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database opeoner class to handle database related operations
 */
public class RouteMyOpenner extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "Trivia_DB";
    protected final static int VERSION_NUM = 3;
    public final static String TABLE_NAME = "RouteName";
    public final static String COL_ID = "_id";
    public final static String COL_USERNAME = "COL_USERNAME";
    public final static String COL_DIFFICULTY_LEVEL = "COL_DIFFICULTY_Route";
    public final static String COL_CORRECT_ANSWERS = "COL_CORRECT_ANSWERS";
    public final static String COL_TOTAL = "COL_TOTAL";


    public RouteMyOpenner(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " text,"
                + COL_DIFFICULTY_LEVEL + " text,"
                + COL_CORRECT_ANSWERS + " INTEGER,"
                + COL_TOTAL + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
