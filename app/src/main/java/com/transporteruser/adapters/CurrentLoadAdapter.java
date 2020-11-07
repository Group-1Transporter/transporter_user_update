package com.transporteruser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transporteruser.R;
import com.transporteruser.databinding.CurrentLoadListBinding;

import java.util.ArrayList;

public class CurrentLoadAdapter extends RecyclerView.Adapter<CurrentLoadAdapter.viewHolder> {

ArrayList<CurrentLoadBean> list;
Context context;

    public CurrentLoadAdapter(ArrayList<CurrentLoadBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.current_load_list,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        CurrentLoadBean model = list.get(position);

        holder.Location.setText(model.getLocation());
        holder.materialtype.setText(model.getMaterialtype());
        holder.pickupContact.setText(model.getPickupContact());
        holder.deleveryContact.setText(model.getDeleveryContact());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        CurrentLoadListBinding binding;
        TextView Location,materialtype,pickupContact,deleveryContact;
        public viewHolder( View itemView) {
            super(itemView);
            Location = itemView.findViewById(R.id.Location);
            materialtype = itemView.findViewById(R.id.Materialtype);
            pickupContact = itemView.findViewById(R.id.pickupaddress);
            deleveryContact = itemView.findViewById(R.id.deleveryaddress);
        }
    }
}

