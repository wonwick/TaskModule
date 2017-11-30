package com.won.taskmodule;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Oshan Wickramaratne on 2017-09-18.
 */

public class SendPostRequest extends AsyncTask<Void, Void, String> {
    URL url;
    String response = null;
    JSONObject postDataParams = new JSONObject();
    String[] keywords;
    String[] values;

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public void setPostDataParams() {
        if (keywords.length == values.length) {
            for (int i = 0; i < keywords.length; i++) {
                try {
                    postDataParams.put(keywords[i], values[i]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String setUrl(String url) {
        try {
            this.url = new URL(url);
            return "successfully set";


        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }


    public String getPostDataString(JSONObject params) throws Exception {
        //Log.d("json",""+params);
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    protected void onPreExecute() {
    }

    protected String doInBackground(Void... voids) {

        try {

            //URL url = new URL("http://35.197.26.101"); // here is your URL path
            setPostDataParams();
            //postDataParams.put("email", "abc@gmail.com");
            Log.e("params", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();



            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));
                Log.d("json", "" + in);
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            } else {
                return new String("false : " + responseCode);
            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("xyz", result);
        response = result;

    }

}
