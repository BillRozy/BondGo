package com.fd.gobondg0.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fd.gobondg0.ResultDetailedActivity;

public class VolatilityAxedFragment extends AxedFragment {

    private ResultDetailedActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        mContext = (ResultDetailedActivity) context;
        super.onAttach(context);
    }

    @Override
    protected void prepareData() {
        forecastVar = mContext.getVolatility();
        prices = mContext.getPrices();
        calls = mContext.getCallsVolaForecast();
        puts = mContext.getPutsVolaForecast();
        axisLegend = new String[]{"Volatility, %", "Price, $"};
    }
}
