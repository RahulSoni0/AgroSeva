package com.example.agroseva.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agroseva.data.campaign.Campaign;
import com.example.agroseva.databinding.ActivityViewCampaignBinding;
import com.example.agroseva.ui.adapters.CampaignAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewCampaignActivity extends AppCompatActivity {

    List<Campaign> campaigns;

    CampaignAdapter adapter;
    ActivityViewCampaignBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewCampaignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = ProgressDialog.show(ViewCampaignActivity.this, "Loading", "Please wait...", true);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("campaigns");


//        Campaign.class

        campaigns = new ArrayList<>();
        adapter = new CampaignAdapter(campaigns, this);
        binding.rvCampaign.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCampaign.setHasFixedSize(true);
        binding.rvCampaign.setAdapter(adapter);
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        Campaign singleCamp = document.toObject(Campaign.class);
                        campaigns.add(singleCamp);
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                } else {
                    Log.d("1111", "Error getting documents: ", task.getException());
                    progressDialog.dismiss();
                }
            }
        });


    }
}