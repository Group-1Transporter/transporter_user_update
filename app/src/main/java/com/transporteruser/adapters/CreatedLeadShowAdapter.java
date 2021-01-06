package com.transporteruser.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.databinding.CreatedLoadBinding;
import com.transporteruser.databinding.CurrentAndConfirmedListBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatedLeadShowAdapter extends RecyclerView.Adapter<CreatedLeadShowAdapter.CreatedViewHolder> {
    OnRecyclerViewClickListner listner;
    ArrayList<Lead> leadList;
    public CreatedLeadShowAdapter(ArrayList<Lead> leadList){
        this.leadList = leadList;
    }

    @NonNull
    @Override
    public CreatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CurrentAndConfirmedListBinding binding = CurrentAndConfirmedListBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new CreatedViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final CreatedViewHolder holder, final int position) {
        final Lead lead = leadList.get(position);
        holder.binding.llConfirmLoad.setVisibility(View.GONE);
        String[] deliveryAddress = lead.getDeliveryAddress().split(",");
        String delivery=(deliveryAddress[1]);

        String[] pickupAddress = lead.getPickUpAddress().split(",");
        String pickup= (pickupAddress[1]);
        holder.binding.tvCLLocation.setText(pickup+" To "+delivery);
        holder.binding.tvCLMaterialType.setText(lead.getTypeOfMaterial());
        holder.binding.tvLastDate.setText(lead.getDateOfCompletion());
        holder.binding.tvDistance.setText(lead.getKm());

        if (!lead.getBidCount().equalsIgnoreCase("0")) {
            holder.binding.llCounter.setVisibility(View.VISIBLE);
            holder.binding.tvCounter.setText("" + lead.getBidCount());
        }
        else{
            holder.binding.llCounter.setVisibility(View.GONE);
        }
        holder.binding.morevertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(),holder.binding.morevertical);
                final Menu menu= popupMenu.getMenu();
                menu.add("Delete");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(holder.itemView.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                        String title = menuItem.getTitle().toString();
                        if (title.equalsIgnoreCase("Delete")){
                            final UserService.UserApi userApi = UserService.getUserApiInstance();
                            AlertDialog.Builder ab = new AlertDialog.Builder(holder.itemView.getContext());
                            ab.setTitle("DELETE");
                            ab.setMessage("Are You Sure ?");
                            ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    userApi.deleteLeadById(lead.getLeadId()).enqueue(new Callback<ArrayList<Lead>>() {
                                        @Override
                                        public void onResponse(Call<ArrayList<Lead>> call, Response<ArrayList<Lead>> response) {
                                            if(response.code() == 200){
                                                leadList.remove(position);
                                                notifyDataSetChanged();
                                                Toast.makeText(holder.itemView.getContext(), "Lead Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                                Toast.makeText(holder.itemView.getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<Lead>> call, Throwable t) {
                                            Toast.makeText(holder.itemView.getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            ab.setNegativeButton("No",null);
                            ab.show();
                        }
                        return false;

                    }
                });
                popupMenu.show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    public class CreatedViewHolder extends RecyclerView.ViewHolder {
       CurrentAndConfirmedListBinding binding;
       public CreatedViewHolder(CurrentAndConfirmedListBinding binding) {
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
