package com.example.agroseva.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.agroseva.data.campaign.CampaignItem;
import com.example.agroseva.databinding.ActivityCampaignDetailsBinding;

public class CampaignDetailsActivity extends AppCompatActivity {

    ActivityCampaignDetailsBinding binding;
    CampaignItem c;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCampaignDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            c = (CampaignItem) getIntent().getSerializableExtra("data");
        }


        if (c != null) {
            binding.campaignTitle.setText(c.getCampaign_title());
            binding.campaignDesc.setText("Description : " + c.getCampaign_description());
            binding.campaignProd.setText("Product Description : " + c.getProduct_description());
            binding.campaignAmount.setText("Fund Needed : ₹ " + c.getAmount());
            binding.name.setText("Name : " + c.getContact().getName());
            binding.age.setText("Age : " + c.getContact().getName());
            String address = c.getContact().getAddress().getVill() + " " + c.getContact().getAddress().getCity() + " " + c.getContact().getAddress().getDistrict() + " " + c.getContact().getAddress().getState() + " " + c.getContact().getAddress().getPincode();


            binding.address.setText("Address : " + address);
            Glide.with(this).load(c.getCampaign_image_url()).into(binding.ivProfilePreview);
            Glide.with(this).load(c.getPaymentOptions().get(0).getUpi_qr_url()).into(binding.ivQrcode);
            Glide.with(this).load(c.getAdhar_url()).into(binding.ivAdharPreview);

            binding.Holdername.setText("Holder Name : " + c.getPaymentOptions().get(0).getName());
            binding.BankName.setText("Bank Name : " + c.getPaymentOptions().get(0).getBank_name());
            binding.accountNo.setText("Account No : " + c.getPaymentOptions().get(0).getAccount_no());
            binding.ifsc.setText("IFSC : " + c.getPaymentOptions().get(0).getIfsc());
            binding.upiId.setText("Upi Id : " + c.getPaymentOptions().get(0).getUpi());


        }
        binding.btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CampaignDetailsActivity.this, DonationActivity.class);
                i.putExtra("data", c);
                startActivity(i);

            }
        });


    }

}
