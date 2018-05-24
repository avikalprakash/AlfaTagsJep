package com.organization.jeptagsalpha.ui.activity.product;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductAddressDetails {

    public ProductAddressDetails(String entity_id) {

    }
        public String getFirstname () {
            return firstname;
        }

    public String getLastname() {
        return lastname;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country_id;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getPhoneNumber() {
        return telephone;
    }

    public String getGeo_latitude() {
        return geo_latitude;
    }

    public String getGeo_longitude() {
        return geo_longitude;
    }

    public String getBilling() {
        return is_default_billing;
    }

    public String getShipping() {
        return is_default_shipping;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProduct_url(String country_id) {
        this.country_id = country_id;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setPhoneNumber(String telephone) {
        this.telephone = telephone;
    }

    public void setBilling(String is_default_billing) {
        this.is_default_billing = is_default_billing;
    }

    public void setShipping(String is_default_shipping) {
        this.is_default_shipping = is_default_shipping;
    }
    public void setGeoLatitude(String geo_latitude) {
        this.geo_latitude = geo_latitude;
    }
    public void setGeoLongitude(String geo_longitude) {
        this.geo_longitude = geo_longitude;
    }


    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    String firstname = "";
    String lastname = "";
    String street = "";
    String city = "";
    String country_id = "";
    String postcode = "";
    String telephone = "";
    String is_default_billing = "";
    String is_default_shipping = "";
    String entity_id;
    String geo_latitude;
    String geo_longitude;







    public ProductAddressDetails(JSONObject jsonObject)
    {
        try {
            this.firstname=jsonObject.getString("firstname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.lastname=jsonObject.getString("lastname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.street=jsonObject.getString("street");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.city=jsonObject.getString("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.country_id=jsonObject.getString("country_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.postcode=jsonObject.getString("postcode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.telephone=jsonObject.getString("telephone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.is_default_billing=jsonObject.getString("is_default_billing");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.is_default_shipping=jsonObject.getString("is_default_shipping");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.entity_id=jsonObject.getString("entity_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.geo_latitude=jsonObject.getString("geo_latitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.geo_longitude=jsonObject.getString("geo_longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public  ProductAddressDetails(){
        this.firstname=firstname;
        this.lastname=lastname;
        this.street=street;
        this.city=city;
        this.country_id=country_id;
        this.postcode=postcode;
        this.telephone=telephone;
        this.is_default_billing=is_default_billing;
        this.is_default_shipping=is_default_shipping;
        this.entity_id=entity_id;
    }
}

