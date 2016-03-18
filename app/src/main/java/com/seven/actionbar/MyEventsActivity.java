package com.seven.actionbar;

/**
 * Created on 10/11/15.
 */


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MyEventsActivity extends DrawerActivity
{

    public MyApp myApp;

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> eventsList;
    HashMap<String, String> myMap;//Uid & Eid

    // url to get all events list
    private static String url_my_events =
            "http://www.ufgatorevents.com/android_connect/get_my_events.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_EID = "E_id";
    private static final String TAG_NAME = "E_name";
    private static final String TAG_DES = "Description";
    private static final String TAG_COUNT = "Count";
    private static final String TAG_DATE = "Date";
    private static final String TAG_TIME = "Time";

    // events JSONArray
    JSONArray events = null;
    ListView lv;


    LinearLayout linearLayout;
    boolean eventsFound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);

        LayoutInflater layoutInflater =
                (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.myevents, null, false);
        frameLayout.addView(activityView);

        linearLayout = (LinearLayout) findViewById(R.id.no_events_display);

        myApp = (MyApp) getApplication();

        // Hashmap for ListView
        eventsList = new ArrayList<HashMap<String, String>>();

        // Loading events in Background Thread
        new LoadMyEvents().execute();

        // Get listview
        lv = (ListView) findViewById(R.id.list);

        // on seleting single event
        // launching  Detail Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                // getting values from selected ListItem
                String eid = ((TextView) view.findViewById(R.id.eid)).getText()
                        .toString();

                myApp.e_uMap.put("U_id", myApp.uMap.get("U_id"));
                myApp.e_uMap.put("U_name", myApp.uMap.get("U_name"));
                myApp.e_uMap.put("E_id", eid);


                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EventsDetailActivity.class);//EventsDetail

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);

            }
        });

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

    /**
     * Background Async Task to Load all event by making HTTP Request
     * */
    class LoadMyEvents extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyEventsActivity.this);//MyEventsActivity
            pDialog.setMessage("Loading events. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting My events from url
         * */
        protected String doInBackground(String... args) {
            //String name = inputName.getText().toString();

            // Building Parameters
            //List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("name", name));
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("U_id", myApp.uMap.get("U_id")));


            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_my_events, "GET", params);

            // Check your log cat for JSON reponse
//            Log.d("All Events: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // events found
                    eventsFound = true;
                    // Getting Array of events
                    events = json.getJSONArray(TAG_EVENTS);

                    // looping through All Events
                    for (int i = 0; i < events.length(); i++)
                    {

                        JSONObject c = events.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_EID);
                        String name = c.getString(TAG_NAME);
                        String decrp = c.getString(TAG_DES);
                        String attendees = c.getString(TAG_COUNT);
                        String date = c.getString(TAG_DATE);
                        String time = c.getString(TAG_TIME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // formatting date and time
                        SimpleDateFormat formatDate1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date event_date_format = null;
                        SimpleDateFormat formatTime1 = new SimpleDateFormat("hh:mm:ss");
                        Date event_time_format = null;

                        try
                        {
                            event_date_format = formatDate1.parse(date);
                            event_time_format = formatTime1.parse(time);
                        }

                        catch (Exception e)
                        {
                            System.err.print(e);
                        }
                        SimpleDateFormat formatDate2 = new SimpleDateFormat("EEE, dd MMM");
                        String event_date = null;
                        SimpleDateFormat formatTime2 = new SimpleDateFormat("hh:mm aaa");
                        String event_time = null;
                        try
                        {
                            event_date = formatDate2.format(event_date_format);
                            event_time = formatTime2.format(event_time_format);
                        }
                        catch (Exception e)
                        {
                            System.err.print(e);
                        }

                        // adding each child node to HashMap key => value
                        map.put(TAG_EID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_DES, decrp);
                        map.put(TAG_COUNT, attendees);
                        map.put(TAG_DATE, event_date);
                        map.put(TAG_TIME, event_time);

                        // adding HashList to ArrayList
                        eventsList.add(map);
                    }
                }
                else

                {
                    // no events found, go to home page
                    eventsFound = false;
                }

            } catch (JSONException e) {
                System.err.println(e);
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
            catch(RuntimeException e){
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all events
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView   EventsActivity
                     * */
                    if (eventsFound)
                    {
                        linearLayout.setVisibility(View.GONE);
                        ListAdapter adapter = new SimpleAdapter(
                                MyEventsActivity.this, eventsList,
                                R.layout.events_detail, new String[]{TAG_EID,
                                TAG_NAME, TAG_COUNT, TAG_DATE, TAG_TIME},
                                new int[]{R.id.eid, R.id.name, R.id.attendees, R.id.date, R.id.time});
                        // updating listview
                        lv.setAdapter(adapter);
                    }
                    else
                    {
                        lv.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }

                }
            });

        }

    }

}
