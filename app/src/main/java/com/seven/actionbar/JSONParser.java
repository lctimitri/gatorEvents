package com.seven.actionbar;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created on 10/19/2015.
 */
public class JSONParser {
    static InputStream instr = null;
    static JSONObject jsonObj = null;
    static String json = "";

    public JSONParser(){

    }

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params)
    {
        try{
            if(method == "POST") {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                //ResponseHandler responseHandler = new BasicResponseHandler();
                HttpResponse httpResponse = httpClient.execute(httpPost);//error occurs
                HttpEntity httpEntity = httpResponse.getEntity();
                instr = httpEntity.getContent();
            }
            else if(method == "GET"){
                HttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                instr = httpEntity.getContent();
            }
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch(ClientProtocolException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    instr, "iso-8859-1"), 8);
            StringBuilder strb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) !=null ){
                strb.append(line + "\n");
            }
            instr.close();
            json = strb.toString();
        }
        catch(Exception e){
            Log.e("Buffer Error", "Error converting result" + e.toString());
        }

        try{
            jsonObj = new JSONObject(json);
        }
        catch(JSONException e){
            Log.e("JSON Parser", "Error parsing data" + e.toString());
        }

        return jsonObj;
    }

}
