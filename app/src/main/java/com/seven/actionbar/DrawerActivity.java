package com.seven.actionbar;

/**
 * Created on 10/11/15.
 */
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;

public class DrawerActivity extends FragmentActivity
{
    HashMap<String, String> myMap;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private String[] mActivityTitles;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle = null;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mActivityTitles = getResources().getStringArray(R.array.activityTitles);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mDrawerList.setItemChecked(0, true);
        mDrawerList.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.drawer_list_item,
                R.id.drawerText,
                mActivityTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                   //    R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        {
            public void onDrawerOpened(View view)
            {
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view)
            {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        Intent intent = getIntent();
        myMap = (HashMap)intent.getSerializableExtra("userMap");


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

        for(int index = 0; index < menu.size(); index++)
        {
            MenuItem menuItem = menu.getItem(index);

            if(menuItem != null)
            {
                //hide menu items if drawer is open
                menuItem.setVisible(!drawerOpen);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void restoreActionBar() {
        ActionBar actionBar = this.getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return  super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view,
                                final int position, long id)
        {
            //Intent intentv = getIntent();
            //myMap = (HashMap)intentv.getSerializableExtra("userMap");

            Intent intent = null;
            switch(position)
            {
                case 0:
                    intent = new Intent(getApplicationContext(), MainActivity.class);

                    break;
                case 1:
                    intent = new Intent(getApplicationContext(), CategoryActivity.class);
                    break;
                //case 2:
                    //intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                   // break;
                case 2:
                    intent = new Intent(getApplicationContext(), MyEventsActivity.class);
                    break;
               // case 4:
                   // intent = new Intent(getApplicationContext(), NotificationsActivity.class);
                   // intent = new Intent(getApplicationContext(), NotificationsActivity.class);
                   // break;
                case 3:
                    intent = new Intent(getApplicationContext(), CalendarActivity.class);
                    break;
               // case 6:
                //    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                 //   intent.putExtra("userMap", myMap);
                //    break;
                default:
                    break;
            }

            mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerList.setItemChecked(position, true);
                }
            }, 2000);
//            mDrawerList.setSelection(position);
            startActivity(intent);
            finish();
//            setTitle(mActivityTitles[position]);

        }


    }
    @Override
    public void setTitle(CharSequence title)
    {
        mTitle = title;
        System.out.println(mTitle);
        getActionBar().setTitle(mTitle);
    }

}
