package com.organization.jeptagsalpha.ui.activity.product;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetails {

    public ProductDetails() {

    }

    public String getApproved() {
        return approved;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getProduct_url() {
        return product_url;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public String getProduct_latitude() {
        return latitude;
    }

    public String getProduct_longitude() {
        return longitude;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public void setProduct_qty(String product_qty) {
        this.product_url = product_qty;
    }

    public void setProduct_latitude(String latitude) {
        this.latitude = latitude;
    }

    public void setProduct_longitude(String longitude) {
        this.longitude = longitude;
    }

    String product_id="";
    String approved="";
    String name="";
    String category="";
    String image="";
    String product_url="";
    String product_qty="";
    String latitude="";
    String longitude="";


    public ProductDetails(JSONObject jsonObject)
    {
        try {
            this.approved=jsonObject.getString("approved");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.product_id=jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.name=jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.category=jsonObject.getString("category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.image=jsonObject.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.product_url=jsonObject.getString("product_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.product_qty=jsonObject.getString("qty");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.latitude=jsonObject.getString("latitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.longitude=jsonObject.getString("longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
