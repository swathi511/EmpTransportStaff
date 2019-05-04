package com.hjsoft.emptransportstaff.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hjsoft on 13/6/17.
 */
public class Pojo {

    @SerializedName("Message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
