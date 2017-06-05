package com.fd.gobondg0.algoritms;


public abstract class CalculationModel {


    public static double MARKET_RISK_RATE = 0.2;
    protected ArgsStore mArgsStore;

    public void setArgsStore(ArgsStore store){
        mArgsStore = store;
    }

    protected float[] calculateParity() throws NullPointerException{
        return calculateParity(mArgsStore.getMaturity(), PriceCalculator.FOR_MATURITY, 49);
    }

    abstract public float[] calculateParity(double T, int type, int step) throws NullPointerException;

    static public CalculationModel createCalculationModel(String type){
        switch(type){
            case "BS":
                return new BlackScholesModel();
            case "Merton":
                return new MertonModel();
            case "Rabinovitch":
                return new RabinovitchModel();
            default:
                return new BlackScholesModel();
        }
    }

}
