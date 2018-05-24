package com.organization.jeptagsalpha.wordpress.model.wc;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Lord Ganesha on 27/09/2016.
 */

public class Attribute implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("options")
    private String[] options;

    public Attribute() {

    }

    public Attribute(int id, String[] options) {
        this.id = id;
        this.options = options;
    }

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


    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
