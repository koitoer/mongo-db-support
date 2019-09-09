package com.koitoer.spring.mongodb.example;

import java.io.Serializable;

/**
 * Created by mauricio.mena on 25/04/2016.
 */
public class DataContentResult implements Serializable{

    String publisherId;
    long total;
    private Integer name;
    private Integer zip;

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }
}
