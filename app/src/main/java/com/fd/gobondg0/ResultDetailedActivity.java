package com.fd.gobondg0;

import android.app.ActionBar;
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

import com.astuetz.PagerSlidingTabStrip;
import com.fd.gobondg0.fragments.BaAxedFragment;
import com.fd.gobondg0.fragments.MaturityAxedFragment;
import com.fd.gobondg0.fragments.ResultPricesFragment;
import com.fd.gobondg0.fragments.VolatilityAxedFragment;

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
    private float[] mCallsMaturityForecast;
    private float[] mPutsMaturityForecast;
    private float[] mCallsVolaForecast;
    private float[] mPutsVolaForecast;
    private float[] mCallsBaForecast;
    private float[] mPutsBaForecast;
    private float mMaturity;
    private float mVolatility;
    private float mBa;

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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.forecast_tabs);
        tabs.setViewPager(mViewPager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            float callPrice = extras.getFloat("call-price");
            mResultedPrices[0] = callPrice;
            float putPrice = extras.getFloat("put-price");
            mResultedPrices[1] = putPrice;
            mCallsMaturityForecast = extras.getFloatArray("call-maturity-forecast");
            mPutsMaturityForecast = extras.getFloatArray("put-maturity-forecast");
            mCallsVolaForecast = extras.getFloatArray("call-vola-forecast");
            mPutsVolaForecast = extras.getFloatArray("put-vola-forecast");
            mCallsBaForecast = extras.getFloatArray("call-ba-forecast");
            mPutsBaForecast = extras.getFloatArray("put-ba-forecast");
            mMaturity = extras.getFloat("maturity");
            mVolatility = extras.getFloat("volatility");
            mBa = extras.getFloat("ba");
        }
    }

    public float[] getCallsMaturityForecast() {
        return mCallsMaturityForecast;
    }

    public float[] getPutsMaturityForecast() {
        return mPutsMaturityForecast;
    }

    public float[] getCallsVolaForecast() {
        return mCallsVolaForecast;
    }

    public float[] getPutsVolaForecast() {
        return mPutsVolaForecast;
    }

    public float[] getCallsBaForecast() {
        return mCallsBaForecast;
    }

    public float[] getPutsBaForecast() {
        return mPutsBaForecast;
    }

    public float getBa() {
        return mBa;
    }

    public float getVolatility() {
        return mVolatility;
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
                    return new BaAxedFragment();
                case 2:
                    return new MaturityAxedFragment();
                case 3:
                    return new VolatilityAxedFragment();
                default:
                    return ResultFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Ожидаемая цена";
                case 1:
                    return "Относительно BA";
                case 2:
                    return "Относительно срока";
                case 3:
                    return "Относительно волатильности ";
            }
            return null;
        }
    }
}
