package com.transporteruser.fragement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.transporteruser.ChatActivity;
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
    ArrayList<Lead> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUserId = FirebaseAuth.getInstance().getUid();
        context = container.getContext();
        UserService.UserApi userApi = UserService.getUserApiInstance();
        binding = HomeFragementBinding.inflate(getLayoutInflater());
        userApi.getCreateAndConfirmed(currentUserId).enqueue(new Callback<ArrayList<Lead>>() {
            @Override
            public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                if(response.code() == 200 ){
                    list = response.body();
                    adapter = new HomeAdapter(list);
                    binding.rv.setAdapter(adapter);
                    binding.rv.setLayoutManager(new LinearLayoutManager(context));
                    adapter.OnHomeClick(new HomeAdapter.OnHomeRecyclerListner() {
                        @Override
                        public void onClick(Lead lead, int position, String status) {
                            deleteLeadCreated(lead,position,status);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();

    }

    private void deleteLeadCreated(final Lead lead, final int position, String status) {
        if(status.equalsIgnoreCase("delete")){
            final UserService.UserApi userApi = UserService.getUserApiInstance();
            AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
            ab.setTitle("DELETE");
            ab.setMessage("Are You Sure ?");
            ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userApi.deleteLeadById(lead.getLeadId()).enqueue(new Callback<ArrayList<Lead>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                            Toast.makeText(getContext(), ""+response.code(), Toast.LENGTH_SHORT).show();
                            if(response.code() == 200){
                                Toast.makeText(getContext(), "Lead Deleted", Toast.LENGTH_SHORT).show();
                                list.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                            else
                                Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                            Toast.makeText(getContext(), "toast : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            ab.setNegativeButton("No",null);
            ab.show();
        }else if(status.equalsIgnoreCase("Chat With Client")){
            Intent i = new Intent(getContext(), ChatActivity.class);
            i.putExtra("transporterId",lead.getDealLockedWith());
            startActivity(i);
        }
    }
}
