package com.fd.gobondg0.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fd.gobondg0.ForecastResult;
import com.fd.gobondg0.ForecastView;
import com.fd.gobondg0.PlotLegend;
import com.fd.gobondg0.PlotLegendAdapter;
import com.fd.gobondg0.R;
import com.fd.gobondg0.ResultDetailedActivity;
import com.fd.gobondg0.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract public class AxedFragment extends ResultDetailedActivity.ResultFragment {

    protected static final int FORECAST_STEPS = 100;
    protected static Map<String, int[]> mColors;
    protected ResultDetailedActivity mContext;
    protected float forecastVar;
    protected float[] prices;
    protected float[] calls;
    protected float[] puts;
    protected String[] axisLegend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_axis, container, false);
        ForecastView graphicalCanvas = (ForecastView) rootView.findViewById(R.id.forecastGraphics);
        PlotLegend legendary = (PlotLegend) rootView.findViewById(R.id.legendGraphics);
        mColors = new HashMap<>();
        mColors.put("BS", new int[]{Color.parseColor("#c51162"), Color.parseColor("#2962ff")});
        mColors.put("Merton", new int[]{Color.parseColor("#730838"), Color.parseColor("#00268d")});
        mColors.put("Rabinovitch", new int[]{Color.parseColor("#b80003"), Color.parseColor("#8000ff")});

        Map<String, ForecastResult> results = mContext.getForecastsResults();
        prepareData(graphicalCanvas, results);
        buildForecast(graphicalCanvas, legendary, results);

        return rootView;
    }

    abstract protected void buildForecast(ForecastView graphicalCanvas, PlotLegend legendary, Map<String, ForecastResult> results);

    abstract protected void prepareData(ForecastView graphicalCanvas, Map<String, ForecastResult> results);

    @Override
    public void onAttach(Context context) {
        mContext = (ResultDetailedActivity) context;
        super.onAttach(context);
    }

    protected float[] maxAndMin(float[] input){

        float[] res= {input[0], input[0]};

        for (int i = 0; i < input.length; i++) {
            if (input[i] > res[0]) {
                res[0] = input[i];
            }
            if(input[i] < res[1]){
                res[1] = input[i];
            }
        }
        return res;
    }

    protected float min(float[] input){

        float res = input[0];

        for (int i = 0; i < input.length; i++) {
            if(input[i] < res){
                res = input[i];
            }
        }
        return res;
    }

    protected float max(float[] input){

        float res = input[0];

        for (int i = 0; i < input.length; i++) {
            if(input[i] > res){
                res = input[i];
            }
        }
        return res;
    }

    protected float max(ArrayList<float[]> list){
        float res = list.get(0)[0];
        for(float[] arr : list){
            if(max(arr) > res){
                res = max(arr);
            }
        }
        return res;
    }

    protected float min(ArrayList<float[]> list){
        float res = list.get(0)[0];
        for(float[] arr : list){
            if(min(arr) < res){
                res = min(arr);
            }
        }
        return res;
    }
}
