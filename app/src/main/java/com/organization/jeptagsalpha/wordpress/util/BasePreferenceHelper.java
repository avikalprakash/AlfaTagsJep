package com.organization.jeptagsalpha.wordpress.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.ui.activity.product.ProductAddressDetails;

/**
 * @author Jan-Louis Crafford
 *         Created on 2016/01/04.
 */
public abstract class BasePreferenceHelper {

    private static final String DEFAULT_STRING = null;
    private static final long DEFAULT_LONG = -1L;
    private static final int DEFAULT_INT = -1;
    private static final float DEFAULT_FLOAT = -1F;
    private static final boolean DEFAULT_BOOLEAN = false;
    public final static String BACKGROUND="bakcground";
    public final static String SELECTED_OPTION="selected_option";

    public final static String LOGIN_DETAILS="login_details";

    public final static String ENTITY_ID="entity_id";
    public final static String FIRST_NAME="first_name";
    public final static String LAST_NAME="last_name";
    public final static String EMAIL="email";
    public final static String PASSWORD="password";
    public final static String SAVE_LOGIN="savelogin";
    protected Context mContext;


    public final static String ADDRESS_DETAILS="address_details";
    public final static String ADDRESS_ID="entity_id";


    /* APP SPECIFIC METHODS */



    public static void setLoginDetails(Context context, UserLoginDetails userLoginDetails) {
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_DETAILS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ENTITY_ID,userLoginDetails.getEntity_id());
        editor.putString(FIRST_NAME,userLoginDetails.getFirstname());
        editor.putString(LAST_NAME,userLoginDetails.getLastname());
        editor.putString(EMAIL,userLoginDetails.getEmail());
        editor.putString(PASSWORD,userLoginDetails.getPassword());
        editor.putBoolean(SAVE_LOGIN,userLoginDetails.isSavelogin());
        editor.commit();
    }
    public static UserLoginDetails getLoginDetails(Context context){
        SharedPreferences prefs = context.getSharedPreferences(LOGIN_DETAILS,Context.MODE_PRIVATE);
       UserLoginDetails userLoginDetails=new UserLoginDetails();

        userLoginDetails.setEntity_id(prefs.getString(ENTITY_ID,""));
        userLoginDetails.setFirstname(prefs.getString(FIRST_NAME,""));
        userLoginDetails.setLastname(prefs.getString(LAST_NAME,""));
        userLoginDetails.setEmail(prefs.getString(EMAIL,""));
        userLoginDetails.setPassword(prefs.getString(PASSWORD,""));
        userLoginDetails.setSavelogin(prefs.getBoolean(SAVE_LOGIN,false));
        return userLoginDetails;

    }

    public static void setAddressId(Context context, ProductAddressDetails productAddressDetails){
        SharedPreferences prefs = context.getSharedPreferences(ADDRESS_DETAILS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ADDRESS_ID,productAddressDetails.getEntity_id());

    }

    public static ProductAddressDetails getAddressId(Context context){
        SharedPreferences prefs = context.getSharedPreferences(ADDRESS_DETAILS,Context.MODE_PRIVATE);
        ProductAddressDetails productAddressDetails=new ProductAddressDetails();
        productAddressDetails.setEntity_id(prefs.getString(ADDRESS_ID,""));
        return productAddressDetails;

    }




    public SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(getAppPreferenceName(), Context.MODE_PRIVATE);
    }

    protected abstract String getAppPreferenceName();

    /* INTERNAL METHODS */

    protected void putStringPref(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }




    protected void putLongPref(String key, long value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        editor.apply();
    }

    protected void putBooleanPref(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    protected void putIntegerPref(String key, int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    protected void putFloatPrefs(String key, float value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    protected String getStringPref(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    protected String getStringPref(String key) {
        return getStringPref(key, DEFAULT_STRING);
    }

    protected long getLongPref(String key, long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    protected long getLongPref(String key) {
        return getLongPref(key, DEFAULT_LONG);
    }

    protected int getIntegerPref(String key, int defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    protected int getIntegerPref(String key) {
        return getIntegerPref(key, DEFAULT_INT);
    }

    protected float getFloatPref(String key, float defaultValue) {
        return getPreferences().getFloat(key, defaultValue);
    }

    protected float getFloatPref(String key) {
        return getFloatPref(key, DEFAULT_FLOAT);
    }

    protected boolean getBooleanPref(String key, boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    protected boolean getBooleanPref(String key) {
        return getBooleanPref(key, DEFAULT_BOOLEAN);
    }

    protected void removePref(String key) {
        getPreferences().edit().remove(key).commit();
    }
}
