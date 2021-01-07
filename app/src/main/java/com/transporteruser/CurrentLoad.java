package com.transporteruser;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.CurrentLoadShowBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentLoad extends AppCompatActivity {
    CurrentLoadShowBinding binding;
    HomeAdapter createAdapter;
    UserService.UserApi userApi;
    String currentUserId ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= CurrentLoadShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle("Current Load");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserService.UserApi userApi = UserService.getUserApiInstance();

         userApi.getAllCreatedLeadsByUserId(currentUserId).enqueue(new Callback<ArrayList<Lead>>() {
                @Override
                public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                    if(response.code() == 200){
                        ArrayList<Lead> leadList = response.body();
                        if (!leadList.isEmpty()){
                            createAdapter = new HomeAdapter(leadList);
                            binding.rv.setAdapter(createAdapter);
                            binding.rv.setLayoutManager(new LinearLayoutManager(CurrentLoad.this));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {

                    Toast.makeText(CurrentLoad.this, ""+t, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

