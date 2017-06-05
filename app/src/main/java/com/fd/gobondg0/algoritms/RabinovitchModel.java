package com.fd.gobondg0.algoritms;

public class RabinovitchModel extends CalculationModel {

    static private final double TIME_STEPS = 100;

    @Override
    public float[] calculateParity(double T, int type, int step) throws NullPointerException{
        if(mArgsStore != null) {
            double tau = mArgsStore.getMaturity();
            double vola = mArgsStore.getVolatility();
            double ba = mArgsStore.getBasicPrice();
            switch (type){
                case PriceCalculator.FOR_MATURITY:
                    tau = T;
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
            double r0 = mArgsStore.getProfitRate();
            double q = mArgsStore.getDividentsYield();
            double kr = mArgsStore.getKr();
            double mur = mArgsStore.getMur();
            double sigmar = mArgsStore.getSigmar();
            double ro = mArgsStore.getRo();

            double e = PriceCalculator.getRandomGaussian();
            double rt = r0;//mArgsStore.getRtInTime(step);//mur + (r0 - mur) * Math.exp(- (kr * tau));// + (sigmar/Math.sqrt(2 * kr)) * Math.sqrt(1 - Math.exp(- (2 * kr * tau))) * PriceCalculator.getRandomGaussian();
            double k = mur + sigmar * MARKET_RISK_RATE/kr - Math.pow((sigmar/kr),2)/2;
            double B = (1 - Math.exp(-(kr * tau)))/ kr;
            double A = Math.exp(k * (B - tau) - Math.pow((sigmar * B/ 2), 2)/kr);
            double P = A * Math.exp(-(rt * B));
            double Tfunc = vola * vola * tau + (tau - 2 * B + (1 - Math.exp(-(2 * kr * tau)))/(2 * kr)) * Math.pow(sigmar/kr,2) - 2 * ro * vola * (tau - B) * (sigmar / kr);
            double d1 = (Math.log(ba/s * P) + Tfunc/2)/Math.sqrt(Tfunc);
             double d2 = d1 - Math.sqrt(Tfunc);

            double nD1 = PriceCalculator.getStandRaspObr(d1);
            double nD2 = PriceCalculator.getStandRaspObr(d2);
            double nD11 = PriceCalculator.getStandRaspObr(-d1);
            double nD22 = PriceCalculator.getStandRaspObr(-d2);
            parity[0] = (float)  (ba * nD1 - s * P * nD2);
            parity[1] = (float) (s * P * nD22 - ba * nD11);
            return parity;
        }else{
            throw new NullPointerException();
        }
    }
}