package com.organization.jeptagsalpha.wordpress.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.organization.jeptagsalpha.wordpress.util.Validate;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by Lord Ganesha on 15/09/2016.
 */

public class Token extends  BaseModel {

    public static final String JSON_FIELD_TOKEN = "token";
    public static final String JSON_FIELD_EMAIL = "user_email";
    public static final String JSON_FIELD_NAME = "user_nicename";
    public static final String JSON_FIELD_DISPLAY_NAME = "user_display_name";
    public static final String JSON_FIELD_ID = "user_id";


    public Token(Parcel in) {
        super(in);
        email = in.readString();
        token = in.readString();
        displayname = in.readString();
        nicename = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(email);
        dest.writeString(token);
        dest.writeString(displayname);
        dest.writeString(nicename);
        dest.writeString(id);
    }
    @SerializedName("user_id")
    private String id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @SerializedName("user_email")
    private String email;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("token")
    private String token;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("user_nicename")
    private String nicename;
    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    @SerializedName("user_display_name")
    private String displayname;
    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
    public static Map<String, Object> mapFromFields(Token token) {
        Map<String, Object> map = new HashMap<>();
        Validate.validateMapEntry(JSON_FIELD_TOKEN, token.getToken(), map);
        Validate.validateMapEntry(JSON_FIELD_EMAIL, token.getEmail(), map);
        Validate.validateMapEntry(JSON_FIELD_DISPLAY_NAME, token.getDisplayname(), map);
        Validate.validateMapEntry(JSON_FIELD_NAME, token.getNicename(), map);
        Validate.validateMapEntry(JSON_FIELD_ID, token.getId(), map);

        return map;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public String toString() {
        return  "";
    }
    public static final Parcelable.Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
}
