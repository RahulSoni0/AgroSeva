package com.example.agroseva.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agroseva.databinding.ActivityCropDetailsBinding;

public class CropDetailsActivity extends AppCompatActivity {

    ActivityCropDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCropDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}