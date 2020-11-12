package com.transporteruser.fragement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.HomeFragementBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragement extends Fragment {
    HomeAdapter adapter;
    String currentUserId;
    Context context;
    HomeFragementBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUserId = FirebaseAuth.getInstance().getUid();
        context = container.getContext();
        UserService.UserApi userApi = UserService.getUserApiInstance();
        binding = HomeFragementBinding.inflate(getLayoutInflater());

        Call<ArrayList<Lead>> call = userApi.getAllLeads(currentUserId);
        call.enqueue(new Callback<ArrayList<Lead>>() {
            @Override
            public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                ArrayList<Lead> list = response.body();
                Toast.makeText(context, "" + list.size(), Toast.LENGTH_SHORT).show();
                adapter = new HomeAdapter(list);
                binding.rv.setAdapter(adapter);
                binding.rv.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {

            }
        });
        return binding.getRoot();

    }
}
