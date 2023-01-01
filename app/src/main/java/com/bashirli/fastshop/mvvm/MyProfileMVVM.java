package com.bashirli.fastshop.mvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bashirli.fastshop.model.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MyProfileMVVM extends ViewModel {
    private MutableLiveData<Boolean> loading=new MutableLiveData<>();
    private MutableLiveData<Boolean> errorData=new MutableLiveData<>();
    private MutableLiveData<Boolean> success=new MutableLiveData<>();
    private String errorMessage;
    private MutableLiveData<Boolean> loading2=new MutableLiveData<>();
    private MutableLiveData<Boolean> errorData2=new MutableLiveData<>();
    private MutableLiveData<Boolean> success2=new MutableLiveData<>();
    private String errorMessage2;
    private MutableLiveData<UserData> user=new MutableLiveData<>();

    public MutableLiveData<Boolean> getErrorData() {
        return errorData;
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

    public String getErrorMessage2() {
        return errorMessage2;
    }

    public MutableLiveData<UserData> getUser(){
        return user;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return errorData;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;



    private void initAll(){
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
    }

    public void getUserData(){
        initAll();
        errorMessage="";
        user.setValue(null);
        loading.setValue(true);
       errorData.setValue(false);
        success.setValue(false);
    firestore.collection("UserData").document(auth.getCurrentUser().getEmail())
            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
            if(error!=null){
                errorMessage=error.getLocalizedMessage();
            }
            if (value!=null){
                 String nickname=(String)value.get("nickname");
                 String email=auth.getCurrentUser().getEmail();
                 String number=(String)value.get("number");
                 String imageURL=(String)value.get("profilePicture");

                UserData userData=new UserData(nickname,email,number,imageURL);
                 user.setValue(userData);
                 loading.setValue(false);
                 success.setValue(true);
                 errorData.setValue(false);
                 return;
            }
        }
    });


    }

    public void signOutUser(){
        initAll();

    loading2.setValue(true);
    errorData2.setValue(false);
    errorMessage2="";
    success2.setValue(true);

    auth.signOut();
    auth.signInAnonymously().addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            errorMessage2=e.getLocalizedMessage();
            errorData2.setValue(true);
            loading2.setValue(false);
            success2.setValue(false);
        }
    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
        @Override
        public void onSuccess(AuthResult authResult) {
            errorMessage2="";
            errorData2.setValue(false);
            loading2.setValue(false);
            success2.setValue(true);
        }
    });

    }





}
