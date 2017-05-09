package com.fd.gobondg0;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fd.gobondg0.algoritms.BlackScholesModel;
import com.fd.gobondg0.algoritms.CalculationModel;
import com.fd.gobondg0.algoritms.PriceCalculator;
import com.fd.gobondg0.db.ForecastsReaderDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalculatorActivity extends Activity {

    private EditText mBaField;
    private EditText mStrikeField;
    private EditText mMaturityField;
    private EditText mVolatilityField;
    private EditText mRateField;
    private Button mGoBond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_activity);
        final String forecastType;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            forecastType = extras.getString("model");
        }else{
            forecastType = "BS";
        }


        mBaField = (EditText) findViewById(R.id.priceBaField);
        mStrikeField = (EditText) findViewById(R.id.priceStrikeField);
        mMaturityField = (EditText) findViewById(R.id.maturityTimeField);
        mVolatilityField = (EditText) findViewById(R.id.volatilityField);
        mRateField = (EditText) findViewById(R.id.rateOfProftField);
        mGoBond = (Button) findViewById(R.id.submitForecastButton);

        mGoBond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double ba = Double.parseDouble(mBaField.getText().toString());
                double s = Double.parseDouble(mStrikeField.getText().toString());
                double t = Double.parseDouble(mMaturityField.getText().toString());
                double vola = Double.parseDouble(mVolatilityField.getText().toString());
                double q = Double.parseDouble(mRateField.getText().toString());
                double r = Double.parseDouble(mRateField.getText().toString());
                ForecastsReaderDbHelper mDbHelper = new ForecastsReaderDbHelper(CalculatorActivity.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = fmt.format(new Date());
                ForecastEntity entity = new ForecastEntity("Users", forecastType, dateString, (float) vola, (float) t, (float) ba, (float) s, (float) r, (float) q);
                ForecastsReaderDbHelper.saveForecastEntity(entity, db);
//                CalculationModel bs = CalculationModel.createCalculationModel(forecastType);
                PriceCalculator pc = BaseApp.getCalculator();
                pc.setMaturity(t);
                pc.setVolatility(vola);
                pc.setBasicPrice(ba);
                pc.setStrikePrice(s);
                pc.setProfitRate(r);
//                pc.performCalculation(PriceCalculator.FOR_MATURITY);
//                ArrayList<float[]> forecastMatur = pc.calculateForecast(0, 2 * pc.getMaturity(), 100, PriceCalculator.FOR_MATURITY);
//                ArrayList<float[]> forecastVola = pc.calculateForecast(0, 2 * pc.getVolatility(), 100, PriceCalculator.FOR_VOLATILITY);
//                ArrayList<float[]> forecastBa = pc.calculateForecast(0, 2 * pc.getBasicPrice(), 100, PriceCalculator.FOR_BASIC_PRICE);
                Intent resIntent = new Intent(CalculatorActivity.this, ResultDetailedActivity.class);
                  resIntent.putExtra("forecast-type", forecastType);
//                resIntent.putExtra("call-price", (float) pc.getCallPrice());
//                resIntent.putExtra("put-price", (float) pc.getPutPrice());
//                resIntent.putExtra("call-maturity-forecast", forecastMatur.get(0));
//                resIntent.putExtra("put-maturity-forecast", forecastMatur.get(1));
//                resIntent.putExtra("call-vola-forecast", forecastVola.get(0));
//                resIntent.putExtra("put-vola-forecast", forecastVola.get(1));
//                resIntent.putExtra("call-ba-forecast", forecastBa.get(0));
//                resIntent.putExtra("put-ba-forecast", forecastBa.get(1));
//                resIntent.putExtra("maturity", (float) pc.getMaturity());
//                resIntent.putExtra("volatility", (float) pc.getVolatility());
//                resIntent.putExtra("ba", (float) pc.getBasicPrice());
                startActivity(resIntent);
            }
        });
    }
}
