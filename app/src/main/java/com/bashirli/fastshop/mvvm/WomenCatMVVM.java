package com.bashirli.fastshop.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.service.Service;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WomenCatMVVM extends ViewModel {
    public MutableLiveData<Boolean> loading=new MutableLiveData<>();
    public MutableLiveData<Boolean> error=new MutableLiveData<>();
    public MutableLiveData<List<RetrofitResponse>> myData=new MutableLiveData<>();
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    private Service service=new Service();


    public void refreshData(){
        getData();
    }


    public void getData(){
        loading.setValue(true);
        error.setValue(false);
        myData.setValue(null);
        compositeDisposable.add(service.getWomenCat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::result));
    }


    private void result(List<RetrofitResponse> list){
        loading.setValue(false);
        if(list.isEmpty()){
            myData.setValue(null);
            error.setValue(true);
        }else{
            myData.setValue(list);
            error.setValue(false);
        }

    }



}
