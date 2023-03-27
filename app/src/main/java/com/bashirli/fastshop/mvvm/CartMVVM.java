package com.bashirli.fastshop.mvvm;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.bashirli.fastshop.adapter.CartAdapter;
import com.bashirli.fastshop.model.DatabaseModel;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.roomdb.RoomDAO;
import com.bashirli.fastshop.roomdb.RoomDB;
import com.bashirli.fastshop.service.Service;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CartMVVM extends ViewModel {

    private MutableLiveData<Boolean> loading=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> error=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> success=new MutableLiveData<Boolean>();
    private Boolean isListEmpty;
    private String errorMessage;
    private RoomDB roomDB;
    private RoomDAO roomDAO;
    private Service service=new Service();
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    private ArrayList<RetrofitResponse> myList=new ArrayList<>();
    private ArrayList<RetrofitResponse> cartList=new ArrayList<>();
    private ArrayList<DatabaseModel> dbModelList=new ArrayList<>();
    private Context appContext;
    private CartAdapter oldAdapter;
    public int price;
    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return success;
    }

    public Boolean getListEmpty() {
        return isListEmpty;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private void initAll(){
        roomDB= Room.databaseBuilder(appContext,RoomDB.class,"db").allowMainThreadQueries().build();
        roomDAO=roomDB.getDao();
    }


    public void updateCartList(){
        initAll();
        try{
            compositeDisposable.add(roomDAO.getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateMyList));
        }catch (Exception e){
            Toast.makeText(appContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMyList(List<DatabaseModel> list){
    dbModelList.clear();
    dbModelList.addAll(list);
    }

    public void calculatePrice(TextView textView){
        updateCartList();
        ArrayList<RetrofitResponse> calList=oldAdapter.arrayList;
         price=0;
         for(int i=0;i<calList.size();i++){
            price+=Integer.parseInt(calList.get(i).price)*dbModelList.get(i).number;
         }

        textView.setText("Price : "+price);
    }

    public void deleteFromCart(int id){
        initAll();
        try{
        DatabaseModel deleteModel=null;
        for(DatabaseModel model:dbModelList){
            if(model.itemId==id){
                deleteModel=model;
            }
        }
            compositeDisposable.add(roomDAO.delete(deleteModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::emptyResult));
        }catch (Exception e){
        Toast.makeText(appContext,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }



    private void emptyResult(){
    loadData(appContext,oldAdapter);
    }



    public void loadData(Context context, CartAdapter adapter){
        appContext=context;
        loading.setValue(true);
        error.setValue(false);
        success.setValue(false);
        oldAdapter=adapter;
        errorMessage=null;
        try {
        compositeDisposable.add(service.getAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::result));
        }catch (Exception e){
            loading.setValue(false);
            errorMessage=e.getLocalizedMessage();
            error.setValue(true);
            success.setValue(false);

        }
    }

    private void result(List<RetrofitResponse> list){
        myList.clear();
        myList.addAll(list);
        setCart();
    }



    private void setCart(){
    initAll();
        try {
            compositeDisposable.add(roomDAO.getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultCart));
        }catch (Exception e){
            loading.setValue(false);
            errorMessage=e.getLocalizedMessage();
            error.setValue(true);
            success.setValue(false);

        }

    }



    private void resultCart(List<DatabaseModel> list){
        dbModelList.clear();
        if(list.isEmpty()){
            isListEmpty=true;
            loading.setValue(false);
            error.setValue(false);
            success.setValue(true);
        }else{
            dbModelList.addAll(list);
            isListEmpty=false;
            ArrayList<Integer> numberList=new ArrayList<>();

            for (DatabaseModel model:list){
                for(RetrofitResponse retrofitResponse:myList){
                    if(model.itemId==retrofitResponse.id){
                        cartList.add(retrofitResponse);
                        numberList.add(model.number);
                    }
                }
            }

            oldAdapter.updateList(cartList,numberList);
            loading.setValue(false);
            error.setValue(false);
            success.setValue(true);
        }

    }




}
