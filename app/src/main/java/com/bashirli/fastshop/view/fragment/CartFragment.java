package com.bashirli.fastshop.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.adapter.CartAdapter;
import com.bashirli.fastshop.databinding.FragmentCartBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.mvvm.CartMVVM;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private CartMVVM viewModel;
    private CartAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentCartBinding.bind(view);
        viewModel=new ViewModelProvider(requireActivity()).get(CartMVVM.class);
        adapter=new CartAdapter(new ArrayList<RetrofitResponse>(),new ArrayList<Integer>(),viewModel);
        binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewCart.setAdapter(adapter);
        viewModel.loadData(requireContext(),adapter);
        observeData();
        itemTouchHelperFunc();

    }

    private void itemTouchHelperFunc(){
        ItemTouchHelper.Callback itemTouchHelper= new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder alert=new AlertDialog.Builder(requireContext());
                alert.setTitle(R.string.attention);
                alert.setMessage(R.string.deletesurecart).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(requireContext(), R.string.refreshInfo, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteFromCart(adapter.arrayList.get(viewHolder.getAdapterPosition()).id);
                    }
                }).create().show();
            }


        };

        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.recyclerViewCart);

    }


    private void observeData(){
        viewModel.getSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
           if(aBoolean){
               binding.recyclerViewCart.setVisibility(View.VISIBLE);
               binding.progressBar11.setVisibility(View.GONE);
               calculatePrice();
           }
            }
        });
        viewModel.getError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.recyclerViewCart.setVisibility(View.GONE);
                    binding.progressBar11.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), viewModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                binding.recyclerViewCart.setVisibility(View.GONE);
                binding.progressBar11.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void calculatePrice(){
        viewModel.calculatePrice(binding.textView19);
    }




}