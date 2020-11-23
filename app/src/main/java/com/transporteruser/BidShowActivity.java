package com.transporteruser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.transporteruser.adapters.BidShowAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Bid;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.ActivityBidShowBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidShowActivity extends AppCompatActivity {
    ActivityBidShowBinding binding;
    UserService.UserApi userApi;
    BidShowAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userApi = UserService.getUserApiInstance();
        binding = ActivityBidShowBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        Lead lead = (Lead) in.getSerializableExtra("lead");

        getBidsByLead(lead.getLeadId());

        String str[]=lead.getPickUpAddress().split(" ");
        String pickup=str[str.length-1];
        str=lead.getDeliveryAddress().split(" ");
        String delivery =str[str.length-1];
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(pickup+" To "+delivery);
    }

    private void getBidsByLead(String leadId) {
        Call<ArrayList<Bid>> call = userApi.getAllBidsByLeadId(leadId);
        call.enqueue(new Callback<ArrayList<Bid>>() {
            @Override
            public void onResponse(Call<ArrayList<Bid>> call, Response<ArrayList<Bid>> response) {
                if(response.code() == 200) {
                    adapter = new BidShowAdapter(response.body());
                    binding.rv.setAdapter(adapter);
                    binding.rv.setLayoutManager(new LinearLayoutManager(BidShowActivity.this));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Bid>> call, Throwable t) {

            }
        });
    }
}