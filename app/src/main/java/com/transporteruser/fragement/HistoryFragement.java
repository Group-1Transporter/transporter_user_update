package com.transporteruser.fragement;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.R;
import com.transporteruser.adapters.CompletedLoadShowAdapter;
import com.transporteruser.adapters.CreatedLeadShowAdapter;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.HistoryFragementBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
HistoryFragement extends Fragment {
    UserService.UserApi userApi;
    HistoryFragementBinding binding;
    CompletedLoadShowAdapter adapter;
    String currentUserId ;
    SharedPreferences sp = null;
    ProgressDialog pd;
    CreatedLeadShowAdapter createdLeadShowAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = HistoryFragementBinding.inflate(getLayoutInflater());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userApi = UserService.getUserApiInstance();
        getCompletedLeads();
        binding.tb.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String title=tab.getText().toString();
                if (title.equals("Completed Load")){
                    getCompletedLeads();
                }
                else if (title.equals("Created Load")){
                    getCreateLeads();

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return binding.getRoot();
    }

    private void getCreateLeads(){
        pd = new ProgressDialog(getContext());
        pd.setMessage("please wait...");
        pd.show();
        Call<ArrayList<Lead>> call = userApi.getAllCreatedLeadsByUserId(currentUserId);
        call.enqueue(new Callback<ArrayList<Lead>>() {
            @Override
            public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                pd.dismiss();
                if(response.code() == 200){
                    ArrayList<Lead> leadList = response.body();
                    if (leadList.size()!= 0){
                        binding.rv.setVisibility(View.VISIBLE);
                        binding.noData.setVisibility(View.GONE);
                        createdLeadShowAdapter = new CreatedLeadShowAdapter(leadList);
                        binding.rv.setAdapter(createdLeadShowAdapter);
                        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    }else {
                        binding.rv.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getCompletedLeads(){
        pd = new ProgressDialog(getContext());
        pd.setMessage("please wait...");
        pd.show();
        Call<ArrayList<Lead>> call = userApi.getAllCompletedLeadsByUserId(currentUserId);
        call.enqueue(new Callback<ArrayList<Lead>>() {
            @Override
            public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                pd.dismiss();
                if(response.code() == 200){
                    ArrayList<Lead> leadList = response.body();
                    if(leadList.size()!= 0){
                        binding.rv.setVisibility(View.VISIBLE);
                        binding.noData.setVisibility(View.GONE);
                        adapter = new CompletedLoadShowAdapter(leadList);
                        binding.rv.setAdapter(adapter);
                        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    }else{
                        binding.rv.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
