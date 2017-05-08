package com.fd.gobondg0.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fd.gobondg0.ForecastView;
import com.fd.gobondg0.PlotLegend;
import com.fd.gobondg0.PlotLegendAdapter;
import com.fd.gobondg0.R;
import com.fd.gobondg0.ResultDetailedActivity;
import com.fd.gobondg0.utils.Utils;

public class BaAxedFragment extends AxedFragment {

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
        forecastVar = mContext.getBa();
        prices = mContext.getPrices();
        calls = mContext.getCallsBaForecast();
        puts = mContext.getPutsBaForecast();
        axisLegend = new String[]{"Basic Price, $", "Price, $"};
    }
}
