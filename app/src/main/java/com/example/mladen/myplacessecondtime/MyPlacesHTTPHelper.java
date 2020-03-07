package com.example.mladen.myplacessecondtime;

import android.net.Uri;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

public class MyPlacesHTTPHelper {
    private static final String GET_MY_PLACE = "1";
    private static final String GET_ALL_PLACES_NAMES = "2";
    private static final String SEND_MY_PLACE = "3";

    public static String sendMyPlace(MyPlace place)
    {
        String retStr = "";
        try
        {
            URL url = new URL("http://10.0.2.2:8080");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            JSONObject holder = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("name", place.getName());
            data.put("desc", place.getDesc());
            data.put("lon", place.getLongitude());
            data.put("lat", place.getLatitude());
            holder.put("myplace", data);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("req", SEND_MY_PLACE)
                    .appendQueryParameter("name", place.getName())
                    .appendQueryParameter("payload", holder.toString());
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                retStr = inputStreamToString(conn.getInputStream());

            }
            else{
                retStr = String.valueOf("Error: " + responseCode);
            }
        }
        catch (Exception error)
        {
            error.printStackTrace();
            retStr = "Error during upload!";
        }

        return retStr;


    }

    private static String inputStreamToString(InputStream is)
    {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try
        {
            while ((line = rd.readLine()) != null)
            {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }
}
