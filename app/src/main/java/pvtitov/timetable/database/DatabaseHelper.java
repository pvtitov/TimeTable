package pvtitov.timetable.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import pvtitov.timetable.database.StationsContract.Entry;

/**
 * Created by pavel on 01.04.18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "StationsDatabase.db";

    private static final String COLUMNS_COMMON_PART = new String(
            new StringBuilder().
                    append(" (").append(Entry._ID).append(" INTEGER PRIMARY KEY,").
                    append(Entry.COLUMN_CITY_COUNTRY).append(" TEXT").
                    append(Entry.COLUMN_CITY_DISTRICT).append(" TEXT").
                    append(Entry.COLUMN_CITY_ID).append(" INTEGER").
                    append(Entry.COLUMN_CITY_TITLE).append(" TEXT").
                    append(Entry.COLUMN_CITY_REGION).append(" TEXT").
                    append(Entry.COLUMN_CITY_LATITUDE).append(" REAL").
                    append(Entry.COLUMN_CITY_LONGITUDE).append(" REAL").
                    append(Entry.COLUMN_STATION_COUNTRY).append(" TEXT").
                    append(Entry.COLUMN_STATION_COUNTRY).append(" TEXT").
                    append(Entry.COLUMN_STATION_DISTRICT).append(" TEXT").
                    append(Entry.COLUMN_STATION_CITY_ID).append(" INTEGER").
                    append(Entry.COLUMN_STATION_CITY_TITLE).append(" TEXT").
                    append(Entry.COLUMN_STATION_REGION).append(" TEXT").
                    append(Entry.COLUMN_STATION_LATITUDE).append(" REAL").
                    append(Entry.COLUMN_STATION_LONGITUDE).append(" REAL").
                    append(Entry.COLUMN_STATION_ID).append(" INTEGER").
                    append(Entry.COLUMN_STATION_TITLE).append(" TEXT")
    );

    private static final String CREATE_TABLE_CITIES_TO = new String(
            new StringBuilder().append("CREATE TABLE ").append(Entry.TABLE_CITIES_TO)
            .append(COLUMNS_COMMON_PART)
    );

    private static final String CREATE_TABLE_CITIES_FROM = new String(
            new StringBuilder().append("CREATE TABLE ").append(Entry.TABLE_CITIES_FROM)
                    .append(COLUMNS_COMMON_PART)
    );

    private static final String DELETE_TABLE_CITIES_TO = new String(
            new StringBuilder().append("DROP TABLE IF EXISTS ").append(Entry.TABLE_CITIES_TO)
    );

    private static final String DELETE_TABLE_CITIES_FROM = new String(
            new StringBuilder().append("DROP TABLE IF EXISTS ").append(Entry.TABLE_CITIES_FROM)
    );

        public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CITIES_TO);
        db.execSQL(CREATE_TABLE_CITIES_FROM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("database", "Database upgrades. Old data will be discarded.");
        db.execSQL(DELETE_TABLE_CITIES_TO);
        db.execSQL(DELETE_TABLE_CITIES_FROM);
        onCreate(db);
    }
}
