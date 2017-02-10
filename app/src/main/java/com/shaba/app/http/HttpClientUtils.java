package com.shaba.app.http;

import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.util.Map;


public class HttpClientUtils {


    private static final String TAG = HttpClientUtils.class.getSimpleName();
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(6000);
    }

    public static void postJson(String postUrl, Map<?, ?> params,
                                JsonHttpResponseHandler responseHandler) {

        try {

            JSONObject json = new JSONObject(params);
            StringEntity entity = new StringEntity(json.toString(), "UTF-8");
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            entity.setContentEncoding("UTF-8");
            client.post(null, postUrl, entity,
                    "application/json;charset=utf-8", responseHandler);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void getJson(String postUrl, RequestParams params,
                               AsyncHttpResponseHandler responseHandler) {
        client.get(postUrl, params, responseHandler);
    }

    public static void getJsonForToken(String postUrl, RequestParams params,
                                       AsyncHttpResponseHandler responseHandler, String token) {
        client.addHeader("Authorization", token);
        client.get(postUrl, params, responseHandler);
    }

    public static void post(String postUrl, Map<?, ?> params, String token,
                            JsonHttpResponseHandler responseHandler) {
        try {
            JSONObject json = new JSONObject(params);
            StringEntity entity = new StringEntity(json.toString(), "UTF-8");
            //entity.setContentType("Authorization: Token " + token);
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            entity.setContentEncoding("UTF-8");
            client.addHeader("Authorization", token);
            client.setTimeout(5000);
            client.post(null, postUrl, entity,
                    "application/json;charset=utf-8", responseHandler);
        } catch (Exception e) {
        }
    }
}
