package com.transporteruser.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transporteruser.BidShowActivity;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.CreatedLoadBinding;

import java.util.ArrayList;

public class CreatedLeadShowAdapter extends RecyclerView.Adapter<CreatedLeadShowAdapter.CreatedViewHolder> {
    OnRecyclerViewClickListner listner;
    ArrayList<Lead> leadList;
    public CreatedLeadShowAdapter(ArrayList<Lead> leadList){
        this.leadList = leadList;
    }



    @NonNull
    @Override
    public CreatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CreatedLoadBinding binding = CreatedLoadBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new CreatedViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final CreatedViewHolder holder, int position) {
        Lead lead = leadList.get(position);
        String str[]=lead.getPickUpAddress().split(" ");
        String pickup=str[str.length-1];
        str=lead.getDeliveryAddress().split(" ");
        String delivery =str[str.length-1];
        holder.binding.tvCLLocation.setText(pickup+" To "+delivery);
        holder.binding.tvCLMaterialType.setText(lead.getTypeOfMaterial());
        holder.binding.tvCLPickUpContact.setText(lead.getContactForPickup());
        holder.binding.tvCLDeliveryContact.setText(lead.getContactForDelivery());
        holder.binding.tvQuntity.setText(lead.getWeight());
    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    public class CreatedViewHolder extends RecyclerView.ViewHolder {
       CreatedLoadBinding binding;
       public CreatedViewHolder(CreatedLoadBinding binding) {
           super(binding.getRoot());
           this.binding = binding;
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   int position = getAdapterPosition();
                   Lead lead = leadList.get(position);
                   if(position!=RecyclerView.NO_POSITION && listner!=null){
                       listner.onItemClick(lead,position);
                   }
               }
           });
       }
   }


   public interface OnRecyclerViewClickListner{
        public void onItemClick(Lead lead,int position);
   }

   public void clickOnRecyclerView(OnRecyclerViewClickListner listner){
        this.listner = listner;
   }

}
