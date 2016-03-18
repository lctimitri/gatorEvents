package com.seven.actionbar;

/**
 * Created on 10/11/15.
 */
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.List;


public class CalendarActivity extends DrawerActivity {

    MaterialCalendarView calendar;
    List<CalendarDay> dates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);

        LayoutInflater layoutInflater =
                (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_calendar, null, false);
        frameLayout.addView(activityView);

        final Calendar today = Calendar.getInstance();
        calendar = (MaterialCalendarView) findViewById(R.id.calendarView);

        calendar.setSelectedDate(today);
        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        calendar.setOnDateChangedListener(listener);
    }

    OnDateSelectedListener listener = new OnDateSelectedListener() {
        @Override
        public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected)
        {
            int day = date.getDay();
            int month = date.getMonth();
            int year = date.getYear();
            month += 1;
            widget.clearSelection();
            widget.setSelectedDate(CalendarDay.today());
            String dateString = String.valueOf(year) + "-" + String.valueOf(month)
                    + "-" + String.valueOf(day);

            Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
            Bundle extras = new Bundle();
            extras.putString("TYPE", "DATE_SEARCH");
            extras.putString("DATE", dateString);
            intent.putExtras(extras);
            Log.i("dateString : ", dateString);

            startActivity(intent);

        }
    };

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
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
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

}
