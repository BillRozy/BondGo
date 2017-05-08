package com.fd.gobondg0.db;


import android.provider.BaseColumns;

public class ForecastReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ForecastReaderContract() {}

    /* Inner class that defines the table contents */
//    public static abstract class ForecastEntry implements BaseColumns {
//        public static final String TABLE_NAME = "forecast";
//        public static final String FORECAST_NAME = "forecast_name";
//        public static final String FORECAST_TYPE = "forecast_type";
//        public static final String FORECAST_TIMESTAMP = "forecast_timestamp";
//        public static final String FORECAST_CURVES_IDS = "forecast_curves_ids";
//        public static final String FORECAST_POINTS_IDS = "forecast_points_ids";
//        public static final String FORECAST_AXIS_X = "forecast_axis_x";
//        public static final String FORECAST_AXIS_Y = "forecast_axis_y";
//    }

        public static abstract class ForecastEntry implements BaseColumns {
        public static final String TABLE_NAME = "forecast";
        public static final String FORECAST_NAME = "forecast_name";
        public static final String FORECAST_TYPE = "forecast_type";
        public static final String FORECAST_TIMESTAMP = "forecast_timestamp";
        public static final String FORECAST_MATURITY = "forecast_maturity";
        public static final String FORECAST_VOLATILITY = "forecast_volatility";
        public static final String FORECAST_BASIC_PRICE = "forecast_basic_price";
        public static final String FORECAST_STRIKE_PRICE = "forecast_strike_price";
        public static final String FORECAST_INTEREST_RATE = "forecast_interest_rate";
        public static final String FORECAST_DIVIDENDS_YIELD = "forecast_dividends_yield";
    }

    /* Inner class that defines the table contents */
    public static abstract class CurveEntry implements BaseColumns {
        public static final String TABLE_NAME = "curve";
        public static final String CURVE_NAME = "curve_name";
        public static final String CURVE_TIMESTAMP = "curve_timestamp";
        public static final String CURVE_COLOR = "curve_color";
        public static final String CURVE_WIDTH = "curve_width";
        public static final String CURVE_X_ARRAY = "curve_x_array";
        public static final String CURVE_Y_ARRAY = "curve_y_array";
    }
}
