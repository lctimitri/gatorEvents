package com.seven.actionbar;

/**
 * Created on 10/11/15.
 */
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

public class NotificationsActivity extends DrawerActivity {
    HashMap<String, String> myMap;
    String uid;
    TextView txtUID;
    TextView txtUName;

    public MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);

        LayoutInflater layoutInflater =
                (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_notifications, null, false);
        frameLayout.addView(activityView);

        txtUID = (TextView)findViewById(R.id.txt_uid);
        txtUName = (TextView)findViewById(R.id.txt_name);
        //Intent intent = getIntent();
        //myMap = (HashMap)intent.getSerializableExtra("userMap");

        /*try {//IMPORTANT PROBLEM!
            txtUID.setText(myMap.get("U_id"));
            txtUName.setText("Hello, " + myMap.get("U_name"));
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }*/

        myApp = (MyApp)getApplication();
        txtUID.setText(myApp.uMap.get("U_id"));
        txtUName.setText(myApp.uMap.get("U_name"));
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

}
