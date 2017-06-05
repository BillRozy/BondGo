package com.fd.gobondg0.algoritms;


import com.fd.gobondg0.ForecastEntity;
import com.fd.gobondg0.ForecastResult;

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
    private double mProfitRate = 0.05;
    private double mDividentsYield = 0.001;
    private double mKr = 0.1;
    private double mMur = 0.1;
    private double mSigmar = 0.09;
    private double mRo = 1;
    private double[] mRt = new double[100];

    private CalculationModel mCurrentModel;

    public PriceCalculator(CalculationModel model){
        mCurrentModel = model;
        mCurrentModel.setArgsStore(this);
    }

    public void performCalculation(int type){
       float[] res = mCurrentModel.calculateParity();
        mCallPrice = res[0];
        mPutPrice = res[1];
    }

    public ForecastResult calculateAndReturnFullResult(){
        ForecastResult res = new ForecastResult();
        mRt = calculateRt(mProfitRate, 2 * mMaturity, mKr, mMur, mSigmar, 100);
        performCalculation(PriceCalculator.FOR_MATURITY);
        ArrayList<float[]> forecastMatur = calculateForecast(0, 2 * mMaturity, 100, PriceCalculator.FOR_MATURITY);
        ArrayList<float[]> forecastVola = calculateForecast(0, 2 * mVolatility, 100, PriceCalculator.FOR_VOLATILITY);
        ArrayList<float[]> forecastBa = calculateForecast(0, 2 * mBasicPrice, 100, PriceCalculator.FOR_BASIC_PRICE);
        res.mCallPrice = (float) mCallPrice;
        res.mPutPrice = (float) mPutPrice;
        res.mMaturityCalls = forecastMatur.get(0);
        res.mMaturityPuts = forecastMatur.get(1);
        res.mVolaCalls = forecastVola.get(0);
        res.mVolaPuts = forecastVola.get(1);
        res.mStockCalls = forecastBa.get(0);
        res.mStockPuts = forecastBa.get(1);
        return res;
    }



    public ArrayList<float[]> calculateForecast(double startT, double endT, int steps, int type){
        ArrayList<float[]> forecast = new ArrayList<>();
        double diff = endT - startT;
        double step = diff/steps;
        float[] calls = new float[steps];
        float[] puts = new float[steps];
        for(int i = 0; i < steps; i++){
            float[] res = mCurrentModel.calculateParity(startT + i * step, type, i);
            calls[i] = res[0];
            puts[i] = res[1];
        }

        forecast.add(calls);
        forecast.add(puts);

        return forecast;
    }

    public void applyForecastEntity(ForecastEntity entity){
        setBasicPrice(entity.getBasicPrice());
        setStrikePrice(entity.getStrikePrice());
        setMaturity(entity.getMaturity());
        setVolatility(entity.getVolatility());
        setProfitRate(entity.getInterestRate());
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
        this.mProfitRate = mProfitRate/100;
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

    static double getRandomGaussian(){
        return new NormalDistribution(0,1).sample();
    }

    public double getKr() {
        return mKr;
    }

    public double getMur() {
        return mMur;
    }

    public double getSigmar() {
        return mSigmar;
    }

    public double getRo() {
        return mRo;
    }

    public void setModel(CalculationModel model){
        mCurrentModel = model;
        mCurrentModel.setArgsStore(this);
    }

    public double getRtInTime(int step){
        return mRt[step];
    }

    static private double[] calculateRt(double r0, double t, double meanreverse, double equlib, double vola, int steps){
        double res[] = new double[steps];
        double step = t/steps;
        Random randomno = new Random();
        res[0] = r0;
        System.out.println("Rs!");
        for(int i = 1; i < steps; i++){
            res[i] = res[i-1] + meanreverse * (equlib - r0) * step + vola * randomno.nextGaussian() * Math.sqrt(step);
            System.out.print(res[i] + "  ");
        }
        return res;
    }
}
