package com.quislisting.task.handler;

import android.util.Log;

import com.quislisting.util.StringUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public URL createUrl(final String stringUrl) {
        try {
            return new URL(stringUrl);
        } catch (final MalformedURLException e) {
            Log.e(TAG, "Error during URL creation", e);
        }
        return null;
    }

    public String makeHttpGetRequest(final URL url, final String idToken,
                                     final boolean includeParentId) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty(HTTP.CONTENT_TYPE, "application/json");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

            if (StringUtils.isNotEmpty(idToken)) {
                urlConnection.setRequestProperty("Authorization", "Bearer " + idToken);
            }

            if (includeParentId) {
                urlConnection.setRequestProperty("parentId", null);
            }

            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (final IOException e) {
            Log.e(TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public String makeHttpGetSearchRequest(final URL url, final JSONObject jsonObject,
                                           final String languageCode) throws IOException {
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpGet = new HttpGet(url + "?query=" + URLEncoder.encode(jsonObject.toString(), "UTF-8") +
                "&languageCode=" + languageCode);
        httpGet.setHeader("accept", "*/*");

        final HttpResponse response = httpClient.execute(httpGet);
        final InputStream inputStream = response.getEntity().getContent();
        return getStringFromInputStream(inputStream);
    }

    public String makeHttpAuthenticateUserPostRequest(final String url, final JSONObject jsonObject) {
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("accept", "*/*");
        httpPost.setHeader("Content-type", "application/json");

        try {
            final StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);

            final HttpResponse response = httpClient.execute(httpPost);
            final InputStream inputStream = response.getEntity().getContent();
            return getStringFromInputStream(inputStream);
        } catch (final Exception e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        return null;
    }

    public int makeHttpSendPostRequest(final String url, final JSONObject jsonObject,
                                       final boolean includeBearerHeader, final String idToken) {
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("accept", "application/json");
        if (includeBearerHeader) {
            httpPost.setHeader("Authorization", "Bearer " + idToken);
        }

        try {
            final StringEntity se = new StringEntity(jsonObject.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);

            final HttpResponse response = httpClient.execute(httpPost);

            return response.getStatusLine().getStatusCode();
        } catch (final Exception e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        return 0;
    }

    private String readFromStream(final InputStream inputStream) throws IOException {
        final StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            final BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (StringUtils.isNotEmpty(line)) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String getStringFromInputStream(final InputStream is) {
        BufferedReader br = null;
        final StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (final IOException e) {
            Log.e(TAG, "BufferedReader could not be prepared.", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e) {
                    Log.e(TAG, "BufferedReader could not be closed.", e);
                }
            }
        }
        return sb.toString();
    }
}