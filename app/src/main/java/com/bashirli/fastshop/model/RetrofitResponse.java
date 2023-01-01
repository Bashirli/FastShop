package com.bashirli.fastshop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RetrofitResponse implements Serializable {
    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;

    @SerializedName("price")
    public String price;

    @SerializedName("description")
    public String description;

    @SerializedName("category")
    public String category;

    @SerializedName("image")
    public String imageURL;

    @SerializedName("rating")
    public RetrofitResponseRatings ratings;

    public RetrofitResponse() {

    }
}
