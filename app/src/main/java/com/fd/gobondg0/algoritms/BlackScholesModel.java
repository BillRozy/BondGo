package com.fd.gobondg0.algoritms;

public class BlackScholesModel extends CalculationModel {

    @Override
    public void setArgsStore(ArgsStore store) {
        mArgsStore = store;
    }

    @Override
    public float[] calculateParity() throws NullPointerException{
        return calculateParity(mArgsStore.getMaturity());
    }

    @Override
    public float[] calculateParity(double T) throws NullPointerException{
        if(mArgsStore != null) {
            float[] parity = new float[2];
            double ba = mArgsStore.getBasicPrice();
            double s = mArgsStore.getStrikePrice();
            double t = T;
            double vola = mArgsStore.getVolatility();
            double r = mArgsStore.getProfitRate();
            double d1 = (Math.log(ba / s) + 0.5 * vola * vola * t) / (vola * Math.pow(t, 0.5));
            double d2 = d1 - vola * Math.pow(t, 0.5);
            double nD1 = PriceCalculator.getStandRaspObr(d1);
            double nD2 = PriceCalculator.getStandRaspObr(d2);
            double nD11 = PriceCalculator.getStandRaspObr(-d1);
            double nD22 = PriceCalculator.getStandRaspObr(-d2);
            parity[0] = (float)  (ba * nD1 - s * nD2);
            parity[1] = (float) (s * nD22 - ba * nD11);
            return parity;
        }else{
            throw new NullPointerException();
        }
    }

    public static void main(String... args){
        CalculationModel bsmodel = new BlackScholesModel();
        PriceCalculator pcalc = new PriceCalculator(bsmodel);
        pcalc.setBasicPrice(105000);
        pcalc.setStrikePrice(100000);
        pcalc.setMaturity(364);
        pcalc.setVolatility(10.1);
        float[] res = bsmodel.calculateParity();
        System.out.println("Call: " + res[0] + " , Put: " + res[1]);
    }
}
