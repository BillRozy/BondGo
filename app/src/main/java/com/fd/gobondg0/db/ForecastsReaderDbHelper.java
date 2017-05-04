package com.fd.gobondg0.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

public class ForecastsReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ForecastsReader.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " REAL";
    private static final String INT_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_FORECAST_TABLE =
            "CREATE TABLE " + ForecastReaderContract.ForecastEntry.TABLE_NAME + " (" +
                    ForecastReaderContract.ForecastEntry._ID + " INTEGER PRIMARY KEY," +
                    ForecastReaderContract.ForecastEntry.FORECAST_NAME + TEXT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_TYPE + TEXT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_TIMESTAMP + INT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_CURVES_IDS + TEXT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_POINTS_IDS + TEXT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_AXIS_X + TEXT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_AXIS_Y + TEXT_TYPE +
            " );";
    private static final String SQL_CREATE_CURVE_TABLE =
            "CREATE TABLE " + ForecastReaderContract.CurveEntry.TABLE_NAME + " (" +
            ForecastReaderContract.CurveEntry._ID + " INTEGER PRIMARY KEY," +
            ForecastReaderContract.CurveEntry.CURVE_NAME + TEXT_TYPE + COMMA_SEP +
            ForecastReaderContract.CurveEntry.CURVE_COLOR + TEXT_TYPE + COMMA_SEP +
            ForecastReaderContract.CurveEntry.CURVE_WIDTH + FLOAT_TYPE + COMMA_SEP +
            ForecastReaderContract.CurveEntry.CURVE_TIMESTAMP + INT_TYPE + COMMA_SEP +
            ForecastReaderContract.CurveEntry.CURVE_X_ARRAY + TEXT_TYPE + COMMA_SEP +
            ForecastReaderContract.CurveEntry.CURVE_Y_ARRAY + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ForecastReaderContract.ForecastEntry.TABLE_NAME + " ; \n" +
                    "DROP TABLE IF EXISTS " + ForecastReaderContract.CurveEntry.TABLE_NAME + " ; \n";

    public ForecastsReaderDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FORECAST_TABLE);
        db.execSQL(SQL_CREATE_CURVE_TABLE);
        seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void seed(SQLiteDatabase db){

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ForecastReaderContract.CurveEntry.CURVE_NAME, "call option");
        values.put(ForecastReaderContract.CurveEntry.CURVE_COLOR, Color.YELLOW + "");
        values.put(ForecastReaderContract.CurveEntry.CURVE_TIMESTAMP, System.currentTimeMillis());
        values.put(ForecastReaderContract.CurveEntry.CURVE_WIDTH, 12);
        values.put(ForecastReaderContract.CurveEntry.CURVE_X_ARRAY, "100,200,300,400");
        values.put(ForecastReaderContract.CurveEntry.CURVE_Y_ARRAY, "100,200,300,400");

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ForecastReaderContract.CurveEntry.TABLE_NAME,
                null,
                values);
        System.out.println("I seed my seeds! " + newRowId);
    }
}
