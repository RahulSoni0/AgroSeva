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
import com.example.agroseva.data.market.Product;
import com.example.agroseva.ui.activities.ProductDetailsActivity;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MyViewHolder> {
    List<Product> data;

    Context context;

    public MarketAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market, parent, false);
        return new MarketAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Product p = data.get(position);

        holder.title.setText("Product : " + p.getProduct_name());
        holder.desc.setText("Description : " + p.getProduct_desc());
        holder.amount.setText(" Rate  ₹  :" + p.getProduct_price());

        String add = p.getContact().getAddress().getVill() + " " + p.getContact().getAddress().getCity() + " " + p.getContact().getAddress().getDistrict() + " " + p.getContact().getAddress().getState() + " - " + p.getContact().getAddress().getPincode() + " ";
        holder.address.setText("Address : " + add);
        holder.author.setText("by : " + p.getContact().getName());
        Glide.with(context).load(p.getProduct_Imageurl()).into(holder.ivbanner);

        holder.ivbanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //move to new activity... and show the details....
                Intent i = new Intent(context, ProductDetailsActivity.class);
                i.putExtra("data", p);
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivbanner;
        TextView title, desc, author, amount, address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivbanner = itemView.findViewById(R.id.mivItemImage);
            title = itemView.findViewById(R.id.mtvCampaignTitle);
            desc = itemView.findViewById(R.id.mtvCampaignDesc);
            author = itemView.findViewById(R.id.mtvCampaignAuthor);
            amount = itemView.findViewById(R.id.mtvCampaignAmount);
            address = itemView.findViewById(R.id.mtvCampaignLocation);
        }
    }


}
