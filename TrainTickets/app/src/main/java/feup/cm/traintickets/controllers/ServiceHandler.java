package feup.cm.traintickets.controllers;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import feup.cm.traintickets.util.KeyValuePair;

public class ServiceHandler {

    private static final String apiUrl = "http://192.168.1.67:8080/api/";

    /**
     * Make a GET request to a sub url with parameters
     * @param sUrl String
     * @param params KeyValuePair[]
     */
    public static String makeGet(String sUrl, String token, KeyValuePair... params) {
        StringBuilder result = new StringBuilder();

        try {
            String queryString = join(params, "&");
            URL url = new URL(apiUrl + sUrl + queryString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(token != null)
                conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
        catch(IOException e) {
            Log.d("Exception", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Make a POST request to a sub url with parameters
     * @param sUrl String
     * @param jsonStr String
     */
    public static String makePost(String sUrl, String jsonStr, String token) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(apiUrl + sUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            if(token != null)
                conn.setRequestProperty("Authorization", "Bearer " + token);

            DataOutputStream wr = null;
            try {
                wr = new DataOutputStream(conn.getOutputStream());
                wr.write(jsonStr.getBytes());
                wr.flush();
            }
            finally {
                if(wr != null) wr.close();
            }
            InputStreamReader is = new InputStreamReader(conn.getResponseCode() / 100 == 2 ?
                    conn.getInputStream() : conn.getErrorStream());
            BufferedReader reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
        catch(IOException e) {
            Log.d("Exception", e.getMessage());
        }
        return null;
    }

    public static String makePost(String sUrl, String jsonStr) {
        return makePost(sUrl, jsonStr, null);
    }

    public static String makeGet(String sUrl, KeyValuePair... params) {
        return makeGet(sUrl, null, params);
    }

    /**
     * Helper method to join an array of objects into a query string
     * @param params KeyValuePair[]
     * @param delim String
     */
    private static String join(KeyValuePair[] params, String delim) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<params.length; i++) {
            builder.append(params[i].toString());
            if(i < params.length - 1)
                builder.append(delim);
        }
        return builder.toString();
    }
}
