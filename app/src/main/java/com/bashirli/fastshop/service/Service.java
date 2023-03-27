package com.bashirli.fastshop.service;

import com.bashirli.fastshop.model.RetrofitResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {
     API api=new Retrofit.Builder()
            .baseUrl("xxx")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(API.class);


    public Observable<List<RetrofitResponse>> getAllData(){
    return api.getAll();
}
    public Observable<List<RetrofitResponse>> getMenCat(){
        return api.getMenCat();
    }
    public Observable<List<RetrofitResponse>> getWomenCat(){
        return api.getWomenCat();
    }
    public Observable<List<RetrofitResponse>> getJew(){
        return api.getJewelery();
    }
    public Observable<List<RetrofitResponse>> getEl(){
        return api.getElectronics();
    }
    public Observable<List<RetrofitResponse>> getSingle(int id){
        return api.getSingleObject(id);
    }

}
