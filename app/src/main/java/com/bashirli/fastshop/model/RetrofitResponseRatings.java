package com.bashirli.fastshop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RetrofitResponseRatings implements Serializable {
    @SerializedName("rate")
    public String rating;
    @SerializedName("count")
    public String count;
}
