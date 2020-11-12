package com.transporteruser.adapters;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.CurrentAndConfirmedListBinding;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    ArrayList<Lead> list;
    public HomeAdapter(ArrayList<Lead> list){
        this.list = list;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CurrentAndConfirmedListBinding binding = CurrentAndConfirmedListBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new HomeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {
        Lead lead = list.get(position);
        Toast.makeText(holder.itemView.getContext(), ""+ lead.getTypeOfMaterial(), Toast.LENGTH_SHORT).show();

        if(lead !=null && lead.getStatus().equals("")) {
            holder.binding.llCurrentLoad.setVisibility(View.VISIBLE);
            holder.binding.llConfirmLoad.setVisibility(View.GONE);
            holder.binding.tvCLLocation.setText(lead.getPickUpAddress());
            holder.binding.tvCLLocationDestination.setText(lead.getDeliveryAddress());
            holder.binding.tvCLMaterialType.setText(lead.getTypeOfMaterial());
            Toast.makeText(holder.itemView.getContext(), ""+ lead.getBidCount(), Toast.LENGTH_SHORT).show();
            if (lead.getBidCount() == null)
                holder.binding.llCounter.setVisibility(View.GONE);
            else{
                holder.binding.tvCounter.setText(""+lead.getBidCount());
                holder.binding.llCounter.setVisibility(View.VISIBLE);
            }
            holder.binding.morevertical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(),holder.binding.morevertical);
                    final Menu menu= popupMenu.getMenu();
                    menu.add("Edit");
                    menu.add("Delete");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String title = menuItem.getTitle().toString();
                            if(title.equals("Edit")){
                                Toast.makeText(holder.itemView.getContext(), "Edit", Toast.LENGTH_SHORT).show();
                            }else if (title.equals("Delete")){
                                Toast.makeText(holder.itemView.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
            holder.binding.tvCLPickUpContact.setText(lead.getContactForPickup());
            holder.binding.tvCLDeliveryContact.setText(lead.getContactForDelivery());
        }else if(lead.getStatus().equalsIgnoreCase("confirmed")){
            holder.binding.llCurrentLoad.setVisibility(View.GONE);
            holder.binding.llConfirmLoad.setVisibility(View.VISIBLE);
            holder.binding.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(),holder.binding.morevertical);
                    final Menu menu= popupMenu.getMenu();
                    menu.add("Chat with Client");
                    menu.add("Cancel");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String title = menuItem.getTitle().toString();
                            if(title.equals("Chat with Client")){
                                Toast.makeText(holder.itemView.getContext(), "Chat with Client", Toast.LENGTH_SHORT).show();
                            }else if (title.equals("Cancel")){
                                Toast.makeText(holder.itemView.getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
            holder.binding.tvTransporterName.setText(""+lead.getTransporterName());
            holder.binding.tvLocation.setText(lead.getPickUpAddress());
            holder.binding.tvLocationDestination.setText(lead.getDeliveryAddress());
            holder.binding.tvMaterialType.setText(lead.getTypeOfMaterial());
            holder.binding.tvPickUpContact.setText(lead.getContactForPickup());
            holder.binding.tvDeliveryContact.setText(lead.getContactForDelivery());

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
        CurrentAndConfirmedListBinding binding;
        public HomeViewHolder(final CurrentAndConfirmedListBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }


}
