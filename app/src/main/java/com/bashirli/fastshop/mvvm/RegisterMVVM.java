package com.bashirli.fastshop.mvvm;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterMVVM extends ViewModel {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    public MutableLiveData<Boolean> loading=new MutableLiveData<>();
    public MutableLiveData<Boolean> error=new MutableLiveData<>();
    public MutableLiveData<Boolean> success=new MutableLiveData<>();
    public MutableLiveData<String> errorMessage=new MutableLiveData<>();


    private void initialize(){
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

    }

    public void register(String email,String password,HashMap<String,Object> userData){
        initialize();
       loading.setValue(true);
       errorMessage.setValue(null);
       error.setValue(false);
       success.setValue(false);

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                firestore.collection("UserData").document(email).set(userData)
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading.setValue(false);
                        error.setValue(true);
                        success.setValue(false);
                        errorMessage.setValue(e.getLocalizedMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loading.setValue(false);
                        error.setValue(false);
                        success.setValue(true);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setValue(false);
                error.setValue(true);
                success.setValue(false);
                errorMessage.setValue(e.getLocalizedMessage());
            }
        });
    }



}
