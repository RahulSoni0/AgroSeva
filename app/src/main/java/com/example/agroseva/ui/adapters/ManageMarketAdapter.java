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
import com.example.agroseva.data.market.Product;

import java.util.List;

public class ManageMarketAdapter extends RecyclerView.Adapter<ManageMarketAdapter.MyViewHolder> {
    List<Product> data;

    Context context;

    public interface ManageMarketAdapterListener {
        void onItemClicked(Product p);
    }

    private ManageMarketAdapter.ManageMarketAdapterListener mListener;

    public void setListener(ManageMarketAdapter.ManageMarketAdapterListener listener) {
        mListener = listener;
    }

    public ManageMarketAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_market, parent, false);
        return new ManageMarketAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Product p = data.get(position);

        holder.title.setText("Product : " + p.getProduct_name());
        holder.desc.setText("Description : " + p.getProduct_desc());
        holder.amount.setText(" Rate  ₹  :" + p.getProduct_price());

        String add = p.getContact().getAddress().getVill() + " " + p.getContact().getAddress().getCity() + " " + p.getContact().getAddress().getDistrict() + " " + p.getContact().getAddress().getState() + " - " + p.getContact().getAddress().getPincode() + " ";
        Glide.with(context).load(p.getProduct_Imageurl()).into(holder.ivbanner);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getBindingAdapterPosition(); // or getAbsoluteAdapterPosition()
                if (pos != RecyclerView.NO_POSITION) {
                    // Handle the click event for the TextView
                    mListener.onItemClicked(p);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivbanner;
        TextView title, desc, amount, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivbanner = itemView.findViewById(R.id.ManageProductivItemImage);
            title = itemView.findViewById(R.id.ManageProductTitle);
            desc = itemView.findViewById(R.id.ManageProductDesc);
            amount = itemView.findViewById(R.id.ManageProductAmount);
            delete = itemView.findViewById(R.id.ManageProductDeleteProduct);
        }
    }


}
