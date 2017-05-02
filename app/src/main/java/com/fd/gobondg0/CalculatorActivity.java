package com.fd.gobondg0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fd.gobondg0.algoritms.BlackScholesModel;
import com.fd.gobondg0.algoritms.CalculationModel;
import com.fd.gobondg0.algoritms.PriceCalculator;

import java.util.ArrayList;

public class CalculatorActivity extends Activity {

    private EditText mBaField;
    private EditText mStrikeField;
    private EditText mMaturityField;
    private EditText mVolatilityField;
    private Button mGoBond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_activity);

        mBaField = (EditText) findViewById(R.id.priceBaField);
        mStrikeField = (EditText) findViewById(R.id.priceStrikeField);
        mMaturityField = (EditText) findViewById(R.id.maturityTimeField);
        mVolatilityField = (EditText) findViewById(R.id.volatilityField);
        mGoBond = (Button) findViewById(R.id.submitForecastButton);

        mGoBond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double ba = Double.parseDouble(mBaField.getText().toString());
                double s = Double.parseDouble(mStrikeField.getText().toString());
                double t = Double.parseDouble(mMaturityField.getText().toString());
                double vola = Double.parseDouble(mVolatilityField.getText().toString());
                CalculationModel bs = new BlackScholesModel();
                PriceCalculator pc = new PriceCalculator(bs);
                pc.setMaturity(t);
                pc.setVolatility(vola);
                pc.setBasicPrice(ba);
                pc.setStrikePrice(s);
                pc.performCalculation();
                ArrayList<float[]> forecast = pc.calculateForecast(0, 4 * pc.getMaturity(), 100);
                Intent resIntent = new Intent(CalculatorActivity.this, ResultDetailedActivity.class);
                resIntent.putExtra("call-price", (float) pc.getCallPrice());
                resIntent.putExtra("put-price", (float) pc.getPutPrice());
                resIntent.putExtra("call-forecast", forecast.get(0));
                resIntent.putExtra("put-forecast", forecast.get(1));
                resIntent.putExtra("maturity", (float) pc.getMaturity());
                startActivity(resIntent);
            }
        });
    }
}
