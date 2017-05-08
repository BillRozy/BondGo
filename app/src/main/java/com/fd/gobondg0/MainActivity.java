package com.fd.gobondg0;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.fd.gobondg0.algoritms.BlackScholesModel;
import com.fd.gobondg0.algoritms.CalculationModel;
import com.fd.gobondg0.algoritms.MertonModel;
import com.fd.gobondg0.algoritms.PriceCalculator;
import com.fd.gobondg0.db.ForecastReaderContract;
import com.fd.gobondg0.db.ForecastsReaderDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener {


    private ListView mForecastsHistory;
    private ForecastAdapter mForecastsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseModelDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mForecastsHistory = (ListView) findViewById(R.id.forecastsHistoryList);


        mForecastsAdapter = new ForecastAdapter(this,new ArrayList<ForecastEntity>());
        View v = getLayoutInflater().inflate(R.layout.forecast_list_header, null);
        mForecastsHistory.addHeaderView(v, null, false);
        mForecastsHistory.setOnItemClickListener(this);
        mForecastsHistory.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ForecastsReaderDbHelper mDbHelper = new ForecastsReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ArrayList<ForecastEntity> forecasts = ForecastsReaderDbHelper.fetchForecastEntities(db);
        mForecastsAdapter.clear();
        mForecastsAdapter.addAll(forecasts);
        mForecastsHistory.setAdapter(mForecastsAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ForecastEntity current = mForecastsAdapter.getForecastItem(position - 1);
        CalculationModel bs = CalculationModel.createCalculationModel(current.getType());
        PriceCalculator pc = new PriceCalculator(bs);
        pc.setMaturity(current.getMaturity());
        pc.setVolatility(current.getVolatility());
        pc.setBasicPrice(current.getBasicPrice());
        pc.setStrikePrice(current.getStrikePrice());
        pc.setProfitRate(current.getInterestRate());
        pc.performCalculation(PriceCalculator.FOR_MATURITY);
        ArrayList<float[]> forecastMatur = pc.calculateForecast(0, 2 * pc.getMaturity(), 100, PriceCalculator.FOR_MATURITY);
        ArrayList<float[]> forecastVola = pc.calculateForecast(0, 2 * pc.getVolatility(), 100, PriceCalculator.FOR_VOLATILITY);
        ArrayList<float[]> forecastBa = pc.calculateForecast(0, 2 * pc.getBasicPrice(), 100, PriceCalculator.FOR_BASIC_PRICE);
        Intent resIntent = new Intent(MainActivity.this, ResultDetailedActivity.class);
        resIntent.putExtra("call-price", (float) pc.getCallPrice());
        resIntent.putExtra("put-price", (float) pc.getPutPrice());
        resIntent.putExtra("call-maturity-forecast", forecastMatur.get(0));
        resIntent.putExtra("put-maturity-forecast", forecastMatur.get(1));
        resIntent.putExtra("call-vola-forecast", forecastVola.get(0));
        resIntent.putExtra("put-vola-forecast", forecastVola.get(1));
        resIntent.putExtra("call-ba-forecast", forecastBa.get(0));
        resIntent.putExtra("put-ba-forecast", forecastBa.get(1));
        resIntent.putExtra("maturity", (float) pc.getMaturity());
        resIntent.putExtra("volatility", (float) pc.getVolatility());
        resIntent.putExtra("ba", (float) pc.getBasicPrice());
        startActivity(resIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {;
        deleteEntryDialog(position);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_forecast) {
            Intent starterCalculator = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(starterCalculator);
        } else if (id == R.id.forecast_history) {
            ForecastsReaderDbHelper mDbHelper = new ForecastsReaderDbHelper(MainActivity.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            mForecastsAdapter.clear();
            if(ForecastsReaderDbHelper.deleteAllForecastEntries(db)) {
                Toast.makeText(this, "История очищена успешно", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.app_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deleteEntryDialog(final int entryPos){
        final Context context = MainActivity.this;
        String title = "Удалить этот прогноз?";
        String button1String = "Удалить";
        String button2String = "Отмена";

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(title);  // заголовок
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                ForecastEntity current = mForecastsAdapter.getForecastItem(entryPos - 1);
                ForecastsReaderDbHelper mDbHelper = new ForecastsReaderDbHelper(MainActivity.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ForecastsReaderDbHelper.deleteForecastEntry(current, db);
                db = mDbHelper.getReadableDatabase();
                ArrayList<ForecastEntity> forecasts = ForecastsReaderDbHelper.fetchForecastEntities(db);
                mForecastsAdapter.clear();
                mForecastsAdapter.addAll(forecasts);
                Toast.makeText(context, "Прогноз удален",
                        Toast.LENGTH_SHORT).show();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });
        ad.show();
    }

    public void chooseModelDialog(){
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_model_dialog);
        dialog.setTitle("Выберите модель:");

        // set the custom dialog components - text, image and button

        Button bsBtn = (Button) dialog.findViewById(R.id.bsChosenBtn);
        Button mertonBtn = (Button) dialog.findViewById(R.id.mertonChosenBtn);
        Button rabiBtn = (Button) dialog.findViewById(R.id.rabiChosenBtn);
        final Intent starterCalculator = new Intent(MainActivity.this, CalculatorActivity.class);
        // if button is clicked, close the custom dialog
        bsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starterCalculator.putExtra("model", "BS");
                dialog.dismiss();
                startActivity(starterCalculator);

            }
        });

        mertonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starterCalculator.putExtra("model", "Merton");
                dialog.dismiss();
                startActivity(starterCalculator);
            }
        });

        rabiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starterCalculator.putExtra("model", "Rabinovitch");
                dialog.dismiss();
                startActivity(starterCalculator);
            }
        });

        dialog.show();
    }

}
