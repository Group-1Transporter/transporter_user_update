package com.transporteruser.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.transporteruser.BidShowActivity;
import com.transporteruser.bean.Bid;
import com.transporteruser.databinding.BidShowBinding;

import java.util.ArrayList;

public class BidShowAdapter extends RecyclerView.Adapter<BidShowAdapter.BidsViewHolder> {

    ArrayList<Bid> bidList;

    public BidShowAdapter(ArrayList<Bid> bidList) {
        this.bidList = bidList;
    }

    @NonNull
    @Override
    public BidsViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BidShowBinding binding = BidShowBinding.inflate(LayoutInflater.from(parent.getContext()));
        BidsViewHolder bidsViewHolder = new BidsViewHolder(binding);
        return bidsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BidsViewHolder holder, int position) {

        Bid bid =bidList.get(position);

        holder.binding.tvTransporterName.setText(bid.getTransporterName());
        holder.binding.tvRate.setText(bid.getAmount());
        holder.binding.tvDate.setText(bid.getEstimatedDate());
        //holder.binding.tvCLMaterialType.setText(bid.get);
        //holder.binding.tvQuntity.setText(bid.getquntity);
        holder.binding.tvRemark.setText(bid.getRemark());
    }

    @Override
    public int getItemCount()
    {
        return bidList.size();
    }


    public class BidsViewHolder extends RecyclerView.ViewHolder {
        BidShowBinding binding;
        public BidsViewHolder(BidShowBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
