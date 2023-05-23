package com.example.agroseva.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.agroseva.data.campaign.Campaign;
import com.example.agroseva.data.campaign.CampaignItem;
import com.example.agroseva.data.models.Address;
import com.example.agroseva.databinding.FragmentManageBinding;
import com.example.agroseva.onboarding.AuthActivity;
import com.example.agroseva.ui.activities.CampaignActivity;
import com.example.agroseva.ui.activities.ManageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ManageFragment extends Fragment {
    FragmentManageBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    List<CampaignItem> campLists;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentManageBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.profileDetailsLayout.setVisibility(View.GONE);
        binding.btnManageDetails.setVisibility(View.GONE);


        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        binding.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.profileDetailsLayout.setVisibility(View.VISIBLE);
                binding.btnManageDetails.setVisibility(View.VISIBLE);

            }
        });

        binding.btnManageDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.profileDetailsLayout.setVisibility(View.GONE);
                binding.btnManageDetails.setVisibility(View.GONE);
            }
        });


        progressDialog = ProgressDialog.show(requireContext(), "Loading", "Please wait...", true);

        firestore.collection("users").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {


                    Address a = documentSnapshot.get("address", Address.class);
                    String address = a.getVill() + " " + a.getCity() + " " + a.getDistrict() + " " + a.getState() + " " + a.getCountry() + "   -  " + a.getPincode();
                    binding.edtAddress.setText(address);
                    String name = documentSnapshot.getString("name");
                    binding.tvName.setText(name);
                    String email = documentSnapshot.getString("email");
                    binding.edtEmail.setText(email);
                    String gender = documentSnapshot.getString("gender");
                    binding.edtGender.setText(gender);
                    String phone = documentSnapshot.getString("phone_no");
                    binding.edtPhone.setText(phone);
                    String age = documentSnapshot.getString("age");
                    binding.edtAge.setText(age);

                    String imageUrl = documentSnapshot.getString("profileImageUrl");

                    Glide.with(getContext()).load(imageUrl).into(binding.profilePic);
                    progressDialog.dismiss();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });


        binding.btnCreateCampaigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireActivity(), CampaignActivity.class);
                startActivity(i);
            }
        });


        binding.btnManageCampaigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireActivity(), ManageActivity.class);
                startActivity(i);
            }
        });


        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent i = new Intent(getActivity().getBaseContext(), AuthActivity.class);
                startActivity(i);
                Toast.makeText(getActivity().getBaseContext(), "Logged out Successfull", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

    }
    private void initCamp() {

        DocumentReference docRef = firestore.collection("campaigns").document(auth.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    Campaign temp = documentSnapshot.toObject(Campaign.class);
                    campLists.addAll(temp.getCampaignsList());
                    // do something with the list
                }
            }
        });


    }
}
