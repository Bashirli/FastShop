package com.bashirli.fastshop.mvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bashirli.fastshop.adapter.FavoritesAdapter;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.service.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FavoritesMVVM extends ViewModel {
    private MutableLiveData<Boolean> loadingDataDB=new MutableLiveData<>();
    private MutableLiveData<Boolean> errorDataDB=new MutableLiveData<>();
    private MutableLiveData<Boolean> successDB=new MutableLiveData<>();
    private MutableLiveData<Boolean> deleteLoading=new MutableLiveData<>();
    private MutableLiveData<Boolean> errorDelete=new MutableLiveData<>();
    private MutableLiveData<Boolean> successDelete=new MutableLiveData<>();
    private String errorMessage=null;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private Service service=new Service();
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    private FavoritesAdapter oldAdapter;
    private ArrayList<RetrofitResponse> oldArrayList=new ArrayList<>();
    private ArrayList<RetrofitResponse> newArrayList=new ArrayList<>();

    public MutableLiveData<Boolean> getDeleteLoading() {
        return deleteLoading;
    }

    public MutableLiveData<Boolean> getErrorDelete() {
        return errorDelete;
    }

    public MutableLiveData<Boolean> getSuccessDelete() {
        return successDelete;
    }

    public MutableLiveData<Boolean> getLoadingDataDB() {
        return loadingDataDB;
    }

    public MutableLiveData<Boolean> getErrorDataDB() {
        return errorDataDB;
    }

    public MutableLiveData<Boolean> getSuccessDB() {
        return successDB;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private void initAll(){
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
    }


    public void createList(FavoritesAdapter adapter){
        oldAdapter=adapter;
        newArrayList.clear();
        loadingDataDB.setValue(true);
        successDB.setValue(false);
        errorDataDB.setValue(false);
        errorMessage=null;
        try{
            compositeDisposable.add(service.getAllData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::newResult));
        }catch (Exception e){
            loadingDataDB.setValue(false);
            errorMessage=e.getLocalizedMessage();
            errorDataDB.setValue(true);
            successDB.setValue(false);
            compositeDisposable.clear();
        }
    }
    private void newResult(List<RetrofitResponse> list){
        oldArrayList.clear();
        oldArrayList.addAll(list);
       loadDataFromDB();
        loadingDataDB.setValue(false);
        successDB.setValue(true);
        errorDataDB.setValue(false);
    }

    private void loadDataFromDB(){
        initAll();

        firestore.collection("Favorites").document(auth.getCurrentUser().getEmail())
                .collection("favorites")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            errorMessage=error.getLocalizedMessage();
                            errorDataDB.setValue(true);
                            loadingDataDB.setValue(false);
                            successDB.setValue(false);
                        return;
                        }
                        if(value!=null){
                            for(DocumentSnapshot snapshot : value.getDocuments()){
                                int i=Integer.parseInt(snapshot.getId());
                                updateAdapter(i);
                                // loadData(i);
                            }

                        }


                    }
                });
    }


    private void updateAdapter(int id){
        for(RetrofitResponse retrofitResponse:oldArrayList){
            if(retrofitResponse.id==id){
                newArrayList.add(retrofitResponse);
                oldAdapter.updateList(newArrayList);
                return;
            }
        }
    }

    public void deleteData(int position){
        int savedId=oldAdapter.arrayList.get(position).id;

        deleteLoading.setValue(true);
        errorDelete.setValue(false);
        errorMessage=null;
        successDelete.setValue(false);

        firestore.collection("Favorites")
                .document(auth.getCurrentUser().getEmail())
                .collection("favorites")
                .document(String.valueOf(savedId))
                .delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        deleteLoading.setValue(false);
                        errorMessage=e.getLocalizedMessage();
                        errorDelete.setValue(true);
                        successDelete.setValue(false);
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        deleteLoading.setValue(false);
                        errorDelete.setValue(false);
                        successDelete.setValue(true);
                        oldAdapter.arrayList.clear();
                        oldAdapter.notifyDataSetChanged();
                        refreshDatabase(oldAdapter);
                    }
                });


    }





    private void stopDatabase(){
        firestore.terminate();
    }
    public void refreshDatabase(FavoritesAdapter adapter){
        stopDatabase();
        createList(adapter);
    }


/*
    private void loadData(int id){
        try{
            compositeDisposable.add(service.getSingle(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::result));
        }catch (Exception e){
            loadingDataDB.setValue(false);
            errorMessage=e.getLocalizedMessage();
            System.out.println(e.getLocalizedMessage());
            errorDataDB.setValue(true);
            successDB.setValue(false);
            compositeDisposable.clear();
        }



    }

    private void result(List<RetrofitResponse> list){
    if(list.isEmpty()){
        loadingDataDB.setValue(false);
        errorMessage="Favorites is empty";
        errorDataDB.setValue(true);
        successDB.setValue(false);
         return;
    }
        loadingDataDB.setValue(false);
        errorDataDB.setValue(false);
        successDB.setValue(true);
        oldAdapter.updateList(list);

    }



 */




}
