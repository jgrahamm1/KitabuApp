package com.example.jgraham.kitabureg1;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/*
 * Helper class used to communicate with the Kitabu server.
 */

public final class ServerUtil {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

    /**
     * Issue a GET request to the server.
     *
     * @param endpoint GET address.
     * @param params   request parameters.
     * @throws IOException propagated from GET.
     */
    public static String get(String endpoint, Map<String, String> params, Context context)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the GET body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.d("REGISTRATION", "Url: " + url);
        Log.d("REGISTRATION", "Body: " + body.toString());

        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            Log.d("TAGG", "" + status);
            if (status != 200) {
                Log.d("ALERT", "Server down!!!");
                Log.d("ALERT", "Server down!!!");
                Log.d("ALERT", "Server down!!!");
                Log.d("ALERT", "Server down!!!");
                Log.d("ALERT", "Server down!!!");
                Log.d("ALERT", "Server down!!!");
                Log.d("ALERT", "Server down!!!");
                return "502"; // When the flag isn't 200, its got to be 502 and the server is down!
            } else {
                // Get Response
                InputStream is = conn.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
                rd.close();
                return response.toString();
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
