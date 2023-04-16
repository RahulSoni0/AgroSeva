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
import com.example.agroseva.data.home.ModelHomeItem;
import com.example.agroseva.ui.activities.BrowserActivity;

import java.util.List;

public class HomeRvAdapter extends RecyclerView.Adapter<HomeRvAdapter.MyViewHolder> {


    List<ModelHomeItem> data;
    Context context;

    public HomeRvAdapter(List<ModelHomeItem> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new HomeRvAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {




        ModelHomeItem m = data.get(position);
        holder.title.setText(m.getTitle());
        holder.desc.setText(m.getDesc());
        Glide.with(context).load(m.getImage_url()).into(holder.banner);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, BrowserActivity.class);
                i.putExtra("data", m);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView banner;
        TextView title, desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            banner = itemView.findViewById(R.id.ivItemHomeImage);
            title = itemView.findViewById(R.id.tvItemHomeTitle);
            desc = itemView.findViewById(R.id.tvItemHomeDescriptionHere);


        }
    }
}
