package com.teamx.soon.item;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.teamx.soon.AppDelegate;
import com.teamx.soon.GlobalConst;

/**
 * Created by ruler_000 on 04/04/2015.
 * Project: SoYBa
 */
public class User {

    private static User currentUser = null;

    public int id = -1;
    public String name;
    public String accId;
    public String facebookAccessToken;
    public String email;
    public String photoUrl;
    public int age;
    public int gender;
    public String username;

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
            user.accId = sp.getString(GlobalConst.SPK_USER_ACC_ID, null);
            user.photoUrl = sp.getString(GlobalConst.SPK_USER_PHOTO, null);
            user.email = sp.getString(GlobalConst.SPK_USER_EMAIL, null);
            return user;
        }
    }

    public static void saveUser(@NonNull User user) {
        User.currentUser = user;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AppDelegate.getInstance()).edit();
        editor.putInt(GlobalConst.SPK_USER_ID, user.id);
        editor.putString(GlobalConst.SPK_USER_NAME, user.name);
        editor.putString(GlobalConst.SPK_USER_ACC_ID, user.accId);
        editor.putString(GlobalConst.SPK_USER_EMAIL, user.email);
        editor.putString(GlobalConst.SPK_USER_PHOTO, user.photoUrl);
        editor.apply();
    }

    public static User getUserByAccId(String accId) {
        User user = loadUser();
        if (user != null && user.accId.equals(accId)) return user;
        else return null;
    }


}
