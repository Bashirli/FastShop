package com.bashirli.fastshop.mvvm;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.adapter.CommentAdapter;
import com.bashirli.fastshop.model.CommentData;
import com.bashirli.fastshop.model.DatabaseModel;
import com.bashirli.fastshop.roomdb.RoomDAO;
import com.bashirli.fastshop.roomdb.RoomDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewItemMVVM extends ViewModel {
    private MutableLiveData<Boolean> loading=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> errorData=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> success=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> loading2=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> errorData2=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> success2=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> loading3=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> errorData3=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> success3=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> loading4=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> errorData4=new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> success4=new MutableLiveData<Boolean>();
    private RoomDB roomDB;
    private RoomDAO roomDAO;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();


    public MutableLiveData<Boolean> getLoading4() {
        return loading4;
    }

    public MutableLiveData<Boolean> getErrorData4() {
        return errorData4;
    }

    public MutableLiveData<Boolean> getSuccess4() {
        return success4;
    }

    public MutableLiveData<Boolean> getLoading3() {
        return loading3;
    }

    public MutableLiveData<Boolean> getErrorData3() {
        return errorData3;
    }

    public MutableLiveData<Boolean> getSuccess3() {
        return success3;
    }

    public MutableLiveData<Boolean> getLoading2() {
        return loading2;
    }

    public MutableLiveData<Boolean> getErrorData2() {
        return errorData2;
    }

    public MutableLiveData<Boolean> getSuccess2() {
        return success2;
    }

    private MutableLiveData<Boolean> isFavorite=new MutableLiveData<>();
    private String errorMessage=null;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getErrorData() {
        return errorData;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }


    private MutableLiveData<Boolean> loadingCart=new MutableLiveData<>();
    private MutableLiveData<Boolean> errorCart=new MutableLiveData<>();
    private MutableLiveData<Boolean> successCart=new MutableLiveData<>();
    private String cartErrorMsg;

    private Boolean isOnCart;
    private int oldItemId;

    public MutableLiveData<Boolean> getLoadingCart() {
        return loadingCart;
    }

    public MutableLiveData<Boolean> getErrorCart() {
        return errorCart;
    }

    public MutableLiveData<Boolean> getSuccessCart() {
        return successCart;
    }

    public String getCartErrorMsg() {
        return cartErrorMsg;
    }

    public Boolean getOnCart() {
        return isOnCart;
    }


    private Context requiredContext;
    private ArrayList<DatabaseModel> requiredDatabaseModelList=new ArrayList<>();


    private void initAll(){
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
    }

    private void initRoom(){
        roomDB= Room.databaseBuilder(requiredContext,RoomDB.class,"db").allowMainThreadQueries().build();
        roomDAO=roomDB.getDao();
    }




    public void checkIsOnCart(Context context,int itemId){
        oldItemId=itemId;
        requiredContext=context;
        loadingCart.setValue(true);
        errorCart.setValue(false);
        successCart.setValue(false);
        cartErrorMsg=null;
        initRoom();
        try {
            compositeDisposable.add(roomDAO.getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultCart));
        }catch (Exception e){
            loadingCart.setValue(false);
            cartErrorMsg=e.getLocalizedMessage();
            errorCart.setValue(true);
            successCart.setValue(false);

        }
    }

    private void resultCart(List<DatabaseModel> list){
        requiredDatabaseModelList.clear();
        if(list.isEmpty()){
            isOnCart=false;
        }else {
            requiredDatabaseModelList.addAll(list);
            for (DatabaseModel model : list) {
                if (model.itemId == oldItemId) {
                    isOnCart = true;
                    loadingCart.setValue(false);
                    errorCart.setValue(false);
                    successCart.setValue(true);
                    return;
                } else {
                    isOnCart = false;
                }
            }
        }
        loadingCart.setValue(false);
        errorCart.setValue(false);
        successCart.setValue(true);
    }

    public void addToCart(int id){
        initRoom();
        DatabaseModel model=new DatabaseModel(id,1);
        try{
            compositeDisposable.add(roomDAO.insert(model)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::emptyResult));
        }catch (Exception e){

        }
    }
    public void loadLastList(){
        try {
            compositeDisposable.add(roomDAO.getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateList));
        }catch (Exception e){

        }
    }

    private void updateList(List<DatabaseModel> list){
        requiredDatabaseModelList.clear();
        requiredDatabaseModelList.addAll(list);
    }


    public void deleteFromCart(int id){
        initRoom();
        loadLastList();
        try{
            DatabaseModel deleteModel=null;
            for(DatabaseModel model:requiredDatabaseModelList){
                if(model.itemId==id){
                    deleteModel=model;
                }
            }
            compositeDisposable.add(roomDAO.delete(deleteModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::emptyResult));
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void emptyResult(){
        loadLastList();
        Toast.makeText(requiredContext, R.string.infoCart, Toast.LENGTH_SHORT).show();
    };


    public void checkDatabase(int id){
        initAll();
        String myId=String.valueOf(id);
        loading4.setValue(true);
        success4.setValue(false);
        errorData4.setValue(false);
        isFavorite.setValue(null);
        errorMessage=null;
        firestore.collection("Favorites").document(auth.getCurrentUser().getEmail())
                .collection("favorites").document(myId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getData()!=null){
                            isFavorite.setValue(true);
                            loading4.setValue(false);
                            errorData4.setValue(false);
                            success4.setValue(true);
                        }else{
                            isFavorite.setValue(false);
                            loading4.setValue(false);
                            errorData4.setValue(false);
                            success4.setValue(true);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading4.setValue(false);
                        errorMessage=e.getLocalizedMessage();
                        errorData4.setValue(true);
                        success4.setValue(false);
                    }
                });

    }


    public void addOrDeleteFromFavorite(int id){
        initAll();
        loading.setValue(true);
        errorData.setValue(false);
        errorMessage=null;
        success.setValue(false);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("email",auth.getCurrentUser().getEmail());
        hashMap.put("date", FieldValue.serverTimestamp());
        if(isFavorite.getValue()){
            firestore.collection("Favorites").document(auth.getCurrentUser().getEmail())
                    .collection("favorites").document(String.valueOf(id)).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            loading.setValue(false);
                            errorData.setValue(false);
                            success.setValue(true);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.setValue(false);
                            errorMessage=e.getLocalizedMessage();
                            errorData.setValue(true);
                            success.setValue(false);
                        }
                    });

        }else{
            firestore.collection("Favorites").document(auth.getCurrentUser().getEmail())
                    .collection("favorites").document(String.valueOf(id)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            loading.setValue(false);
                            errorData.setValue(false);
                            success.setValue(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.setValue(false);
                            errorMessage=e.getLocalizedMessage();
                            errorData.setValue(true);
                            success.setValue(false);
                        }
                    });



        }
    }


    public void publishComment(int id,String text){
        try {
            initAll();
            HashMap<String,Object> commentData=new HashMap<>();
            commentData.put("email",auth.getCurrentUser().getEmail());
            commentData.put("comment",text);
            commentData.put("date",FieldValue.serverTimestamp());
            loading3.setValue(true);
            errorData3.setValue(false);
            errorMessage=null;
            success3.setValue(false);
            firestore.collection("Comments").document(String.valueOf(id)).collection("comments")
                    .add(commentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            loading3.setValue(false);
                            success3.setValue(true);
                            errorData3.setValue(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading3.setValue(false);
                            errorMessage=e.getLocalizedMessage();
                            errorData3.setValue(false);
                            success3.setValue(false);
                        }
                    });
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }

    }





    public void loadComments(int id, CommentAdapter commentAdapter){
        try {


        initAll();
        loading2.setValue(true);
        errorData2.setValue(false);
        ArrayList<CommentData> arrayList=new ArrayList<>();
        errorMessage=null;
        success2.setValue(false);
        firestore.collection("Comments").document(String.valueOf(id))
                .collection("comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            loading2.setValue(false);
                            errorMessage=error.getLocalizedMessage();
                            errorData2.setValue(true);
                            success2.setValue(false);

                            return;
                        }
                        if(value!=null){

                        for(DocumentSnapshot snapshot:value.getDocuments()){
                            String email=(String) snapshot.get("email");
                            firestore.collection("UserData")
                                    .document(email)
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot docvalue, @Nullable FirebaseFirestoreException error) {
                                       if(error!=null){
                                           loading2.setValue(false);
                                           errorMessage=error.getLocalizedMessage();
                                           errorData2.setValue(true);
                                           success2.setValue(false);

                                           return;
                                       }
                                       if(docvalue!=null){

                                                   String imageURL=(String)docvalue.get("profilePicture");
                                                   Timestamp timestamp=(Timestamp) snapshot.get("date");
                                                   String comment=(String) snapshot.get("comment");
                                                   CommentData commentData=new CommentData(email,comment,timestamp,imageURL);
                                                   do{
                                                       timestamp=(Timestamp) snapshot.get("date");
                                                   }while (timestamp==null);
                                                   arrayList.add(commentData);
                                                   commentAdapter.updateData(arrayList);

                                       }
                                        }
                                    });




                        }
                    //update adapter
                            loading2.setValue(false);
                            success2.setValue(true);
                            errorData2.setValue(false);





                        }


                    }
                });
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }

    }



    public void stopDatabase(){
        firestore.terminate();
    }


}
