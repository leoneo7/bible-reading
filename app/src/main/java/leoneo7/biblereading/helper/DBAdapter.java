package leoneo7.biblereading.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ryouken on 2016/09/24.
 */
public class DBAdapter {

    static final String DATABASE_NAME = "bible_reading.db";
    static final int DATABASE_VERSION = 6;

    // スプリントのログ
    public static final String SPRINT_LOG = "sprint_log";
    public static final String SPRINT_ID = "sprint_id";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";

    // 個々のログ
    public static final String READ_LOG = "read_log";
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
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL( "CREATE TABLE " + SPRINT_LOG + " ("
                    + SPRINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + START_DATE + " INTEGER NOT NULL UNIQUE,"
                    + END_DATE + " INTEGER NOT NULL);");
            db.execSQL( "CREATE TABLE " + READ_LOG + " ("
                    + LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + LOG_DATE + " INTEGER NOT NULL UNIQUE,"
                    + LOG_CHAPTERS + " INTEGER NOT NULL,"
                    + SPRINT_ID + " INTEGER NOT NULL,"
                    + "FOREIGN KEY" + "(" + SPRINT_ID + ")"
                    + "REFERENCES " + SPRINT_LOG + "(" + SPRINT_ID + ")"
                    + ");");
        }

        @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.execSQL("DROP TABLE IF EXISTS " + SPRINT_LOG);
            db.execSQL("DROP TABLE IF EXISTS " + READ_LOG);
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

    public Cursor getSprintLog(){
        String sql = "SELECT * from sprint_log order by start_date asc";
        return db.rawQuery(sql, null);
    }

    public Cursor getCurrentSprintId(){
        String sql = "SELECT sprint_id from sprint_log WHERE sprint_id = (select max(sprint_id) from sprint_log);";
        return db.rawQuery(sql, null);
    }

    public Cursor getReadLog(){
        String sql = "SELECT * from read_log order by log_date asc;";
        return db.rawQuery(sql, null);
    }

    public Cursor getReadLogInCurrentSprint(int currentSprintId){
        String sql = "SELECT * from read_log WHERE sprint_id = " + String.valueOf(currentSprintId) + ";";
        return db.rawQuery(sql, null);
    }

    public void saveSprint(long startDate, long endDate){
        Log.d("saveSprint", String.valueOf(startDate));
        ContentValues values = new ContentValues();
        values.put(START_DATE, startDate);
        values.put(END_DATE, endDate);
        db.insertOrThrow(SPRINT_LOG, null, values);
    }

    public void saveLog(long date, int chapters, int sprintId){
        Log.d("saveLog", String.valueOf(date));
        ContentValues values = new ContentValues();
        values.put(LOG_DATE, date);
        values.put(LOG_CHAPTERS, chapters);
        values.put(SPRINT_ID, sprintId);
        db.insertOrThrow(READ_LOG, null, values);
    }

    public void updateLog(int id, long date, int chapters, int sprintId){
        Log.d("updateLog", String.valueOf(date));
        ContentValues values = new ContentValues();
        values.put(LOG_DATE, date);
        values.put(LOG_CHAPTERS, chapters);
        values.put(SPRINT_ID, sprintId);
        db.update(READ_LOG, values, "log_id = " + String.valueOf(id), null);
    }

}