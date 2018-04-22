package com.example.pengyuchen.hw91;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class detailActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private JSONObject resultJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String result = bundle.getString("fromResult");
        //resultJson = JSONObject.fromObject(result);
        toolbar.setTitle(result);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);







        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_share:
                    msg += "Click edit";
                    break;
                case R.id.action_like:
                    msg += "Click share";
                    break;

            }

            if(!msg.equals("")) {
                Toast.makeText(detailActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
        private String tabTitles[] = new String[]{"INFO", "PHOTOS", "MAP", "REVIEWS"};
        private int[] imageResId = {
                R.drawable.info_outline,
                R.drawable.photos,
                R.drawable.maps,
                R.drawable.review
        };

        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.mContext = context;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    infoFragment tab1 = new infoFragment();
                    return tab1;
                case 1:
                    photoFragment tab2 = new photoFragment();
                    return tab2;
                case 2:
                    mapFrament tab3 = new mapFrament();
                    return tab3;
                case 3:
                    reviewFrament tab4 = new reviewFrament();
                    return tab4;

                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
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
