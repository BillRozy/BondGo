package com.fd.gobondg0.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fd.gobondg0.BaseApp;
import com.fd.gobondg0.ForecastResult;
import com.fd.gobondg0.ForecastView;
import com.fd.gobondg0.PlotLegend;
import com.fd.gobondg0.PlotLegendAdapter;
import com.fd.gobondg0.ResultDetailedActivity;

import java.util.ArrayList;
import java.util.Map;

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
    protected void prepareData(ForecastView graphicalCanvas, Map<String, ForecastResult> results) {
        forecastVar = (float) BaseApp.getCalculator().getVolatility();
        float[] Ts = new float[FORECAST_STEPS];
        for(int i = 0; i < FORECAST_STEPS; i++){
            Ts[i] = (forecastVar * 2)/FORECAST_STEPS * i;
        }

        ArrayList<float[]> arrays = new ArrayList<>();

        for(String type : results.keySet()) {
            if (results.get(type) != null) {
                ForecastResult res = results.get(type);
                arrays.add(res.mVolaCalls);
                arrays.add(res.mVolaPuts);
            }
        }

        float[] TsMaxMin = maxAndMin(Ts);
        float min = min(arrays);
        float max = max(arrays);
        graphicalCanvas.prepareToDrawCurves(TsMaxMin[1], TsMaxMin[0], min,max);
        graphicalCanvas.setAxisLegend("Vola, %", "Price, $");
    }

    protected void buildForecast(ForecastView graphicalCanvas, PlotLegend legendary, Map<String, ForecastResult> results) {

        float[] Ts = new float[FORECAST_STEPS];
        for(int i = 0; i < FORECAST_STEPS; i++){
            Ts[i] = (forecastVar * 2)/FORECAST_STEPS * i;
        }

        for(String type : results.keySet()) {
            if (results.get(type) != null) {
                ForecastResult res = results.get(type);
                graphicalCanvas.createPolyline(Ts, res.mVolaCalls, type + " Call", mColors.get(type)[0]);
                graphicalCanvas.createPolyline(Ts, res.mVolaPuts, type + " Put", mColors.get(type)[1]);
                graphicalCanvas.createPoint(forecastVar, res.mCallPrice, 20 ,"Your Call", Color.BLUE);
                graphicalCanvas.createPoint(forecastVar, res.mPutPrice, 10 ,"Your Put", Color.BLUE);
            }
        }

        PlotLegendAdapter adapter = new PlotLegendAdapter(mContext, graphicalCanvas.generateLegendItems());
        legendary.setAdapter(adapter);
    }
}
