package com.teamx.soon;

/**
 * Created by Nguyen Duc Thinh on 11/02/2015.
 * Project type: Android
 */

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.teamx.soon.item.Event;
import com.teamx.soon.item.Feedback;
import com.teamx.soon.item.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HttpClient {
    private static final String BASE_URL = "http://128.199.167.255/hatch/";

    private static final String GEOCODING = "http://maps.googleapis.com/maps/api/geocode/json";


    private static final String SUB_LOGIN_WITH_FACEBOOK = "user/loginWithFacebook";

    private static final String SUB_GET_EVENT = "event/getEvent";
    private static final String SUB_GET_EVENT_BY_USER = "http://128.199.167.255/hatch/event/getEventByUser";
    private static final String SUB_GET_FEEDBACK = "feedback/getFeedbackByEvent";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static AsyncHttpClient syncClient = new SyncHttpClient();

    private static Context context;

    public static void initialize(Context context) {
        HttpClient.context = context;
    }

    static RequestHandle get(String subUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return client.get(getAbsoluteUrl(subUrl), params, responseHandler);
    }

    static RequestHandle get(String subUrl, AsyncHttpResponseHandler responseHandler) {
        return client.get(getAbsoluteUrl(subUrl), responseHandler);
    }

    public static RequestHandle post(String subUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return client.post(getAbsoluteUrl(subUrl), params, responseHandler);
        } else {
            return syncClient.post(getAbsoluteUrl(subUrl), params, responseHandler);
        }
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static RequestHandle facebookLogin(@NonNull User user, @NonNull String deviceId, final CreateResponse createResponse) {
        RequestParams params = new RequestParams();

        params.put("facebook_id", user.accId);
        params.put("facebook_access_token", user.facebookAccessToken);
        params.put("photo", user.photoUrl);
        params.put("username", user.username);
        params.put("device_id", deviceId);

        return post(SUB_LOGIN_WITH_FACEBOOK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject user_data = response.getJSONObject("data");
                    int newUserId = user_data.optInt("id", -1);
                    createResponse.onSuccess(newUserId);
                } catch (JSONException ignore) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                createResponse.onFailure(throwable.getMessage());
            }
        });
    }

    public static RequestHandle getEvent(@NonNull int limit, @NonNull int offset, final ListResponse<Event> eventResponse) {
        RequestParams params = new RequestParams();

        params.put("limit", limit);
        params.put("offset", offset);

        return get(SUB_GET_EVENT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray eventJson = response.getJSONArray("data");
                    ArrayList<Event> eventList = new ArrayList<Event>();
                    for (int i = 0; i < eventJson.length(); i++) {
                        JSONObject c = eventJson.getJSONObject(i);
                        int id = c.optInt("id");
                        String name = c.optString("name");
                        String image = c.optString("images");
                        String description = c.optString("description");
                        String status = c.optString("status");
                        String date = c.optString("date");
                        String address = c.optString("address");
                        String type = c.optString("type");

                        Event event = new Event(name, image, address, date, type, status, description);
                        event.id = id;
                        eventList.add(event);
                    }
                    eventResponse.onSuccess(eventList);
                } catch (JSONException ignore) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                eventResponse.onFailure(throwable.getMessage());
            }
        });
    }

    public static RequestHandle getFeedback(Event event, final ListResponse<Feedback> eventResponse) {
        RequestParams params = new RequestParams();

        params.put("event_id", event.id);

        return get(SUB_GET_FEEDBACK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray eventJson = response.getJSONArray("data").getJSONArray(0);
                    ArrayList<Feedback> eventList = new ArrayList<>();
                    for (int i = 0; i < eventJson.length(); i++) {
                        Feedback feedback = new Feedback();
                        JSONObject c = eventJson.getJSONObject(i);
                        feedback.id = c.optInt("id");
                        feedback.content = c.optString("question_content");
                        feedback.type = c.getInt("question_type");
                        eventList.add(feedback);
                    }
                    eventResponse.onSuccess(eventList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                eventResponse.onFailure(throwable.getMessage());
            }
        });
    }

    public static RequestHandle getEventByUser(@NonNull String userId, @NonNull int limit, @NonNull int offset, final ListResponse<Event> eventResponse) {
        RequestParams params = new RequestParams();

        params.put("user_id", userId);
        params.put("limit", limit);
        params.put("offset", offset);

        return get(SUB_GET_EVENT_BY_USER, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray eventJson = response.getJSONArray("data");
                    ArrayList<Event> eventList = new ArrayList<>();
                    for (int i = 0; i < eventJson.length(); i++) {
                        JSONObject c = eventJson.getJSONObject(i);

                        String name = c.optString("name");
                        String image = getAbsoluteUrl(c.optString("image"));
                        String description = c.optString("description");
                        String status = c.optString("status");
                        String date = c.optString("date");
                        String address = c.optString("address");
                        String type = c.optString("type");

                        Event event = new Event(name, image, address, date, type, status, description);
                        eventList.add(event);
                    }
                    eventResponse.onSuccess(eventList);
                } catch (JSONException ignore) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                eventResponse.onFailure(throwable.getMessage());
            }
        });
    }

    /* **************************************************************************************** **/

    @NonNull
    public static String cumulativeErrorMessage(int statusCode, Throwable throwable, JSONObject errorResponse) {
        StringBuilder sb = new StringBuilder();
        sb.append("code=").append(statusCode).append("; ");
        if (throwable != null) sb.append(throwable.getMessage()).append("; ");
        if (errorResponse != null) sb.append(errorResponse.toString());

        return sb.toString();
    }

    @NonNull
    public static String cumulativeErrorMessage(int statusCode, String responseString, Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append("code=").append(statusCode).append("; ");
        if (throwable != null) sb.append(throwable.getMessage()).append("; ");
        sb.append(responseString);

        return sb.toString();
    }
    
    /* **************************************************************************************** **/

    public interface CreateResponse {
        void onSuccess(int returnId);

        void onFailure(String message);
    }

    public interface NoArgumentResponse {
        void onSuccess();

        void onFailure(String message);
    }

    public interface ListResponse<T> {
        void onSuccess(ArrayList<T> objects);

        void onFailure(String message);
    }

    public interface ListWithCountResponse<T> {
        void onSuccess(ArrayList<T> objects, int count);

        void onFailure(String message);
    }

    public interface SingleObjectResponse<T> {
        void onSuccess(T object);

        void onFailure(String message);
    }

    public static class GeocodingResponse {
        public void onResponse(JSONObject response) {
        }

        public void onFirstGeometry(double latitude, double longitude) {
        }

        public void onFailure(String message) {
        }
    }

    // Will replace every JsonHttpResponseHandler
    abstract static class SimpleResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            onFailure(statusCode + ": " + (errorResponse != null ? errorResponse.toString() : "unknown"));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            onFailure(statusCode + ": " + (errorResponse != null ? errorResponse.toString() : "unknown"));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            onFailure(statusCode + ": " + (responseString != null ? responseString : "unknown"));
        }

        public void onFailure(String message) {
            if (BuildConfig.DEBUG) Log.d("FAIL RESPOND", message);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            onSuccess(response);
        }

        public abstract void onSuccess(JSONObject response);
    }
}