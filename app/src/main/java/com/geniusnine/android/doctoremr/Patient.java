package com.geniusnine.android.doctoremr;

/**
 * Created by AndriodDev8 on 15-02-2017.
 */

public class Patient {

    @com.google.gson.annotations.SerializedName("id")
    private String id;
    @com.google.gson.annotations.SerializedName("fname")
    private String fname;
    @com.google.gson.annotations.SerializedName("lname")
    private String lname;

    public Patient() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
