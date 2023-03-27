package com.bashirli.fastshop.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.databinding.FragmentEditProfileBinding;
import com.bashirli.fastshop.model.UserData;
import com.bashirli.fastshop.mvvm.EditProfileMVVM;
import com.bashirli.fastshop.view.activity.util.Util;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class EditProfileFragment extends Fragment {
    private FragmentEditProfileBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private Uri selectedImageUri=null;
    private Util util=new Util();
    private String localPP=null;
    private EditProfileMVVM viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentEditProfileBinding.bind(view);
        binding.editTextTextPersonName3.setInputType(InputType.TYPE_NULL);
        permissionSetup();
        viewModel=new ViewModelProvider(requireActivity()).get(EditProfileMVVM.class);
        loadUserData();
        onClicks();

    }

    private void loadUserData(){
    viewModel.loadData();
    viewModel.getSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {
            if(aBoolean){
                binding.progressBar6.setVisibility(View.INVISIBLE);
                binding.constraintData.setVisibility(View.VISIBLE);
                binding.cardView.setVisibility(View.VISIBLE);
                setupUserData(viewModel.getUserData());
            }
        }
    });

        viewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                binding.progressBar6.setVisibility(View.VISIBLE);
                binding.constraintData.setVisibility(View.INVISIBLE);
                binding.cardView.setVisibility(View.INVISIBLE);
                }
            }
        });

        viewModel.getErrorData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.progressBar6.setVisibility(View.INVISIBLE);
                    binding.constraintData.setVisibility(View.INVISIBLE);
                    binding.cardView.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(), viewModel.getErrorMessage()+" Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupUserData(UserData userData){
        String email=userData.getEmail();
        String nickname=userData.getNickname();
        String pp=userData.getImageURL();
        localPP=userData.getImageURL();
        String number=userData.getNumber();

        if(localPP.equals("null")){
            localPP=null;
        }

        binding.editTextTextPersonName3.setText(email);

        if(pp.equals("null")){
            binding.imageView7.setImageResource(R.drawable.circlelogo);

        }else{

            util.setImageURL(binding.imageView7,requireContext(),pp,util.placeHolder(requireContext()));
        }


        if(nickname.equals("null")){
            binding.editTextTextPersonName4.setText("");
        }else{
            binding.editTextTextPersonName4.setText(nickname);
        }



        if(number.equals("null")){
            binding.editTextNumber.setText("");
        }else{
            binding.editTextNumber.setText(number);
        }


    }


    private void onClicks(){
        binding.goBackEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(requireContext());
                alert.setTitle(R.string.attention);
                alert.setMessage(R.string.goBackAttention);
                alert.setCancelable(false);
                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavDirections navDirections=EditProfileFragmentDirections.actionEditProfileFragmentToMyProfileFragment();
                        Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(navDirections);
                    }
                }).create().show();
            }
        });
        binding.textView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(view);
            }
        });

        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(requireContext());
                alert.setTitle(R.string.attention).setMessage(R.string.choosewhatdo)
                        .setCancelable(true).setNegativeButton(R.string.deleteImage, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedImageUri=null;
                                localPP=null;
                                binding.imageView7.setImageResource(R.drawable.circlelogo);
                            }
                        }).setPositiveButton(R.string.selectImage, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectImage(view);
                            }
                        }).create().show();
            }
        });

    }

    private void selectImage(View view){
    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            Snackbar.make(view,R.string.perDenied,Snackbar.LENGTH_INDEFINITE).setAction(R.string.givePer, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }).show();
        }else{
            //permission
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }else{
        //action
        activityResultLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));

    }
    }

    private void permissionSetup(){
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
           if(result.getResultCode()== Activity.RESULT_OK){
               Intent intent=result.getData();
               if(intent!=null){
                   selectedImageUri=intent.getData();
               binding.imageView7.setImageURI(selectedImageUri);
               }
           }
            }
        });


        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
           if(result){
               activityResultLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
           }else{
               Toast.makeText(requireContext(), R.string.perDeniedMsg, Toast.LENGTH_SHORT).show();
           }
            }
        });
    }

    private boolean error(){
        if(binding.editTextNumber.getText().toString().isEmpty()||binding.editTextTextPersonName4.getText().toString().isEmpty()){
            Toast.makeText(requireContext(), R.string.errorEmpty, Toast.LENGTH_SHORT).show();
        return true;
        }
        if(binding.editTextTextPersonName4.getText().toString().length()>35){
            Toast.makeText(requireContext(), R.string.nickprob, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void update(View view){
    if(error()){
        return;
    }

    AlertDialog.Builder alert=new AlertDialog.Builder(requireContext());
    alert.setTitle(R.string.attention).setMessage(R.string.updateAttention).setCancelable(false)
            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    updateUserData(view);
                }
            }).create().show();


    }

    private void updateUserData(View view){

        HashMap<String,Object> userData=new HashMap<>();
        userData.put("nickname",binding.editTextTextPersonName4.getText().toString());
        userData.put("number",binding.editTextNumber.getText().toString());
        userData.put("email",binding.editTextTextPersonName3.getText().toString());
        if(selectedImageUri!=null) {
            userData.put("profilePicture", selectedImageUri);
        }else if(selectedImageUri==null&&localPP==null){
            userData.put("profilePicture", "null");
        }else if(selectedImageUri==null&&localPP!=null){
            userData.put("profilePicture", localPP);
        }
    viewModel.updateData(userData,localPP);

        ProgressDialog progressDialog=new ProgressDialog(requireContext());
        progressDialog.setTitle(R.string.attention);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        viewModel.getSuccess2().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                   progressDialog.cancel();
                    Toast.makeText(requireContext(), R.string.sucUpdate, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getLoading2().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                  progressDialog.show();
                }
            }
        });

        viewModel.getErrorData2().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                 progressDialog.cancel();
                    Toast.makeText(requireContext(), viewModel.getErrorMessage2()+" Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}