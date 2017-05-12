package com.fd.gobondg0;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        TextView newForecast = (TextView) findViewById(R.id.forecastTitle);
        TextView aboutForecast = (TextView) findViewById(R.id.forecastDetails);

        switch (forecastType){
            case "BS":
                newForecast.setText("Новый прогноз по Блэку-Шоулзу");
                aboutForecast.setText("Корреляция не используется, дивиденды с непрерывным доходом не учитываются.");
                break;
            case "Merton":
                newForecast.setText("Новый прогноз по Мертону");
                aboutForecast.setText("Динамическая корреляция не используется, дивиденды с непрерывным доходом учитываются.");
                break;
            case "Rabinovitch":
                newForecast.setText("Новый прогноз по Рабиновичу");
                aboutForecast.setText("Используются стохастические процентные ставки по Васичеку");
                break;
            case "Triple":
                newForecast.setText("Новый сравнительный прогноз");
                aboutForecast.setText("Используется для сравнения результатов прогнозирования в разных моделях ценообразования");
                break;
        }


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
                PriceCalculator pc = BaseApp.getCalculator();
                pc.applyForecastEntity(entity);
                Intent resIntent = new Intent(CalculatorActivity.this, ResultDetailedActivity.class);
                resIntent.putExtra("forecast-type", forecastType);
                startActivity(resIntent);
            }
        });
    }
}
