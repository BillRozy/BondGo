package com.fd.gobondg0;

public class ForecastEntity {

    private String mName;
    private String mType;
    private String mDate;
    private float mVolatility;
    private float mMaturity;
    private float mBasicPrice;
    private float mStrikePrice;

    public ForecastEntity(String name, String type, String date, float vola, float t, float ba, float s){
        mName = name;
        mType = type;
        mDate = date;
        mVolatility = vola;
        mMaturity = t;
        mBasicPrice = ba;
        mStrikePrice = s;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getDate() {
        return mDate;
    }

    public float getVolatility() {
        return mVolatility;
    }

    public float getMaturity() {
        return mMaturity;
    }

    public float getBasicPrice() {
        return mBasicPrice;
    }

    public float getStrikePrice() {
        return mStrikePrice;
    }
}
