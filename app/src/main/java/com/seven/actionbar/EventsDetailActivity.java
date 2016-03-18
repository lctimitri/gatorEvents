package com.seven.actionbar;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EventsDetailActivity extends AppCompatActivity {

    public MyApp myApp;

    private static String url_detail_events = "http://www.ufgatorevents.com/android_connect/get_events_details_2.php";
    private static String url_join = "http://www.ufgatorevents.com/android_connect/join_events.php";
    private static String url_leave = "http://www.ufgatorevents.com/android_connect/leave_event.php";
    private ProgressDialog pDialog;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";


    // events JSONArray
    JSONArray events = null;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    TextView tDes;
    TextView tVenue;
    TextView tDate;
    TextView tTime;
    TextView tPdate;
    TextView tPtime;
    TextView tCont;
    TextView tOrg;
    TextView tCount;
    ImageView header;

    String mCat;
    String mName;
    String mImageURL;
    String mDes;
    String mVenue;
    String mDate;
    String mTime;
    String mPdate;
    String mPtime;
    String mCont;
    String mOrg;
    String mCount;
    String mGoingStatus;

    // UI Components -  switch and drawable for action bar
    Switch goingSwitch;
    ColorDrawable actionBarColor;

    HashMap<String, String> joinMap;

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventsdetail);

        goingSwitch = (Switch) findViewById(R.id.btn_join);

        tDes = (TextView)findViewById(R.id.evt_desc);
        tVenue = (TextView)findViewById(R.id.evt_venue);
        tDate = (TextView)findViewById(R.id.evt_date);
        tTime = (TextView)findViewById(R.id.evt_time);
        tPdate = (TextView)findViewById(R.id.evt_post_date);
        tPtime = (TextView)findViewById(R.id.evt_post_time);
        tCont = (TextView)findViewById(R.id.evt_contact);
        tOrg = (TextView)findViewById(R.id.evt_org);
        tCount = (TextView)findViewById(R.id.evt_count);

        //Intent intent = getIntent();
        //joinMap = (HashMap)intent.getSerializableExtra("e_uMap");
        myApp = (MyApp)getApplication();

        //action bar magic
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        header = (ImageView) findViewById(R.id.header);

        new LoadDetail().execute();

        goingSwitch.setOnCheckedChangeListener(null);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void showMap(View view)
    {

        Intent google_map = new Intent(android.content.Intent.ACTION_VIEW);

        google_map.setData(Uri.parse("geo:0,0?q=:" + mVenue));

        startActivity(google_map);
    }


    /******************************Join******************************************************/
    /**
     * Background Async Task to Load all event by making HTTP Request
     * */
    class JoinEvents extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EventsDetailActivity.this);//EventsActivity
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * joinning
         * */
        protected String doInBackground(String... args) {
            //String name = inputName.getText().toString();

            // Building Parameters
            String going = args[0];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("U_id", myApp.e_uMap.get("U_id")));
            params.add(new BasicNameValuePair("E_id", myApp.e_uMap.get("E_id")));
            // Building Parameters
            //List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL

            JSONObject json;

            if (going == "true")
            {
                json = jParser.makeHttpRequest(url_join, "GET", params);
            }

            else
            {
                json = jParser.makeHttpRequest(url_leave, "GET", params);
            }

            // Check your log cat for JSON reponse
//            Log.d("All Events: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    Intent mInt = new Intent(getApplicationContext(), LoginActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(),
                            (int) System.currentTimeMillis(),mInt,0);
                    Notification n = new Notification.Builder(getApplicationContext())
                            .setContentTitle("You've joined one event, please check it!")
                            .setContentText("GatorEvents")
                            .setSmallIcon(R.drawable.gatorevents)
                            .setContentIntent(pIntent)
                            .addAction(R.drawable.gatorevents, "Have a look!", pIntent).build();
                    NotificationManager notificationManager = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);
                    n.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(0,n);


                } else {
                    // no events found, go to home page
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);//HomeAcitivity
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
        }

    }

    /****************************************************************************************/

    /**
     * Background Async Task to Load all event by making HTTP Request
     * */
    class LoadDetail extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EventsDetailActivity.this);//EventsActivity
            pDialog.setMessage("Loading event. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All events from url
         * */
        protected String doInBackground(String... args) {
            //String name = inputName.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uid", myApp.e_uMap.get("U_id")));
            params.add(new BasicNameValuePair("eid", myApp.e_uMap.get("E_id")));
            // Building Parameters
            //List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_detail_events, "GET", params);

            // Check your log cat for JSON reponse
//            Log.d("All Events: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // events found
                    // Getting Array of events
                    events = json.getJSONArray("events");

                    // looping through All Events
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject c = events.getJSONObject(i);

                        // Storing each json item in variable
                        mCat = c.getString("Category");
                        mName =  c.getString("E_name");
                        mImageURL = c.getString("ImageURL");
                        mDes = c.getString("Description");
                        mVenue =  c.getString("Venue");
                        mDate =  c.getString("EDate");
                        mTime = c.getString("Time");
                        mPdate = c.getString("Post_date");
                        mPtime = c.getString("Post_time");
                        mCont = c.getString("Contact_Person");
                        mOrg = c.getString("Organization");
                        mCount = c.getString("Count");
                        mGoingStatus = c.getString("isGoing");
                        // creating new HashMap
                     /*   HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_EID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_DES, decrp);

                        // adding HashList to ArrayList
                        eventsList.add(map);
                     */
                    }
                } else {
                    // no events found, go to home page
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);//HomeAcitivity
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
                  /* ListAdapter adapter = new SimpleAdapter(
                            MainActivity.this, eventsList,
                            R.layout.events_detail, new String[] { TAG_EID,
                            TAG_NAME, TAG_DES},
                            new int[] { R.id.eid, R.id.name, R.id.decrp});
                    // updating listview
                    lv.setAdapter(adapter);*/
//                    tCat.setText(mCat);
//                    tName.setText(mName);


                    //Formatting the date
                    SimpleDateFormat formatDate1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date event_date_format = null;
                    Date posted_date_format = null;

                    try
                    {
                        event_date_format = formatDate1.parse(mDate);
                        posted_date_format = formatDate1.parse(mPdate);
                    }
                    catch (Exception e)
                    {
                        System.err.print(e);
                    }
                    SimpleDateFormat formatDate2 = new SimpleDateFormat("EEE, dd MMM yyyy");
                    String event_date = null;
                    String posted_date = null;
                    try
                    {
                        event_date = formatDate2.format(event_date_format);
                        posted_date = formatDate2.format(posted_date_format);
                    }
                    catch (Exception e)
                    {
                        System.err.print(e);
                    }

                    //Formatting the time
                    SimpleDateFormat formatTime1 = new SimpleDateFormat("hh:mm:ss");
                    Date event_time_format = null;
                    Date posted_time_format = null;
                    try
                    {
                        event_time_format = formatTime1.parse(mTime);
                        posted_time_format = formatTime1.parse(mPtime);

                    }
                    catch (Exception e)
                    {
                        System.err.print(e);
                    }
                    SimpleDateFormat formatTime2 = new SimpleDateFormat("hh:mm aaa");
                    String event_time = null;
                    String posted_time = null;
                    try
                    {
                        event_time = formatTime2.format(event_time_format);
                        posted_time = formatTime2.format(posted_time_format);
                    }
                    catch (Exception e)
                    {
                        System.err.print(e);
                    }

                    if (mGoingStatus.equals("true"))
                    {
                        Log.i("going status : ", mGoingStatus);
                        boolean mGoing = true;
                        goingSwitch.setChecked(mGoing);
                    }
                    else
                    {
                        Log.i("going status : ", mGoingStatus);
                        boolean mGoing = false;
                        goingSwitch.setChecked(mGoing);
                    }

                    goingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                    {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                        {
                            new JoinEvents().execute(String.valueOf(isChecked));
                        }
                    });

                    collapsingToolbar.setTitle(mName);
                    tDes.setText(mDes);
                    tVenue.setText(mVenue);
                    tDate.setText(event_date);
                    tTime.setText(event_time);
                    tPdate.setText(posted_date);
                    tPtime.setText(posted_time);
                    tCont.setText(mCont);
                    tOrg.setText(mOrg);
                    int count = Integer.parseInt(mCount);
                    String countText = null;
                    if (count == 0)
                    {
                        countText = "Be the first to attend!";
                    }
                    else if (count == 1)
                    {
                        countText = "1 person is going";
                    }
                    else
                    {
                        countText = mCount + " people are going";
                    }
                    tCount.setText(countText);

                    new DownloadImageTask((ImageView) findViewById(R.id.header)).execute(mImageURL);
                }
            });


        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
