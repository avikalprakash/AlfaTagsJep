package com.organization.jeptagsalpha.ui.activity.product;


import org.json.JSONObject;

public class BrandList {
    int id;
    String name="";

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  BrandList(JSONObject jsonObject){
        if (jsonObject!=null){
            try {
                this.id=jsonObject.getInt("value");
            }catch (Exception e){}
            try {
                this.name=jsonObject.getString("label");
            }catch (Exception e){}
        }

    }
}
