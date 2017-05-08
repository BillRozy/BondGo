package com.fd.gobondg0.fragments;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fd.gobondg0.R;
import com.fd.gobondg0.ResultDetailedActivity;

public class ResultPricesFragment extends ResultDetailedActivity.ResultFragment {

    private ResultDetailedActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calculation_result_fragment, container, false);
        TextView call = (TextView) rootView.findViewById(R.id.callOptionPrice);
        TextView put = (TextView) rootView.findViewById(R.id.putOptionPrice);
        float[] prices = mContext.getPrices();
        startPriceAnimation(call, prices[0]);
        startPriceAnimation(put, prices[1]);
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
