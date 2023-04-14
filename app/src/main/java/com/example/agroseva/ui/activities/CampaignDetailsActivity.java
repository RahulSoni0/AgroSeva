package com.example.agroseva.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.agroseva.data.campaign.Campaign;
import com.example.agroseva.databinding.ActivityCampaignDetailsBinding;

public class CampaignDetailsActivity extends AppCompatActivity {

    ActivityCampaignDetailsBinding binding;
    Campaign c;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCampaignDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            c = (Campaign) getIntent().getSerializableExtra("data");
        }

        Toast.makeText(this, c.toString(), Toast.LENGTH_SHORT).show();

        if (c != null) {

            binding.campaignTitle.setText(c.getCampaignsList().get(0).getCampaign_title());
            binding.campaignDesc.setText("Description : " + c.getCampaignsList().get(0).getCampaign_description());
            binding.campaignProd.setText("Product Description : " + c.getCampaignsList().get(0).getProduct_description());
            binding.campaignAmount.setText("Fund Needed : ₹ " + c.getCampaignsList().get(0).getAmount());
            binding.name.setText("Name : " + c.getCampaignsList().get(0).getContact().getName());
            binding.age.setText("Age : " + c.getCampaignsList().get(0).getContact().getName());
            String address = c.getCampaignsList().get(0).getContact().getAddress().getVill() + " " + c.getCampaignsList().get(0).getContact().getAddress().getCity() + " " + c.getCampaignsList().get(0).getContact().getAddress().getDistrict() + " " + c.getCampaignsList().get(0).getContact().getAddress().getState() + " " + c.getCampaignsList().get(0).getContact().getAddress().getCountry() + "   -  " + c.getCampaignsList().get(0).getContact().getAddress().getPincode();


            binding.address.setText("Address : " + address);
            Glide.with(this).load(c.getCampaignsList().get(0).getCampaign_image_url()).into(binding.ivProfilePreview);
            Glide.with(this).load(c.getCampaignsList().get(0).getPaymentOptions().get(0).getUpi_qr_url()).into(binding.ivQrcode);
            Glide.with(this).load(c.getCampaignsList().get(0).getAdhar_url()).into(binding.ivAdharPreview);

            binding.Holdername.setText("Holder Name : " + c.getCampaignsList().get(0).getPaymentOptions().get(0).getName());
            binding.BankName.setText("Bank Name : " + c.getCampaignsList().get(0).getPaymentOptions().get(0).getBank_name());
            binding.accountNo.setText("Account No : " + c.getCampaignsList().get(0).getPaymentOptions().get(0).getAccount_no());
            binding.ifsc.setText("IFSC : " + c.getCampaignsList().get(0).getPaymentOptions().get(0).getIfsc());
            binding.upiId.setText("Upi Id : " + c.getCampaignsList().get(0).getPaymentOptions().get(0).getUpi());


        }


    }
}
