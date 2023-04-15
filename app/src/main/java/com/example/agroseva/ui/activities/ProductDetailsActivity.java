package com.example.agroseva.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.agroseva.data.market.Product;
import com.example.agroseva.data.models.Address;
import com.example.agroseva.databinding.ActivityProductDetailsBinding;

public class ProductDetailsActivity extends AppCompatActivity {

    ActivityProductDetailsBinding binding;
    Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (getIntent() != null) {
            p = (Product) getIntent().getSerializableExtra("data");
        }

        binding.campaignTitle.setText(p.getProduct_name());
        binding.campaignDesc.setText("Description : " + p.getProduct_desc());
        binding.campaignAvailability.setText("Availability : " + p.getProduct_availability());
        binding.campaignAmount.setText("Rate ₹ : " + p.getProduct_price());

        Address a = p.getContact().getAddress();
        String address = a.getVill() + " " + a.getCity() + " " + a.getDistrict() + " " + a.getState() + " " + a.getCountry() + "   -  " + a.getPincode();

        binding.address.setText("Address : " + address);
        binding.name.setText("Name : " + p.getContact().getName());
        binding.age.setText("Age : " + p.getContact().getAge());
        binding.phoneNumber.setText("Phone number : " + p.getContact().getPhone_no());
        binding.emailId.setText("Email Id : " + p.getContact().getEmail());
        Glide.with(this).load(p.getProduct_Imageurl()).into(binding.ivProfilePreview);

        binding.btnWhatsappChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msgurl = "https://api.whatsapp.com/send?phone=+91" +
                        p.getWhatsappno().trim() +
                        "&text=Hello , I found your product : " + p.getProduct_name() + " " +
                        " which is for sell/rent at Agroseva , i want to know further details";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(msgurl));
                startActivity(i);


            }
        });


    }
}