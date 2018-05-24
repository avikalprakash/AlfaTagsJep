package com.organization.jeptagsalpha.ui.activity.product;

import org.json.JSONException;
import org.json.JSONObject;


public class JepTagList {
    public String getJepTag_id() {
        return jeptag_id;
    }

    public String getJepTagTime() {
        return time;
    }

    public void setJeptag_id(String jeptag_id) {
        this.jeptag_id = jeptag_id;
    }

    public void setJepTagTime(String time) {
        this.time = time;
    }

    String jeptag_id="";
    String time="";
    public JepTagList(JSONObject jsonObject){
        try {
            this.jeptag_id=jsonObject.getString("jeptag_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.time=jsonObject.getString("updated_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public  JepTagList(String jeptag_id,String updated_at){
        this.jeptag_id=jeptag_id;
        this.time=updated_at;
    }
}
