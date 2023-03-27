package com.bashirli.fastshop.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bashirli.fastshop.adapter.AllCatAdapter;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchMVVM extends ViewModel {
    private MutableLiveData<Boolean> loading=new MutableLiveData<Boolean>();

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private MutableLiveData<Boolean> error=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> success=new MutableLiveData<Boolean>();
    private String errorMessage=null;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    private Service service=new Service();
    private ArrayList<RetrofitResponse> list=new ArrayList<>();
    private ArrayList<RetrofitResponse> searchList=new ArrayList<>();

    public void loadData(){
        try {
            loading.setValue(true);
            error.setValue(false);
            success.setValue(false);
            if(!list.isEmpty()){
                list.clear();
            }
            compositeDisposable.add(service.getAllData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::getData));
        }catch (Exception e){
            errorMessage=e.getLocalizedMessage();
            error.setValue(true);
            success.setValue(false);
            loading.setValue(false);
        }
    }
    private void getData(List<RetrofitResponse> retrofitResponseList){
        list.addAll(retrofitResponseList);
        errorMessage=null;
        error.setValue(false);
        success.setValue(true);
        loading.setValue(false);
    }

    public void search(String string, AllCatAdapter adapter){
        loading.setValue(true);
        error.setValue(false);
        success.setValue(false);
        errorMessage=null;
        if(!searchList.isEmpty()){
            searchList.clear();
        }
        string=string.toLowerCase();

        for(RetrofitResponse retrofitResponse : list){
            String oldString=retrofitResponse.title.toLowerCase();
            String oldCat=retrofitResponse.category.toLowerCase(Locale.ROOT);
            if(oldString.contains(string)||oldString.equals(string)||
                    oldCat.contains(string)||oldCat.equals(string)
            ){
                searchList.add(retrofitResponse);
            }

        }
        if(!searchList.isEmpty()){
            adapter.updateList(searchList);
            errorMessage="";
            error.setValue(false);
            loading.setValue(false);
            success.setValue(true);
        }else{
            errorMessage="Nothing found :(";
            error.setValue(true);
            loading.setValue(false);
            success.setValue(false);
        }




    }




}
