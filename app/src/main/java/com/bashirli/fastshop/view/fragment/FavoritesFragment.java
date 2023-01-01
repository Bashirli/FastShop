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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bashirli.fastshop.R;
import com.bashirli.fastshop.adapter.FavoritesAdapter;
import com.bashirli.fastshop.databinding.FragmentFavoritesBinding;
import com.bashirli.fastshop.model.RetrofitResponse;
import com.bashirli.fastshop.mvvm.FavoritesMVVM;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding  binding;
    private FavoritesMVVM viewModel;
    private FavoritesAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=FragmentFavoritesBinding.bind(view);

        viewModel=new ViewModelProvider(this).get(FavoritesMVVM.class);
        adapter=new FavoritesAdapter(new ArrayList<RetrofitResponse>());
        binding.recyclerFavoritesSection.setAdapter(adapter);
        binding.recyclerFavoritesSection.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadData();
        swipecallback();

        binding.swiperefreshfavo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshDatabase(adapter);
                binding.swiperefreshfavo.setRefreshing(false);
            }
        });

    }
    private void swipecallback(){
        ItemTouchHelper.Callback i= new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder alert=new AlertDialog.Builder(requireContext());
                alert.setTitle(R.string.attention);
                alert.setMessage(R.string.deletesure).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(requireContext(), R.string.refreshInfo, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                   deleteItem(viewHolder.getAdapterPosition());
                    }
                }).create().show();
            }

        };
        new ItemTouchHelper(i).attachToRecyclerView(binding.recyclerFavoritesSection);

    }

    private void deleteItem(int position){
        viewModel.deleteData(position);
        viewModel.getErrorDelete().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.recyclerFavoritesSection.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), viewModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getDeleteLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.recyclerFavoritesSection.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), R.string.deleting, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getSuccessDelete().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.recyclerFavoritesSection.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), R.string.sucDel, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void loadData(){
        viewModel.createList(adapter);
        viewModel.getLoadingDataDB().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.progressBar10.setVisibility(View.VISIBLE);
                    binding.recyclerFavoritesSection.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getErrorDataDB().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.progressBar10.setVisibility(View.GONE);
                    binding.recyclerFavoritesSection.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), viewModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getSuccessDB().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.progressBar10.setVisibility(View.GONE);
                    binding.recyclerFavoritesSection.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}