package com.example.pengyuchen.hw91;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends AppCompatActivity {

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
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }


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
        if (id == R.id.action_settings) {
            return true;
        }

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


//        public CharSequence getPageTitle(int position) {
//            Drawable drawable;
//            String title;
//            switch (position) {
//                case 0:
//                    drawable = ContextCompat.getDrawable(mContext, R.drawable.search_icon);
//                    title="RESULTS";
//                    break;
//                case 1:
//                    drawable = ContextCompat.getDrawable(mContext, R.drawable.heart_fill_white);
//                    title="FAVORITES";
//                    break;
//                default:
//                    drawable = ContextCompat.getDrawable(mContext, R.drawable.search_icon);
//                    title="RESULTS";
//                    break;
//            }
//
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
//            SpannableString spannableString = new SpannableString(" " + title);
//            spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            return spannableString;
//        }



    }
}
