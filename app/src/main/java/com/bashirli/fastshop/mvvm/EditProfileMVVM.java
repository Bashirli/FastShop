package com.bashirli.fastshop.mvvm;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bashirli.fastshop.model.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class EditProfileMVVM extends ViewModel {
    private MutableLiveData<Boolean> loading=new MutableLiveData<>();
    private MutableLiveData<Boolean> errorData=new MutableLiveData<>();
    private MutableLiveData<Boolean> success=new MutableLiveData<>();
    private String errorMessage;
    private MutableLiveData<Boolean> loading2=new MutableLiveData<>();
    private MutableLiveData<Boolean> errorData2=new MutableLiveData<>();
    private MutableLiveData<Boolean> success2=new MutableLiveData<>();
    private String errorMessage2;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private UserData userData;

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



    public UserData getUserData() {
        return userData;
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

    private void initAll(){
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
    }

    public void loadData(){
        userData=null;
        initAll();
        loading.setValue(true);
        errorData.setValue(false);
        success.setValue(false);
        errorMessage=null;
        firestore.collection("UserData").document(auth.getCurrentUser().getEmail())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                 if(error!=null){
                     errorMessage=error.getLocalizedMessage();
                     errorData.setValue(true);
                     loading.setValue(false);
                     return;
                 }
                 if(value!=null){
                     String email=auth.getCurrentUser().getEmail();
                     String nickname=(String) value.get("nickname");
                     String number=(String) value.get("number");
                     String profilePicture=(String) value.get("profilePicture");
                     userData=new UserData(nickname,email,number,profilePicture);
                    loading.setValue(false);
                    errorData.setValue(false);
                    errorMessage=null;
                    success.setValue(true);
                    return;
                 }
            }
        });
    }

    public void updateData(HashMap<String,Object> data,@Nullable String localPP){
        initAll();
        loading2.setValue(true);
        errorData2.setValue(false);
        success2.setValue(false);
        errorMessage2=null;

        if(!data.get("profilePicture").toString().equals("null")&&localPP==null){
            UUID uuid=UUID.randomUUID();
            String fileName="pp/"+uuid+".jpg";

            storageReference.child(fileName).putFile((Uri) data.get("profilePicture"))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child(fileName).getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loading2.setValue(false);
                                success2.setValue(false);
                                errorMessage2=e.getLocalizedMessage();
                                errorData2.setValue(true);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                data.remove("profilePicture");
                                data.put("profilePicture",uri.toString());

                                firestore.collection("UserData").document(auth.getCurrentUser().getEmail())
                                        .update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                loading2.setValue(false);
                                                success2.setValue(true);
                                                errorData2.setValue(false);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loading2.setValue(false);
                                                success2.setValue(false);
                                                errorMessage2=e.getLocalizedMessage();
                                                errorData2.setValue(true);
                                            }
                                        });

                            }
                        });




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading2.setValue(false);
                            success2.setValue(false);
                            errorMessage2=e.getLocalizedMessage();
                            errorData2.setValue(true);
                        }
                    });



        }
        else{
            firestore.collection("UserData").document(auth.getCurrentUser().getEmail())
                    .update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            loading2.setValue(false);
                            success2.setValue(true);
                            errorData2.setValue(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading2.setValue(false);
                            success2.setValue(false);
                            errorMessage2=e.getLocalizedMessage();
                            errorData2.setValue(true);
                        }
                    });
        }


    }


}
