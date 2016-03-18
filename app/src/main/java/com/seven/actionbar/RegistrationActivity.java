package com.seven.actionbar;

/**
 * Created on 10/18/15.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays.*;
import java.lang.*;

public class RegistrationActivity extends Activity {

    //Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    EditText nameEditText, emailEditText;
    Button registerButton;

    //url to create new user
    private static String url_create_user = "http://www.ufgatorevents.com/android_connect/create_user.php";

    //JSON Node names
    private static final String TAG_SUCCESS = "success";

    public static final String myPreferences = "myPrefs";
    public static final String name = "nameKey";
    public static final String email = "emailKey";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        nameEditText = (EditText) findViewById(R.id.nameText);
        emailEditText = (EditText) findViewById(R.id.emailText);
    }

    public void register(View view)
    {

      //  sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);

      //  String nameString = nameEditText.getText().toString();
      //  String emailString = emailEditText.getText().toString();

      //  SharedPreferences.Editor editor = sharedPreferences.edit();

      //  editor.putString(name, nameString);
      //  editor.putString(email, emailString);
      //  editor.commit();

//        Intent intent = new Intent(this, RegisterSecond.class);
//        startActivity(intent);

        // creating new user in background thread
        String mEmail = emailEditText.getText().toString();
        int p = 0;
        char s[] = mEmail.toCharArray();
        boolean isEmail = false;
        while(p < mEmail.length()){
            if(s[p] == '@')
                isEmail = true;
            p++;
        }

        if(isEmail == false){
            Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(i);
            finish();
            return;
        }


        String[] getCom = mEmail.split("\\@",-1);

        emailEditText.setText(getCom[1].toString());

            if(getCom[1].toString().equals("ufl.edu")){

                new CreateNewUser().execute(nameEditText.getText().toString(),
                        emailEditText.getText().toString());

            }else{
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(i);
                finish();
                return;
            }


    }

    public void login(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    /**
     * Background Async Task to Create new User
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegistrationActivity.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating user
         * */
        protected String doInBackground(String... args) {
            String name = args[0];
            String email = args[1];


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("email", email));

            // getting JSON Object
            // Note that create user url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "POST", params);

            // check log cat for respons
//             Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created user
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
                    // failed to create user
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
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }



}
