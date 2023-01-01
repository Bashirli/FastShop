package com.bashirli.fastshop.service;

import com.bashirli.fastshop.model.RetrofitResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    @GET("/products")
    Observable<List<RetrofitResponse>> getAll();

    @GET("/products/category/jewelery")
    Observable<List<RetrofitResponse>> getJewelery();

    @GET("/products/category/electronics")
    Observable<List<RetrofitResponse>> getElectronics();

    @GET("/products/category/men's clothing")
    Observable<List<RetrofitResponse>> getMenCat();

    @GET("/products/category/women's clothing")
    Observable<List<RetrofitResponse>> getWomenCat();

    @GET("/products")
    Observable<List<RetrofitResponse>> getSingleObject(@Query("id") int i);

}
