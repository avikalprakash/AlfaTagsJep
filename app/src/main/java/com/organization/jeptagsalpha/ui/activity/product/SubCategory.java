package com.organization.jeptagsalpha.ui.activity.product;

import org.json.JSONException;
import org.json.JSONObject;


public class SubCategory {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name="";

    public SubCategory(JSONObject jsonObject)
    {

        if(jsonObject!=null)
        {
            try {
                this.id=jsonObject.getInt("subcat_id");
            } catch (JSONException e) {}

            try {
                this.name=jsonObject.getString("name");
            } catch (JSONException e) {}
        }

    }
}
