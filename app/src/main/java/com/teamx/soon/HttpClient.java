package com.teamx.soon;

/**
 * Created by Nguyen Duc Thinh on 11/02/2015.
 * Project type: Android
 */

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.teamx.soon.activity.EventActivity;
import com.teamx.soon.item.Event;
import com.teamx.soon.item.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HttpClient {
    private static final String BASE_URL = "http://api.meboo.vn/";

    private static final String GEOCODING = "http://maps.googleapis.com/maps/api/geocode/json";

    private static final String SUB_GET_USER = "user/getuser";

    private static final String SUB_CREATE_USER = "user/createuser";

    private static final String SUB_LOGIN_WITH_FACEBOOK = "user/loginWithFacebook";

    private static final String SUB_GET_EVENT = "event/getEvent";
    private static final String SUB_GET_EVENT_BY_USER = "http://128.199.167.255/hatch/event/getEventByUser";

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

    public static RequestHandle getUser(final int accType, String accId, final SingleObjectResponse<User> userResponse) {
        RequestParams params = new RequestParams();
        if (accType == GlobalConst.ACC_TYPE_FB) params.put("facebook_id", accId);
        else if (accType == GlobalConst.ACC_TYPE_GG) params.put("google_id", accId);

        return post(SUB_GET_USER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response.isNull("data")) {
                    userResponse.onSuccess(null);
                } else {
                    JSONObject userData = response.optJSONObject("user_data");

                    User user = new User();
                    user.id = userData.optInt("user_id");
                    user.name = userData.optString("name");
                    if (accType == GlobalConst.ACC_TYPE_FB) {
                        user.accId = userData.optString("facebook_id");
                        user.facebookAccessToken = userData.optString("facebook_access_token");
                    } else if (accType == GlobalConst.ACC_TYPE_GG) {
                        user.accId = userData.optString("google_id");
                    }
                    user.email = userData.optString("email");
                    user.photoUrl = userData.optString("photo");
                    user.lastUpdated = userData.optLong("last_updated");

                    userResponse.onSuccess(user);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                userResponse.onFailure(throwable.getMessage());
            }
        });
    }

    public static RequestHandle createUser(@NonNull User user, final CreateResponse createResponse) {
        RequestParams params = new RequestParams();

        params.put("name", user.name);
        params.put("email", user.email);
        params.put("photo", user.photoUrl);
        params.put("last_updated", user.lastUpdated);

        return post(SUB_CREATE_USER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                int newUserId = response.optInt("user_data");
                createResponse.onSuccess(newUserId);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                createResponse.onFailure(throwable.getMessage());
            }
        });
    }

    public static RequestHandle facebookLogin(@NonNull User user, @NonNull String deviceId, final CreateResponse createResponse) {
        RequestParams params = new RequestParams();

        params.put("facebook_id", user.accId);
        params.put("age", user.age + "");
        params.put("gender", user.gender + "");
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
                } catch (JSONException e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                createResponse.onFailure(throwable.getMessage());
            }
        });
    }

    public static RequestHandle getEvent(@NonNull int limit, @NonNull int offset, final CreateResponse createResponse) {
        RequestParams params = new RequestParams();

        params.put("limit", limit);
        params.put("offset", offset);

        return get(SUB_GET_EVENT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray eventJson = response.getJSONArray("data");
                    ListResponse<Event> eventResponse = new ListResponse<Event>() {
                        @Override
                        public void onSuccess(ArrayList<Event> objects) {

                        }

                        @Override
                        public void onFailure(String message) {

                        }
                    };

                    ArrayList<Event> eventList = new ArrayList<Event>();
                    for (int i = 0; i < eventJson.length(); i++) {
                        JSONObject c = eventJson.getJSONObject(i);

                        String name = c.getString("name");
                        String image = getAbsoluteUrl(c.getString("image"));
                        String description = c.getString("description");
                        String status = c.getString("status");
                        String date = c.getString("date");
                        String address = c.getString("address");
                        String type = c.getString("type");

                        Event event = new Event(name, image, address, date, type, status, description);
                        eventList.add(event);
                    }
                    eventResponse.onSuccess(eventList);
                } catch (JSONException e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                createResponse.onFailure(throwable.getMessage());
            }
        });
    }

    public static RequestHandle getEventByUser(@NonNull String userId, @NonNull int limit, @NonNull int offset, final CreateResponse createResponse) {
        RequestParams params = new RequestParams();

        params.put("user_id", userId);
        params.put("limit", limit);
        params.put("offset", offset);

        return get(SUB_GET_EVENT, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray eventJson = response.getJSONArray("data");
                    ListResponse<Event> eventResponse = new ListResponse<Event>() {
                        @Override
                        public void onSuccess(ArrayList<Event> objects) {

                        }

                        @Override
                        public void onFailure(String message) {

                        }
                    };

                    ArrayList<Event> eventList = new ArrayList<Event>();
                    for (int i = 0; i < eventJson.length(); i++) {
                        JSONObject c = eventJson.getJSONObject(i);

                        String name = c.getString("name");
                        String image = getAbsoluteUrl(c.getString("image"));
                        String description = c.getString("description");
                        String status = c.getString("status");
                        String date = c.getString("date");
                        String address = c.getString("address");
                        String type = c.getString("type");

                        Event event = new Event(name, image, address, date, type, status, description);
                        eventList.add(event);
                    }
                    eventResponse.onSuccess(eventList);
                } catch (JSONException e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                createResponse.onFailure(throwable.getMessage());
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