package com.example.agroseva.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agroseva.data.market.Product;
import com.example.agroseva.databinding.FragmentMarketBinding;
import com.example.agroseva.ui.activities.CreateProductActivity;
import com.example.agroseva.ui.adapters.MarketAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment {


    FragmentMarketBinding binding;
    List<Product> products;
    List<Product> l1;
    List<Product> l2;
    List<Product> l3;
    List<Product> l4;



    MarketAdapter a1;
    MarketAdapter a2;
    MarketAdapter a3;
    MarketAdapter a4;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(), CreateProductActivity.class);

                startActivity(i);
            }
        });

        progressDialog = ProgressDialog.show(requireContext(), "Loading", "Please wait...", true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("market");
        products = new ArrayList<>();
        l1 = new ArrayList<>();
        l2 = new ArrayList<>();
        l3 = new ArrayList<>();
        l4 = new ArrayList<>();


        a1 = new MarketAdapter(requireContext(), l1);
        binding.rvMachines.setHasFixedSize(true);
        binding.rvMachines.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvMachines.setAdapter(a1);

        a2 = new MarketAdapter(requireContext(), l2);
        binding.rvRaw.setHasFixedSize(true);
        binding.rvRaw.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvRaw.setAdapter(a2);

        a3 = new MarketAdapter(requireContext(), l3);
        binding.rvftpst.setHasFixedSize(true);
        binding.rvftpst.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvftpst.setAdapter(a3);
        a4 = new MarketAdapter(requireContext(), l4);
        binding.rvfodder.setHasFixedSize(true);
        binding.rvfodder.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvfodder.setAdapter(a4);


        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        Product singleProd = document.toObject(Product.class);
                        products.add(singleProd);
                    }


                    for (Product pp : products
                    ) {
                        if (pp.getCat().equals("Equipments")) {
                            l1.add(pp);

                        } else if (pp.getCat().equals("Raw Materials")) {
                            l2.add(pp);

                        } else if (pp.getCat().equals("Fertilizers and Pesticides")) {
                            l3.add(pp);
                        } else {
                            l4.add(pp);
                        }
                    }


                    progressDialog.dismiss();

                    a1.notifyDataSetChanged();
                    a2.notifyDataSetChanged();
                    a3.notifyDataSetChanged();
                    a4.notifyDataSetChanged();
                } else {
                    Log.d("1111", "Error getting documents: ", task.getException());
                    progressDialog.dismiss();
                }
            }
        });


    }
}
