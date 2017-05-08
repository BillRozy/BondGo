package com.fd.gobondg0.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import com.fd.gobondg0.ForecastEntity;

import java.util.ArrayList;

public class ForecastsReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ForecastsReader.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATETIME";
    private static final String FLOAT_TYPE = " REAL";
    private static final String INT_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_FORECAST_TABLE =
            "CREATE TABLE " + ForecastReaderContract.ForecastEntry.TABLE_NAME + " (" +
                    ForecastReaderContract.ForecastEntry._ID + " INTEGER PRIMARY KEY," +
                    ForecastReaderContract.ForecastEntry.FORECAST_NAME + TEXT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_TYPE + TEXT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_TIMESTAMP + DATE_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_MATURITY + FLOAT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_VOLATILITY + FLOAT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_BASIC_PRICE + FLOAT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_STRIKE_PRICE + FLOAT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_INTEREST_RATE + FLOAT_TYPE + COMMA_SEP +
                    ForecastReaderContract.ForecastEntry.FORECAST_DIVIDENDS_YIELD + FLOAT_TYPE +
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

        values.put(ForecastReaderContract.ForecastEntry.FORECAST_NAME, "Example 1");
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_TYPE, "BS");
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_TIMESTAMP, "2017-05-05 21:17:00");
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_MATURITY, 364);
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_VOLATILITY, 20.1f);
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_BASIC_PRICE, 80);
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_STRIKE_PRICE, 100);

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ForecastReaderContract.ForecastEntry.TABLE_NAME,
                null,
                values);
        System.out.println("I seed my seeds! " + newRowId);
    }

    public static ArrayList<ForecastEntity> fetchForecastEntities(SQLiteDatabase db){
        ArrayList<ForecastEntity> res = new ArrayList<>();
        String[] projection = {
                ForecastReaderContract.ForecastEntry._ID,
                ForecastReaderContract.ForecastEntry.FORECAST_NAME,
                ForecastReaderContract.ForecastEntry.FORECAST_TYPE,
                ForecastReaderContract.ForecastEntry.FORECAST_TIMESTAMP,
                ForecastReaderContract.ForecastEntry.FORECAST_BASIC_PRICE,
                ForecastReaderContract.ForecastEntry.FORECAST_STRIKE_PRICE,
                ForecastReaderContract.ForecastEntry.FORECAST_MATURITY,
                ForecastReaderContract.ForecastEntry.FORECAST_VOLATILITY,
                ForecastReaderContract.ForecastEntry.FORECAST_INTEREST_RATE,
                ForecastReaderContract.ForecastEntry.FORECAST_DIVIDENDS_YIELD
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                ForecastReaderContract.ForecastEntry.FORECAST_TIMESTAMP + " DESC";

        Cursor c = db.query(
                ForecastReaderContract.ForecastEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        while(c.moveToNext()){
            long itemId = c.getLong(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry._ID));
            String name = c.getString(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry.FORECAST_NAME));
            String type = c.getString(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry.FORECAST_TYPE));
            String date = c.getString(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry.FORECAST_TIMESTAMP));
            float ba = c.getFloat(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry.FORECAST_BASIC_PRICE));
            float s = c.getFloat(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry.FORECAST_STRIKE_PRICE));
            float t = c.getFloat(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry.FORECAST_MATURITY));
            float vola = c.getFloat(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry.FORECAST_VOLATILITY));
            Float r = c.getFloat(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry.FORECAST_INTEREST_RATE));
            Float q = c.getFloat(c.getColumnIndexOrThrow(ForecastReaderContract.ForecastEntry.FORECAST_DIVIDENDS_YIELD));
            res.add(new ForecastEntity(name,type,date, vola, t ,ba, s, r, q));
        }
        return res;
    }

    static public void saveForecastEntity(ForecastEntity entity, SQLiteDatabase db){

        ContentValues values = new ContentValues();
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_NAME, entity.getName());
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_TYPE, entity.getType());
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_TIMESTAMP, entity.getDate());
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_MATURITY, entity.getMaturity());
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_VOLATILITY, entity.getVolatility());
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_BASIC_PRICE, entity.getBasicPrice());
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_STRIKE_PRICE, entity.getStrikePrice());
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_INTEREST_RATE, entity.getInterestRate());
        values.put(ForecastReaderContract.ForecastEntry.FORECAST_DIVIDENDS_YIELD, entity.getDividentsYield());

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ForecastReaderContract.ForecastEntry.TABLE_NAME,
                null,
                values);
        System.out.println("Put new forecast in db " + newRowId);
    }

    static public boolean deleteForecastEntry(ForecastEntity entity, SQLiteDatabase db){
        // Define 'where' part of query.
        String selection = ForecastReaderContract.ForecastEntry.FORECAST_TIMESTAMP + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(entity.getDate()) };
        // Issue SQL statement.
        return (db.delete(ForecastReaderContract.ForecastEntry.TABLE_NAME, selection, selectionArgs) > 0);
    }

    static public boolean deleteAllForecastEntries(SQLiteDatabase db){
        db.execSQL("DELETE FROM " + ForecastReaderContract.ForecastEntry.TABLE_NAME);
        return true;
    }
}
