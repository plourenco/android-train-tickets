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

    private static final String apiUrl = "http://10.0.2.2:8080/api/";

    /**
     * Make a GET request to a sub url with parameters
     * @param sUrl String
     * @param params KeyValuePair[]
     */
    public static JSONObject makeGet(String sUrl, KeyValuePair... params) {
        StringBuilder result = new StringBuilder();

        try {
            String queryString = join(params, "&");
            URL url = new URL(apiUrl + sUrl + queryString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(1000);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return new JSONObject(result.toString());
        }
        catch(IOException | JSONException e) {
            Log.d("Exception", e.getMessage());
        }
        return null;
    }

    /**
     * Make a POST request to a sub url with parameters
     * @param sUrl String
     * @param params KeyValuePair[]
     */
    public static JSONObject makePost(String sUrl, KeyValuePair... params) {
        StringBuilder result = new StringBuilder();

        try {
            String queryString = join(params, "&");
            URL url = new URL(apiUrl + sUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            DataOutputStream wr = null;
            try {
                conn.setRequestProperty("Content-Length",
                        String.valueOf(queryString.length()));
                wr = new DataOutputStream(conn.getOutputStream());
                wr.write(queryString.getBytes());
            }
            finally {
                if(wr != null) wr.close();
            }

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return new JSONObject(result.toString());
        }
        catch(IOException | JSONException e) {
            Log.d("Exception", e.getMessage());
        }
        return null;
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
