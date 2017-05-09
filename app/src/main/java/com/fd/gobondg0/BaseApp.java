package com.fd.gobondg0;

import android.app.Application;

import com.fd.gobondg0.algoritms.BlackScholesModel;
import com.fd.gobondg0.algoritms.PriceCalculator;

public class BaseApp extends Application{

    static private PriceCalculator mPriceCalculator;

    @Override
    public void onCreate() {
        super.onCreate();
        mPriceCalculator = new PriceCalculator(new BlackScholesModel());

    }

    static public PriceCalculator getCalculator(){
        return mPriceCalculator;
    }
}
