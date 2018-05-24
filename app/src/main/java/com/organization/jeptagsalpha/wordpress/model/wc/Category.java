package com.organization.jeptagsalpha.wordpress.model.wc;


import java.io.Serializable;

/**
 * Created by Lord Ganesha on 26/09/2016.
 */

public class Category implements Serializable{
    private int id;
    private String name;
    private String slug;

    public Category()
    {

    }
    public Category(int id,String name) {
        this.id = id;
        this.name=name;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return getName();
    }
}
