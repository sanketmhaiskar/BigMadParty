package com.smartapps4u.bigmadparty.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
 
public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    JSONArray jArray=null;
    String json = "";
 
    // constructor
    public JSONParser() {
 
    }
 
    public JSONArray getJSONFromUrl(String url) {
 
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpParams httpParameters = new BasicHttpParams();
            
            //int timeoutConnection = 3000;
           // HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 15000;
         	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
         	httpClient.setParams(httpParameters);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();           
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF8"),8);
          
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");   
              
            }
            is.close();
            //json = sb.toString();
           
            
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        
        // try parse the string to a JSON object
        try {
            jArray = new JSONArray(sb.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jArray;
        
    }
}