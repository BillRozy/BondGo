package com.fd.gobondg0.algoritms;

public class RabinovitchModel extends CalculationModel {

    @Override
    public float[] calculateParity(double T, int type) throws NullPointerException{
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

            double B = (1 - Math.exp(- (kr * tau)))/kr;
            double k = mur + sigmar * MARKET_RISK_RATE / kr - Math.pow(sigmar/kr, 2)/2;
            double A = Math.exp(k * (B - tau) - Math.pow(sigmar * B / 2, 2)/kr);
            double P = A * Math.exp(- (r0 * B));
            double Tfun = (vola * vola * tau) + (tau - 2 * B + (1 - Math.exp(-2 * kr * tau)/ (2 * kr))) * Math.pow(sigmar/kr, 2) - 2 * ro * vola * (tau - B) * sigmar/kr;
            double d1 = (Math.log((ba/s) * P) + Tfun/2)/Math.sqrt(Tfun);
            double d2 = d1 - Math.sqrt(Tfun);

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