package com.fd.gobondg0;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultPricesFragment extends ResultDetailedActivity.ResultFragment {

    private ResultDetailedActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calculation_result_fragment, container, false);
        TextView call = (TextView) rootView.findViewById(R.id.callOptionPrice);
        TextView put = (TextView) rootView.findViewById(R.id.putOptionPrice);
        float[] prices = mContext.getPrices();
        call.setText(prices[0] + "");
        put.setText(prices[1] + "");
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        mContext = (ResultDetailedActivity) context;
        super.onAttach(context);
    }
}
