package com.fd.gobondg0.algoritms;


import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.Random;

public class PriceCalculator implements ArgsStore{

    static public final int FOR_VOLATILITY = 0;
    static public final int FOR_MATURITY = 1;
    static public final int FOR_BASIC_PRICE = 2;


    private double mCallPrice = 0;
    private double mPutPrice = 0;

    private double mBasicPrice = 0;
    private double mStrikePrice = 0;
    private double mMaturity = 0;
    private double mVolatility = 0;
    private double mProfitRate = 0;
    private double mDividentsYield = 0.001;
    private CalculationModel mCurrentModel;

    public PriceCalculator(CalculationModel model){
        mCurrentModel = model;
        mCurrentModel.setArgsStore(this);
    }

    public void performCalculation(int type){
       float[] res = mCurrentModel.calculateParity(type);
        mCallPrice = res[0];
        mPutPrice = res[1];
    }



    public ArrayList<float[]> calculateForecast(double startT, double endT, int steps, int type){
        ArrayList<float[]> forecast = new ArrayList<>();
        double diff = endT - startT;
        double step = diff/steps;
        float[] calls = new float[steps];
        float[] puts = new float[steps];
        for(int i = 0; i < steps; i++){
            float[] res = mCurrentModel.calculateParity(startT + i * step, type);
            calls[i] = res[0];
            puts[i] = res[1];
        }

        forecast.add(calls);
        forecast.add(puts);

        return forecast;
    }

    @Override
    public double getBasicPrice() {
        return mBasicPrice;
    }

    @Override
    public double getMaturity() {
        return mMaturity;
    }

    @Override
    public double getProfitRate() {
        return mProfitRate;
    }

    @Override
    public double getStrikePrice() {
        return mStrikePrice;
    }

    @Override
    public double getVolatility() {
        return mVolatility;
    }

    public double getCallPrice() {
        return mCallPrice;
    }

    public double getPutPrice() {
        return mPutPrice;
    }

    public void setBasicPrice(double mBasicPrice) {
        this.mBasicPrice = mBasicPrice;
    }

    public void setStrikePrice(double mStrikePrice) {
        this.mStrikePrice = mStrikePrice;
    }

    public void setMaturity(double mMaturity) {

        this.mMaturity = mMaturity/364;
    }

    public void setVolatility(double mVolatility) {
        this.mVolatility = mVolatility/100;
    }

    public void setProfitRate(double mProfitRate) {
        this.mProfitRate = mProfitRate;
    }

    public double getDividentsYield() {
        return mDividentsYield;
    }

    public void setDividentsYield(double mDividentsYield) {
        this.mDividentsYield = mDividentsYield;
    }

    static double getStandRaspObr(double z){
        return new NormalDistribution().cumulativeProbability(z);
   }

}
