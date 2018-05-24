package com.organization.jeptagsalpha.ui.model;


public class Product {
    private String id;
    private String name;
    private String image;
    private String cateogry;

    public Product(String id) {
        this.id = id;
    }

    public Product(String cateogry, String id, String image, String name) {
        this.cateogry = cateogry;
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public String getCateogry() {
        return cateogry;
    }

    public void setCateogry(String cateogry) {
        this.cateogry = cateogry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
