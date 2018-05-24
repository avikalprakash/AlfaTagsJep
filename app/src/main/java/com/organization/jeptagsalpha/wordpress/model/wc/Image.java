package com.organization.jeptagsalpha.wordpress.model.wc;

import java.io.Serializable;

/**
 * Created by Lord Ganesha on 27/09/2016.
 */

public class Image implements Serializable {

    public Image(long id) {
        this.id = id;
    }
    public Image(long id,long position) {
        this.id = id;
        this.position = position;
    }
    public long position;
    private long id;
    private String name,src,alt;


    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
