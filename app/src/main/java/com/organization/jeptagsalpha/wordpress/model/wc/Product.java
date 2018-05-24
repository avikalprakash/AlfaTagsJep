package com.organization.jeptagsalpha.wordpress.model.wc;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Product implements Serializable,Cloneable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    @SerializedName("categories")
    private List<Category> categories;

    @SerializedName("dimensions")
    private Dimensions dimensions;

    @SerializedName("weight")
    private String weight;

    @SerializedName("images")
    private List<Image> images;

    @SerializedName("description")
    private String description;

    @SerializedName("attributes")
    private List<Attribute> attributes;

    @SerializedName("regular_price")
    private String regular_price;

    @SerializedName("sale_price")
    private String sale_price;

    public Product() {
    }

    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public void setRegular_price(String regular_price) {
        this.regular_price = regular_price;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public String getCategoriesToString()
    {
        String data="";
        for (Category category:getCategories()) {
            data = data + category.getName()+",";
        }
        return data;
    }
    public String getImageURL()
    {
        String url="";
        for (Image image:getImages()) {
            return image.getSrc();
        }
        return url;
    }
}
