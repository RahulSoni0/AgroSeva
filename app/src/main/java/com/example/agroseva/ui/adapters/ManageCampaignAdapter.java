package com.example.agroseva.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agroseva.R;
import com.example.agroseva.data.campaign.CampaignItem;

import java.util.List;

public class ManageCampaignAdapter extends RecyclerView.Adapter<ManageCampaignAdapter.MyViewHolder> {


    List<CampaignItem> dataList;
    Context context;

    public interface ManageCampaignAdapterListener {
        void onItemClicked(CampaignItem c);
    }

    private ManageCampaignAdapterListener mListener;

    public void setListener(ManageCampaignAdapterListener listener) {
        mListener = listener;
    }

    public ManageCampaignAdapter(List<CampaignItem> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_camp, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CampaignItem c = dataList.get(position);

        holder.title.setText(c.getCampaign_title());
        holder.desc.setText("Product : " + c.getCampaign_description());
        holder.prod.setText("Description : " + c.getProduct_description());
        holder.amount.setText("Amount : " + c.getAmount());



        Glide.with(context).load(c.getCampaign_image_url()).into(holder.ivbanner);

        holder.tvmDeleteCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = holder.getBindingAdapterPosition(); // or getAbsoluteAdapterPosition()
                if (p != RecyclerView.NO_POSITION) {
                    // Handle the click event for the TextView
                    mListener.onItemClicked(c);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView ivbanner;
        TextView title, prod, desc, amount, tvmDeleteCamp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.tvmCampaignAmount);
            tvmDeleteCamp = itemView.findViewById(R.id.tvmDeleteCamp);
            ivbanner = itemView.findViewById(R.id.ivmItemImage);
            title = itemView.findViewById(R.id.tvmCampaignTitle);
            prod = itemView.findViewById(R.id.tvmCampaignProd);
            desc = itemView.findViewById(R.id.tvmCampaignDesc);
        }
    }
}
