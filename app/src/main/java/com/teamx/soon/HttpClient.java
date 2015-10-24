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
import com.teamx.soon.item.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HttpClient {
    private static final String BASE_URL = "http://api.meboo.vn/";

    private static final String GEOCODING = "http://maps.googleapis.com/maps/api/geocode/json";

    private static final String SUB_GET_SICK_PATIENT = "patient/getsickpatient";
    private static final String SUB_GET_PATIENT_INJECTION = "patient/getpatientinjection";
    private static final String SUB_GET_PATIENT = "patient/getpatientuser";
    private static final String SUB_GET_USER = "user/getuser";
    private static final String SUB_GET_PHARMACIES = "pharmacy/getPharmacy";
    private static final String SUB_GET_PHARMACIES_COUNT = "pharmacy/getPharmacyCount";
    private static final String SUB_GET_HEIGHT_WEIGHT = "patient/getHeightWeight";
    private static final String SUB_GET_MEDICINE_REMINDS = "medicineRemind/GetMedicineRemindOfPatient";
    private static final String SUB_GET_EVERYTHING = "super/superapi";

    private static final String SUB_EDIT_USER = "user/updateInfoUser";
    private static final String SUB_EDIT_PATIENT = "patient/updatepatient";
    private static final String SUB_EDIT_SCHEDULE = "patient/updateis";
    private static final String SUB_EDIT_SICK_PATIENT = "sick/updatesickpatient";
    private static final String SUB_EDIT_MEDICINE_REMIND = "medicineRemind/editRemind";
    private static final String SUB_EDIT_PHARMACY_PLACE = "pharmacy/AddPharmacyCoordinates";

    private static final String SUB_CREATE_USER = "user/createuser";
    private static final String SUB_CREATE_PATIENT = "patient/createpatientuser";
    private static final String SUB_CREATE_SICK_PATIENT = "sick/createsickuser";
    private static final String SUB_CREATE_SUPER_PATIENT = "super/createpatientandsick";
    private static final String SUB_CREATE_HEIGHT_WEIGHT = "patient/createHeightWeight";
    private static final String SUB_CREATE_MEDICINE_REMIND = "medicineRemind/addRemind";
    private static final String SUB_CREATE_DOCTOR = "doctor/addDoctor";
    private static final String SUB_CREATE_PHARMACY = "pharmacy/createPharmacy";

    private static final String SUB_DELETE_PATIENT = "patient/deleteapatient";
    private static final String SUB_DELETE_MEDICINE_REMIND = "medicineRemind/deleteRemind";

    private static final String SUB_POST_REVIEW = "review/addReview";
    private static final String SUB_DELETE_REVIEW = "review/deleteReview";
    private static final String SUB_GET_REVIEWS = "review/getReview";

    private static final String SUB_GET_DOCTORS = "doctor/getDoctor";
    private static final String SUB_GET_MY_DOCTORS = "doctor/getDoctorByUser";
    private static final String SUB_GET_DOCTOR_COUNT = "doctor/countRecord";
    private static final String SUB_GET_MY_DOCTOR_COUNT = "doctor/countRecordByUser";
    private static final String SUB_GET_DOCTORS_FILTERED = "doctor/SearchDoctorByAddressAndKeywords";

    private static final String SUB_GET_MY_PHARMACY = "pharmacy/getPharmacyByUser";
    private static final String SUB_GET_MY_PHARMACY_COUNT = "pharmacy/countRecordByUser";
    private static final String SUB_GET_PHARMACIES_FILTERED = "pharmacy/SearchPharmacyByAddressAndKeywords";
    private static final String SUB_GET_CLINICS = "clinic/getClinic";

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

    public static RequestHandle freeGeocoding(@NonNull String address, @Nullable final GeocodingResponse geocodingResponse) {
        RequestParams params = new RequestParams();
        params.put("address", address);

        return client.get(GEOCODING, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (geocodingResponse != null) geocodingResponse.onResponse(response);

                try {
                    String responseStr = response.getString("status");

                    if (responseStr.equalsIgnoreCase("OK")) {
                        JSONObject locationObj = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                        double latitude = locationObj.getDouble("lat");
                        double longitude = locationObj.getDouble("lng");

                        if (geocodingResponse != null)
                            geocodingResponse.onFirstGeometry(latitude, longitude);
                    } else {
                        if (geocodingResponse != null) geocodingResponse.onFailure(responseStr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (geocodingResponse != null) geocodingResponse.onFailure(e.getMessage());
                }
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