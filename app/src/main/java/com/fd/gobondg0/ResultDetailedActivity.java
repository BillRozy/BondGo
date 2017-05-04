package com.fd.gobondg0;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.fd.gobondg0.db.ForecastReaderContract;
import com.fd.gobondg0.db.ForecastsReaderDbHelper;

public class ResultDetailedActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private float[] mResultedPrices = new float[2];
    private float[] mCallsForecast;
    private float[] mPutsForecast;
    private float mMaturity;

    public float[] getPrices(){
        return mResultedPrices;
    }

    public float getMaturity() {
        return mMaturity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detailed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            float callPrice = extras.getFloat("call-price");
            mResultedPrices[0] = callPrice;
            float putPrice = extras.getFloat("put-price");
            mResultedPrices[1] = putPrice;
            mCallsForecast = extras.getFloatArray("call-forecast");
            mPutsForecast = extras.getFloatArray("put-forecast");
            mMaturity = extras.getFloat("maturity");
        }

        SQLiteDatabase db = (new ForecastsReaderDbHelper(this)).getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                ForecastReaderContract.CurveEntry._ID,
                ForecastReaderContract.CurveEntry.CURVE_NAME,
                ForecastReaderContract.CurveEntry.CURVE_COLOR
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                ForecastReaderContract.CurveEntry.CURVE_NAME + " DESC";

        Cursor c = db.query(
                ForecastReaderContract.CurveEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToFirst();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(ForecastReaderContract.CurveEntry._ID)
        );
        String name = c.getString(c.getColumnIndexOrThrow(ForecastReaderContract.CurveEntry.CURVE_NAME));
        Toast.makeText(this, "Got: " + name + " , " + itemId, Toast.LENGTH_LONG).show();

    }

    public float[] getCallsForecast() {
        return mCallsForecast;
    }

    public float[] getPutsForecast() {
        return mPutsForecast;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_detailed, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ResultFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public ResultFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ResultFragment newInstance(int sectionNumber) {
            ResultFragment fragment = new ResultFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_result_detailed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ResultFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return new ResultPricesFragment();
                case 1:
                    return new ResultAxisFragment();
                default:
                    return ResultFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
