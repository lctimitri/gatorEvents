package com.seven.actionbar;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

public class CategoryActivity extends DrawerActivity {

    //The number of pages to display
    private static final int NUM_PAGES = 4;
    //Handles animation and allows swiping to consecutive pages
    private ViewPager mPager;
    //Provides pages to the ViewPager widget
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_category);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);

        LayoutInflater layoutInflater =
                (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_category, null, false);
        frameLayout.addView(activityView);

        //Instantiate a ViewPager and a PagerAdapter
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        LinePageIndicator lineIndicator = (LinePageIndicator) findViewById(R.id.indicator);
        lineIndicator.setViewPager(mPager);
    }

    @Override
    public void onBackPressed() {
        if(mPager.getCurrentItem() == 0)
        {
            //If on first page, allow system to handle back action
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
        else
        {
            //else move to the previous page
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        try {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        catch (Exception e)
        {
            System.err.println(e.toString());
        }
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

    private class CategoryPagerAdapter extends FragmentPagerAdapter
    {

        public CategoryPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0: return SportFragment.newInstance();
                case 1: return RecreationFragment.newInstance();
                case 2: return UFOfficialFragment.newInstance();
                case 3: return CultureFragment.newInstance();
                default: return RecreationFragment.newInstance();
            }
        }

        @Override
        public int getCount()
        {
            return NUM_PAGES;
        }

    }


    public void clickedCategory(View view)
    {
        String category = getResources().getResourceName(view.getId());
//        String categoryName = category.replace("com.seven.actionbar:/id", "");
        String categoryName = category.replace("com.seven.actionbar:id/", "");
        categoryName = categoryName.replace("_", " ");
        Intent intent = new Intent(getApplicationContext(), EventListActivity.class);

        Bundle extras = new Bundle();
        extras.putString("TYPE", "CATEGORY_SEARCH");
        extras.putString("CATEGORY", categoryName);
        intent.putExtras(extras);

        Log.i("categoryName : ", categoryName);

        startActivity(intent);

    }
}
