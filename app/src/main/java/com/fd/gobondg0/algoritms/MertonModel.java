package com.fd.gobondg0.algoritms;


public class MertonModel extends CalculationModel {

    @Override
    public void setArgsStore(ArgsStore store) {
        mArgsStore = store;
    }

    @Override
    public float[] calculateParity(int type) throws NullPointerException{
        return calculateParity(mArgsStore.getMaturity(), type);
    }

    @Override
    public float[] calculateParity(double T, int type) throws NullPointerException{
        if(mArgsStore != null) {
            double t = mArgsStore.getMaturity();
            double vola = mArgsStore.getVolatility();
            double ba = mArgsStore.getBasicPrice();
            switch (type){
                case PriceCalculator.FOR_MATURITY:
                    t = T;
                    break;
                case PriceCalculator.FOR_VOLATILITY:
                    vola = T;
                    break;
                case PriceCalculator.FOR_BASIC_PRICE:
                    ba = T;
                    break;
            }
            float[] parity = new float[2];
            double s = mArgsStore.getStrikePrice();
            double r = mArgsStore.getProfitRate();
            double q = mArgsStore.getDividentsYield();
            double d1 = (Math.log(ba / s) + (r - q + vola * vola / 2) * t)/ vola * Math.sqrt(t);
            double d2 = d1 - vola * Math.pow(t, 0.5);
            double nD1 = PriceCalculator.getStandRaspObr(d1);
            double nD2 = PriceCalculator.getStandRaspObr(d2);
            double nD11 = PriceCalculator.getStandRaspObr(-d1);
            double nD22 = PriceCalculator.getStandRaspObr(-d2);
            parity[0] = (float)  (ba * Math.exp(-(q * t)) * nD1 - s * Math.exp(-(r * t)) * nD2);
            parity[1] = (float) (s * Math.exp(-(r * t)) * nD22 - ba * Math.exp(-(q * t)) * nD11);
            return parity;
        }else{
            throw new NullPointerException();
        }
    }
}
