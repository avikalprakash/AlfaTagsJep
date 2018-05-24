package com.organization.jeptagsalpha.wordpress.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @author Jan-Louis Crafford
 *         Created on 2016/02/26.
 */
public class LoginAccountHelper extends BasePreferenceHelper {

    private static final String APP_PREFS = "JEP_TAGS";

    /* USER CONSTANTS */
    private static final String PREF_USER_HAS_PROFILE = "USER_HAS_PROFILE";
    private static final String PREF_USER_ID = "USER_ID";
    private static final String PREF_USER_EMAIL = "USER_EMAIL";
    private static final String PREF_USER_NAME = "USER_NAME";
    private static final String PREF_USER_DISPLAYNAME = "DISPLAY_NAME";
    private static final String PREF_USER_PASSWORD = "PASSWORD";
    private static final String PREF_USER_FIRST_NAME = "USER_FIRST_NAME";
    private static final String PREF_USER_LAST_NAME = "USER_LAST_NAME";
    private static final String PREF_USER_PROFILE_PIC = "USER_PROFILE_PIC";
    private static final String PREF_USER_ACC_TYPE = "USER_ACC_TYPE";
    private static final String PREF_USER_ROLE = "USER_ROLE";
    private static final String PREF_IS_LOGGED_IN = "USER_LOGGED_IN";
    private static final String PREF_PROD_ID = "PROD_ID";

    public static final String ACC_TYPE_FACEBOOK = "facebook";
    public static final String ACC_TYPE_GOOGLE = "google";

    private static LoginAccountHelper sInstance = null;

   /* public static LoginAccountHelper with(Context context) {
        if (sInstance == null) {
            sInstance = new LoginAccountHelper(context);
        }

        return sInstance;
    }*/

    public LoginAccountHelper(Context context) {
        if (context == null) {
            throw new IllegalStateException("Context can not be null!");
        }
        mContext = context;
    }

    @Override
    protected String getAppPreferenceName() {
        return APP_PREFS;
    }

    public void resetUserState() {
        SharedPreferences preferences = getPreferences();
        setUser(0,"","","","","","","","",false);
       // preferences.edit().clear().commit();
    }

    // USER PREFERENCES

    public LoginAccountHelper setUserHasProfile(boolean hasProfile) {
        String pref = this.getString(PREF_USER_HAS_PROFILE);
        putBooleanPref(pref, hasProfile);
        return this;
    }

    public boolean getUserHasProfile() {
        String pref = this.getString(PREF_USER_HAS_PROFILE);
        return getBooleanPref(pref);
    }

    public LoginAccountHelper setUserId(String id) {
        String pref = this.getString(PREF_USER_ID);
        putStringPref(pref, id);
        return this;
    }
    public String getString(String string)
    {
        return string;
    }
    public String getProdId() {
        String pref = this.getString(PREF_PROD_ID);
        return getStringPref(pref);
    }
    public LoginAccountHelper setProdId(String id) {
        String pref = this.getString(PREF_PROD_ID);
        putStringPref(pref, id);
        return this;
    }

    public String getUserId() {
        String pref = this.getString(PREF_USER_ID);
        return getStringPref(pref);
    }

    public LoginAccountHelper setUserEmail(String email) {
        String pref = this.getString(PREF_USER_EMAIL);
        putStringPref(pref, email);
        return this;
    }

    public String getUserEmail() {
        String pref = this.getString(PREF_USER_EMAIL);
        return getStringPref(pref);
    }

    public LoginAccountHelper setUserName(String name) {
        String pref = this.getString(PREF_USER_NAME);
        putStringPref(pref, name);
        return this;
    }

    public String getUserName() {
        String pref = this.getString(PREF_USER_NAME);
        return getStringPref(pref);
    }
    public LoginAccountHelper setPassword(String password) {
        String pref = this.getString(PREF_USER_PASSWORD);
        putStringPref(pref, password);
        return this;
    }

    public String getPassword() {
        String pref = this.getString(PREF_USER_PASSWORD);
        return getStringPref(pref);
    }

    public LoginAccountHelper setUserFirstName(String firstName) {
        String pref = this.getString(PREF_USER_FIRST_NAME);
        putStringPref(pref, firstName);
        return this;
    }

    public String getUserFirstName() {
        String pref = this.getString(PREF_USER_FIRST_NAME);
        return getStringPref(pref);
    }

    public LoginAccountHelper setUserLastName(String lastName) {
        String pref = this.getString(PREF_USER_LAST_NAME);
        putStringPref(pref, lastName);
        return this;
    }
    public boolean getUserLoggedIn() {
        String pref = this.getString(PREF_IS_LOGGED_IN);
        return getBooleanPref(pref);
    }

    public LoginAccountHelper setUserLoggedIn(boolean loggedin) {
        String pref = this.getString(PREF_IS_LOGGED_IN);
        putBooleanPref(pref, loggedin);
        return this;
    }
    public LoginAccountHelper setUser(long id,String username,String password, String displayname,String firstName,String lastName,String email,String role,String profilePic,boolean loggedin) {

        putStringPref(PREF_USER_ID, String.valueOf(id));
        putStringPref(PREF_USER_NAME, username);
        putStringPref(PREF_USER_PASSWORD, password);
        putStringPref(PREF_USER_DISPLAYNAME, displayname);
        putStringPref(PREF_USER_FIRST_NAME, firstName);
        putStringPref(PREF_USER_LAST_NAME, lastName);
        putStringPref(PREF_USER_EMAIL, email);
        putStringPref(PREF_USER_ROLE, role);
        putStringPref(PREF_USER_PROFILE_PIC, profilePic);
        putBooleanPref(PREF_IS_LOGGED_IN,loggedin);
        return this;
    }

    public String getUserLastName() {
        String pref = this.getString(PREF_USER_LAST_NAME);
        return getStringPref(pref);
    }

    public String getUserRole() {
        String pref = this.getString(PREF_USER_ROLE);
        return getStringPref(pref);
    }

    public LoginAccountHelper setUserFullName(String fullname) {
        String pref = this.getString(PREF_USER_DISPLAYNAME);
        putStringPref(pref, fullname);
        return this;
    }
    public String getUserFullName() {
        String pref = this.getString(PREF_USER_DISPLAYNAME);
        return getStringPref(pref);
    }

    public LoginAccountHelper setUserProfilePic(String pic) {
        String pref = this.getString(PREF_USER_PROFILE_PIC);
        putStringPref(pref, pic);
        return this;
    }

    public String getUserProfilePic() {
        String pref = this.getString(PREF_USER_PROFILE_PIC);
        return getStringPref(pref);
    }

    public LoginAccountHelper setUserAccType(String type) {
        String pref = this.getString(PREF_USER_ACC_TYPE);
        putStringPref(pref, type);
        return this;
    }

    public String getUserAccType() {
        String pref = this.getString(PREF_USER_ACC_TYPE);
        return getStringPref(pref);
    }
}
