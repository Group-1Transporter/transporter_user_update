package com.transporteruser.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transporteruser.R;
import com.transporteruser.databinding.ConfirmLoadListBinding;


import java.util.ArrayList;

public class ConfirmLoadAdapter extends RecyclerView.Adapter<ConfirmLoadAdapter.viewHolder> {

    ArrayList<ConfirmdLoadBean> list1;
    Context context;

    public ConfirmLoadAdapter(ArrayList<ConfirmdLoadBean> list1, Context context) {
        this.list1 = list1;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.confirm_load_list,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

       ConfirmdLoadBean model1 = list1.get(position);

        holder.transpoterName.setText(model1.getTranspoterName());
        holder.location.setText(model1.getLocation());
        holder.material.setText(model1.getMaterial());
        holder.pickupAddress.setText(model1.getPickupAddress());
        holder.deleveryAddress.setText(model1.getDeleveryAddress());
        holder.amount.setText(model1.getAmount());
        holder.expireDate.setText(model1.getExpireDate());
        holder.status.setText(model1.getStatus());
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
       ConfirmLoadListBinding binding;
        TextView transpoterName,location,material,pickupAddress,deleveryAddress,amount,expireDate,status;
        public viewHolder( View itemView) {
            super(itemView);
            transpoterName = itemView.findViewById(R.id.transpoterName);
            location = itemView.findViewById(R.id.location);
            material = itemView.findViewById(R.id.materialType);
            pickupAddress = itemView.findViewById(R.id.pickupAddress);
            deleveryAddress = itemView.findViewById(R.id.deleveryAddress);
            amount = itemView.findViewById(R.id.amount);
            expireDate = itemView.findViewById(R.id.expireDate);
            status = itemView.findViewById(R.id.progresStatus);
        }
    }
}

