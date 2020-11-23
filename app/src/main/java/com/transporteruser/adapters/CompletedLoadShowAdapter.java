package com.transporteruser.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transporteruser.BidShowActivity;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.CompletedLoadBinding;

import java.util.ArrayList;

public class CompletedLoadShowAdapter extends RecyclerView.Adapter<CompletedLoadShowAdapter.CompletedViewHolder> {
    ArrayList<Lead> leadList;
    public CompletedLoadShowAdapter(ArrayList<Lead> leadList){
        this.leadList = leadList;
    }

    @NonNull
    @Override
    public CompletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CompletedLoadBinding binding = CompletedLoadBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new CompletedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompletedViewHolder holder, int position) {
        final Lead lead = leadList.get(position);
        Toast.makeText(holder.itemView.getContext(), ""+lead.getDeliveryAddress(), Toast.LENGTH_SHORT).show();
        holder.binding.tvDate.setText("Date :- "+lead.getDateOfCompletion());
        holder.binding.tvMaterial.setText(lead.getTypeOfMaterial());
        holder.binding.tvQuntity.setText(lead.getWeight());
        String str[]=lead.getPickUpAddress().split(" ");
        String pickup=str[str.length-1];
        str=lead.getDeliveryAddress().split(" ");
        String delivery =str[str.length-1];
        holder.binding.tvLocationStart.setText(pickup);
        holder.binding.tvLocationEnd.setText(delivery);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent in = new Intent(holder.itemView.getContext(), BidShowActivity.class);
//                in.putExtra("lead",lead);
//                view.getContext().startActivity(in);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }


    public class CompletedViewHolder extends RecyclerView.ViewHolder {
        CompletedLoadBinding binding;
        public CompletedViewHolder(CompletedLoadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
