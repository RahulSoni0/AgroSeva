package com.example.agroseva.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agroseva.data.campaign.Campaign;
import com.example.agroseva.data.campaign.CampaignItem;
import com.example.agroseva.data.market.Product;
import com.example.agroseva.data.market.ProductList;
import com.example.agroseva.databinding.ActivityManageBinding;
import com.example.agroseva.ui.adapters.ManageCampaignAdapter;
import com.example.agroseva.ui.adapters.ManageMarketAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ManageActivity extends AppCompatActivity implements ManageCampaignAdapter.ManageCampaignAdapterListener, ManageMarketAdapter.ManageMarketAdapterListener {

    private ActivityManageBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private ManageCampaignAdapter cAdapter;
    private ManageMarketAdapter mAdapter;
    List<CampaignItem> campaigns;
    List<Product> products;
    private ProgressDialog progressDialog;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = ProgressDialog.show(ManageActivity.this, "Loading", "Please wait...", true);
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Campaign"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Market"));
        db = FirebaseFirestore.getInstance();


        campaigns = new ArrayList<>();
        products = new ArrayList<>();
        initData();
        cAdapter = new ManageCampaignAdapter(campaigns, this);
        mAdapter = new ManageMarketAdapter(this, products);
        binding.rvMain.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMain.setHasFixedSize(true);
        binding.rvMain.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
        cAdapter.setListener(this); // Set the listener
        mAdapter.setListener(this);


        binding.tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection event
                int position = tab.getPosition();
                // Perform actions based on the selected tab
                switch (position) {
                    case 0:
                        // Code for the first tab

                        Log.d("12345", "onTabSelected: " + campaigns.toString());

                        Toast.makeText(ManageActivity.this, "Campaigns", Toast.LENGTH_SHORT).show();
                        binding.rvMain.setAdapter(cAdapter);
                        cAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        // Code for the second tab
                        Toast.makeText(ManageActivity.this, "MarketPlace", Toast.LENGTH_SHORT).show();
                        binding.rvMain.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        break;

                    default:
                        // Code for other tabs
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselection event
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselection event
            }
        });
    }

    private void initData() {
        db.collection("campaigns").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Campaign c = documentSnapshot.toObject(Campaign.class);
                    for (CampaignItem e : c.getCampaignsList()
                    ) {
                        campaigns.add(e);
                    }
                }
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
        db.collection("market").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ProductList p = documentSnapshot.toObject(ProductList.class);
                    for (Product e : p.getProducts()
                    ) {
                        products.add(e);
                    }
                }
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClicked(CampaignItem c) {


        // delete this one add add rest of the lists.....

        Campaign c1 = new Campaign();


        // now set this to curr doc it thing..

        for (int i = 0; i < campaigns.size(); i++) {

            if (campaigns.get(i).getCampaign_title().equals(c.getCampaign_title())) {
                campaigns.remove(c);
            }

        }
        c1.setCampaignsList(campaigns);


        Toast.makeText(this, "Campaign deleted!!", Toast.LENGTH_SHORT).show();
        cAdapter.notifyDataSetChanged();
        //modifive dis 1
        db.collection("campaigns").document(c.getUid()).set(c1);


    }

    @Override
    public void onItemClicked(Product p) {

        // here delete product...
        ProductList p1 = new ProductList();


        // now set this to curr doc it thing..

        for (int i = 0; i < products.size(); i++) {

            if (products.get(i).getProduct_name().equals(p.getProduct_name())) {
                products.remove(p);
            }

        }
        p1.setProducts(products);

        Toast.makeText(this, "Product Deleted!!!", Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();
        //modifive dis 1
        db.collection("market").document(auth.getUid()).set(p1);

    }
}
