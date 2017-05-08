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

abstract public class AxedFragment extends ResultDetailedActivity.ResultFragment {

    protected static final int FORECAST_STEPS = 100;
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

        prepareData();
        buildForecast(graphicalCanvas, legendary);

        return rootView;
    }

    protected void buildForecast(ForecastView graphicalCanvas, PlotLegend legendary){
        float[] Ts = new float[FORECAST_STEPS];
        for(int i = 0; i < FORECAST_STEPS; i++){
            Ts[i] = (forecastVar * 2)/FORECAST_STEPS * i;
        }

        graphicalCanvas.createPolyline(Ts, calls, "Call-option", Color.parseColor("#c51162"));
        graphicalCanvas.createPolyline(Ts, puts, "Put-option", Color.parseColor("#2962ff"));
        graphicalCanvas.createPoint(forecastVar, prices[0], 20 ,"Your Call", Color.BLUE);
        graphicalCanvas.createPoint(forecastVar, prices[1], 10 ,"Your Put", Color.BLUE);
        float[] callsMaxMin = maxAndMin(calls);
        float[] putsMaxMin = maxAndMin(puts);
        float[] TsMaxMin = maxAndMin(Ts);
        graphicalCanvas.prepareToDrawCurves(TsMaxMin[1], TsMaxMin[0], Math.min(callsMaxMin[1], putsMaxMin[1]), Math.max(callsMaxMin[0], putsMaxMin[0]));
        graphicalCanvas.setAxisLegend(axisLegend[0], axisLegend[1]);
        PlotLegendAdapter adapter = new PlotLegendAdapter(mContext, graphicalCanvas.generateLegendItems());
        legendary.setAdapter(adapter);
    }

    abstract protected void prepareData();

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
}
