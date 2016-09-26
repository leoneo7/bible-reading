package leoneo7.biblereading.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ryouken on 2016/09/24.
 */
public class DBAdapter {

    static final String DATABASE_NAME = "bible_reading.db";
    static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "read_log";
    public static final String LOG_ID = "log_id";
    public static final String LOG_DATE = "log_date";
    public static final String LOG_CHAPTERS = "log_chapters";

    protected final Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION); }

        @Override public void onCreate(SQLiteDatabase db) {
            db.execSQL( "CREATE TABLE " + TABLE_NAME + " ("
                    + LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + LOG_DATE + " INTEGER NOT NULL,"
                    + LOG_CHAPTERS + " INTEGER NOT NULL);");
        }

        @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public Cursor getAll(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void saveLog(long date, int chapters){
        ContentValues values = new ContentValues();
        values.put(LOG_DATE, date);
        values.put(LOG_CHAPTERS, chapters);
        db.insertOrThrow(TABLE_NAME, null, values);
    }
}