package com.transporteruser.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transporteruser.ChatActivity;
import com.transporteruser.MainActivity;
import com.transporteruser.PrivacyPolicy;
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
        final Lead lead = list.get(position);
        if(lead !=null && lead.getStatus().equals("")) {
            holder.binding.llCurrentLoad.setVisibility(View.VISIBLE);
            holder.binding.llConfirmLoad.setVisibility(View.GONE);
            String str[]=lead.getPickUpAddress().split(" ");
            String pickup=str[str.length-1];
            str=lead.getDeliveryAddress().split(" ");
            String delivery =str[str.length-1];
            holder.binding.tvCLLocation.setText(pickup);
            holder.binding.tvCLLocationDestination.setText(delivery);
            holder.binding.tvCLMaterialType.setText(lead.getTypeOfMaterial());
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
                                Intent i = new Intent(holder.itemView.getContext(), ChatActivity.class);
                                i.putExtra("transporterId",lead.getDealLockedWith());
                                holder.itemView.getContext().startActivity(i);
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
            String str[]=lead.getPickUpAddress().split(" ");
            String pickup=str[str.length-1];
            str=lead.getDeliveryAddress().split(" ");
            String delivery =str[str.length-1];
            holder.binding.tvLocation.setText(pickup);
            holder.binding.tvLocationDestination.setText(delivery);
            holder.binding.tvMaterialType.setText(lead.getTypeOfMaterial());
            holder.binding.tvPickUpContact.setText(lead.getContactForPickup());
            holder.binding.tvDeliveryContact.setText(lead.getContactForDelivery());

        }else{
            holder.binding.main.setVisibility(View.GONE);
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
