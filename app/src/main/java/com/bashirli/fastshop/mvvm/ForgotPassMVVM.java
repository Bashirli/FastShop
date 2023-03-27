package com.bashirli.fastshop.mvvm;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassMVVM extends ViewModel {
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<Boolean> error = new MutableLiveData<>();
    public MutableLiveData<Boolean> success = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage=new MutableLiveData<>();
    private FirebaseAuth auth;


    public void sendResetEmail(String email){
        auth=FirebaseAuth.getInstance();
        loading.setValue(true);
        error.setValue(false);
        success.setValue(false);
        errorMessage.setValue(null);

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                loading.setValue(false);
                error.setValue(false);
                errorMessage.setValue(null);
                success.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setValue(false);
                errorMessage.setValue(e.getLocalizedMessage());
                error.setValue(true);
                success.setValue(false);
            }
        });

    }


}
