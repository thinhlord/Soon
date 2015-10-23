package com.teamx.soon;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * Created by ruler_000 on 04/04/2015.
 * Project: SoYBa
 */
public class User {

    private static User currentUser = null;

    public int id = -1;
    public String name;
    public int accType = -1;
    public String accId;
    public String facebookAccessToken;
    public int gender = -1;
    public String email;
    public String phoneNumber;
    public String wardId;
    public String photoUrl;

    public long lastUpdated;


    public static User getCurrentUser() {
        if (currentUser == null) {
            currentUser = loadUser();
        }
        return currentUser;
    }


    public static void setCurrentUserId(int currentUserId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AppDelegate.getInstance()).edit();
        editor.putInt(GlobalConst.SPK_USER_ID, currentUserId);
        if (currentUserId == -1) editor.putInt(GlobalConst.SPK_FIRST_TIME, -1);
        editor.apply();
    }

    public static void clearCurrentUser() {
        //DbController.getInstance().removeUserDb();
        setCurrentUserId(-1);
        currentUser = null;
    }

    public static User loadUser() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AppDelegate.getInstance());
        if (sp.getInt(GlobalConst.SPK_USER_ID, -1) == -1){
            return null;
        }
        else {
            User user = new User();
            user.id = sp.getInt(GlobalConst.SPK_USER_ID, -1);
            user.name = sp.getString(GlobalConst.SPK_USER_NAME, null);
            user.accType = sp.getInt(GlobalConst.SPK_USER_ACC_TYPE, 0);
            user.accId = sp.getString(GlobalConst.SPK_USER_ACC_ID, null);
            user.gender = sp.getInt(GlobalConst.SPK_USER_GENDER, 0);
            user.photoUrl = sp.getString(GlobalConst.SPK_USER_PHOTO, null);
            user.email = sp.getString(GlobalConst.SPK_USER_EMAIL, null);
            user.phoneNumber = sp.getString(GlobalConst.SPK_USER_PHONE, null);
            user.wardId = sp.getString(GlobalConst.SPK_USER_WARD_ID, null);
            return user;
        }
    }

    public static void saveUser(@NonNull User user) {
        User.currentUser = user;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AppDelegate.getInstance()).edit();
        editor.putInt(GlobalConst.SPK_USER_ID, user.id);
        editor.putString(GlobalConst.SPK_USER_NAME, user.name);
        editor.putInt(GlobalConst.SPK_USER_ACC_TYPE, user.accType);
        editor.putString(GlobalConst.SPK_USER_ACC_ID, user.accId);
        editor.putInt(GlobalConst.SPK_USER_GENDER, user.gender);
        editor.putString(GlobalConst.SPK_USER_EMAIL, user.email);
        editor.putString(GlobalConst.SPK_USER_PHONE, user.phoneNumber);
        editor.putString(GlobalConst.SPK_USER_WARD_ID, user.wardId);
        editor.putString(GlobalConst.SPK_USER_PHOTO, user.photoUrl);
        editor.apply();
    }

    public static User getUserByAccId(String accId) {
        User user = loadUser();
        if (user != null && user.accId.equals(accId)) return user;
        else return null;
    }


}
