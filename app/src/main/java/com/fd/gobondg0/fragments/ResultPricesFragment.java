package com.fd.gobondg0.fragments;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fd.gobondg0.BaseApp;
import com.fd.gobondg0.ForecastResult;
import com.fd.gobondg0.R;
import com.fd.gobondg0.ResultDetailedActivity;
import com.fd.gobondg0.algoritms.PriceCalculator;

import java.util.Map;

public class ResultPricesFragment extends ResultDetailedActivity.ResultFragment {

    private ResultDetailedActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calculation_result_fragment, container, false);
        TextView bscall = (TextView) rootView.findViewById(R.id.bsCallOptionPrice);
        TextView bsput = (TextView) rootView.findViewById(R.id.bsPutOptionPrice);
        TextView mertcall = (TextView) rootView.findViewById(R.id.mertonCallOptionPrice);
        TextView mertput = (TextView) rootView.findViewById(R.id.mertonPutOptionPrice);
        TextView rabcall = (TextView) rootView.findViewById(R.id.rabiCallOptionPrice);
        TextView rabput = (TextView) rootView.findViewById(R.id.rabiPutOptionPrice);
        TextView bsLabel = (TextView) rootView.findViewById(R.id.bsModelText);
        TextView mertLabel = (TextView) rootView.findViewById(R.id.mertonModelText);
        TextView rabLabel = (TextView) rootView.findViewById(R.id.rabiModelText);

        Map<String, ForecastResult> results = mContext.getForecastsResults();

        for(String type : results.keySet()){
            if(results.get(type) != null){
                ForecastResult res = results.get(type);
                switch (type){
                    case "BS":
                        bscall.setVisibility(View.VISIBLE);
                        bsput.setVisibility(View.VISIBLE);
                        bsLabel.setVisibility(View.VISIBLE);
                        startPriceAnimation(bscall, res.mCallPrice);
                        startPriceAnimation(bsput, res.mPutPrice);
                        break;
                    case "Merton":
                        mertcall.setVisibility(View.VISIBLE);
                        mertput.setVisibility(View.VISIBLE);
                        mertLabel.setVisibility(View.VISIBLE);
                        startPriceAnimation(mertcall, res.mCallPrice);
                        startPriceAnimation(mertput, res.mPutPrice);
                        break;
                    case "Rabinovitch":
                        rabcall.setVisibility(View.VISIBLE);
                        rabput.setVisibility(View.VISIBLE);
                        rabLabel.setVisibility(View.VISIBLE);
                        startPriceAnimation(rabcall, res.mCallPrice);
                        startPriceAnimation(rabput, res.mPutPrice);
                        break;
                }
            }
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        mContext = (ResultDetailedActivity) context;
        super.onAttach(context);
    }

    public void startPriceAnimation(final TextView view, float price){
        ValueAnimator animator = ValueAnimator.ofFloat(0, price);
        animator.setDuration(1000);
        view.setText(0.0 + "");
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setText(String.format("%.1f", (Float)animation.getAnimatedValue()));
            }
        });
        animator.start();

    }
}
