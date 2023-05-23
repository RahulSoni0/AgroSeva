package com.example.agroseva.ui.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.agroseva.ui.activities.CampaignDetailsActivity;

import java.util.List;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.MyViewHolder> {


    List<CampaignItem> dataList;
    Context context;

    public CampaignAdapter(List<CampaignItem> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_campaign, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CampaignItem c = dataList.get(position);

        holder.title.setText(c.getCampaign_title());
        holder.desc.setText("Product : " + c.getCampaign_description());
        holder.prod.setText("Description : " + c.getProduct_description());
        holder.amount.setText(" Fund needed  ₹  :" + c.getAmount());
        String add = c.getContact().getAddress().getVill() + " " +
                c.getContact().getAddress().getCity() + " " +
                c.getContact().getAddress().getDistrict() + " " +
                c.getContact().getAddress().getState() + " - " +
                c.getContact().getAddress().getPincode() + " ";
        holder.address.setText("Address : " + add);
        holder.author.setText("by : " + c.getContact().getName());

        Glide.with(context).load(c.getCampaign_image_url()).into(holder.ivbanner);

        holder.ivbanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //move to new activity... and show the details....
                Intent i = new Intent(context, CampaignDetailsActivity.class);
                i.putExtra("data", c);
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView ivbanner;
        TextView title, prod, desc, author, amount, address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivbanner = itemView.findViewById(R.id.ivItemImage);
            title = itemView.findViewById(R.id.tvCampaignTitle);
            prod = itemView.findViewById(R.id.tvCampaignProd);
            desc = itemView.findViewById(R.id.tvCampaignDesc);
            author = itemView.findViewById(R.id.tvCampaignAuthor);
            amount = itemView.findViewById(R.id.tvCampaignAmount);
            address = itemView.findViewById(R.id.tvCampaignLocation);
        }
    }
}
