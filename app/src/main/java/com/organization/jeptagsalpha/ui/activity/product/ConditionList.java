package com.organization.jeptagsalpha.ui.activity.product;


import org.json.JSONObject;

public class ConditionList {
    int value;
    String label="";

    public void setValue(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public  ConditionList(JSONObject jsonObject){
        if (jsonObject!=null){
            try {
                this.value=jsonObject.getInt("value");
            }catch (Exception e){}
            try {
                this.label=jsonObject.getString("label");
            }catch (Exception e){}
        }

    }
}
