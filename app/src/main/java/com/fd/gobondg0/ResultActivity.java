package com.fd.gobondg0;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculation_result_fragment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double callPrice = extras.getDouble("call-price");
            TextView callView = (TextView) findViewById(R.id.bsCallOptionPrice);
            callView.setText(callPrice + "");
            double putPrice = extras.getDouble("put-price");
            TextView putView = (TextView) findViewById(R.id.bsPutOptionPrice);
            putView.setText(putPrice + "");
        }
    }
}
