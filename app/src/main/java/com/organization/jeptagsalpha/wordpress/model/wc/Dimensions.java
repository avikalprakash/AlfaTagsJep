package com.organization.jeptagsalpha.wordpress.model.wc;

import java.io.Serializable;

/**
 * Created by Lord Ganesha on 27/09/2016.
 */

public class Dimensions implements Serializable{
    private String length,width,height;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
