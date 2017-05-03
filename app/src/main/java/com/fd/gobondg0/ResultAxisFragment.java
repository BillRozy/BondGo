package com.fd.gobondg0;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fd.gobondg0.utils.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResultAxisFragment extends ResultDetailedActivity.ResultFragment {

    private ResultDetailedActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_axis, container, false);
        ForecastView graphicalCanvas = (ForecastView) rootView.findViewById(R.id.forecastGraphics);
        graphicalCanvas.setActionBarFix(Utils.dpToPx(48, mContext));
        PlotLegend legendary = (PlotLegend) rootView.findViewById(R.id.legendGraphics);
        float maturity = mContext.getMaturity();
        float[] prices = mContext.getPrices();
        float[] calls = mContext.getCallsForecast();
        float[] puts = mContext.getPutsForecast();
        float[] Ts = new float[100];
        for(int i = 0; i < 100; i++){
            Ts[i] = (maturity * 4)/100 * i;
        }

        graphicalCanvas.createPolyline(Ts, calls, "Call-option", Color.YELLOW);
        graphicalCanvas.createPolyline(Ts, puts, "Put-option", Color.RED);
        graphicalCanvas.createPoint(maturity, prices[0], 20 ,"Your Call", Color.BLUE);
        graphicalCanvas.createPoint(maturity, prices[1], 10 ,"Your Put", Color.BLUE);
        float[] callsMaxMin = maxAndMin(calls);
        float[] putsMaxMin = maxAndMin(puts);
        float[] TsMaxMin = maxAndMin(Ts);
        graphicalCanvas.prepareToDrawCurves(TsMaxMin[1], TsMaxMin[0], Math.min(callsMaxMin[1], putsMaxMin[1]), Math.max(callsMaxMin[0], putsMaxMin[0]));
        graphicalCanvas.setAxisLegend("Maturity, years", "Price, $");
        PlotLegendAdapter adapter = new PlotLegendAdapter(mContext, graphicalCanvas.generateLegendItems());
        legendary.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        mContext = (ResultDetailedActivity) context;
        super.onAttach(context);
    }

    private float[] maxAndMin(float[] input){

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
