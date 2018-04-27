package com.example.pengyuchen.hw91;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private LocationManager locationManager;
    private String provider;

    public static double lat;
    public static double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location!=null){
                    lon = location.getLongitude();
                    lat = location.getLatitude();
                    String currentPosition="latitude is"+lat+"\n"+"longitude is"+lon;
                    Log.v("msg",currentPosition);
                }else{
                    Toast.makeText(this, "Cannot get your location", Toast.LENGTH_LONG).show();

                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10,locationListener);


            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        1234);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    1234);
        }




    }


    LocationListener locationListener=new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            lon = location.getLongitude();
            lat = location.getLatitude();
            String currentPosition="latitude is"+lat+"\n"+"longitude is"+lon;
            Log.v("msg",currentPosition);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;
        private String tabTitles[] = new String[]{"RESULTS", "FAVORITES"};
        private int[] imageResId = {R.drawable.search_icon,
                R.drawable.heart_fill_white,
                };

        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.mContext = context;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    searchFragment tab1 = new searchFragment();
                    return tab1;
                case 1:
                    favoriteFragment tab2 = new favoriteFragment();
                    return tab2;

                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            Drawable image = mContext.getResources().getDrawable(imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            // Replace blank spaces with image icon
            SpannableString sb = new SpannableString("   " + tabTitles[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }


    }



}
