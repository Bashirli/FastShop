package com.bashirli.fastshop.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.service.Service;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AllCatMVVM extends ViewModel {
    public MutableLiveData<Boolean> loading=new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> error=new MutableLiveData<Boolean>();
    public MutableLiveData<List<RetrofitResponse>> myData=new MutableLiveData<List<RetrofitResponse>>();
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    private Service service=new Service();


    public void getData() {

        loading.setValue(true);
        try {
            compositeDisposable.add(service.getAllData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::result));
        } catch (Exception e) {
            error.setValue(true);
            loading.setValue(false);
        }
    }

    public void refreshData(){
        getData();
    }

    private void result(List<RetrofitResponse> it){

        myData.setValue(it);
        loading.setValue(false);
        error.setValue(false);
    }

}
