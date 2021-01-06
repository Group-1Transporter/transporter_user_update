package com.transporteruser;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmedLoad  extends AppCompatActivity {
    com.transporteruser.databinding.CurrentLoadShowBinding binding;
    HomeAdapter confirmAdapter;
    UserService.UserApi userApi;
    String currentUserId ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= com.transporteruser.databinding.CurrentLoadShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle("Confirmed Load");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userApi = UserService.getUserApiInstance();
        userApi.getConfirmLeads(currentUserId).enqueue(new Callback<ArrayList<Lead>>() {
            @Override
            public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                if(response.code() == 200) {
                    ArrayList<Lead> leadList = response.body();
                    if (!leadList.isEmpty()) {
                        confirmAdapter = new HomeAdapter(leadList);
                        binding.rv.setAdapter(confirmAdapter);
                        binding.rv.setLayoutManager(new LinearLayoutManager(ConfirmedLoad.this));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                Toast.makeText(ConfirmedLoad.this, ""+t, Toast.LENGTH_SHORT).show();


            }
        });

    }
}
