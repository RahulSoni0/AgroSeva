package com.example.agroseva.onboarding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.agroseva.R
import com.example.agroseva.databinding.ActivityOnboardingBinding
import com.example.agroseva.databinding.AddressFormLayoutBinding
import com.example.agroseva.databinding.DescriptionLayoutFormBinding
import com.example.agroseva.databinding.PersonalFormLayoutBinding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding;

    private lateinit var personalFormBinding: PersonalFormLayoutBinding
    private lateinit var addressFormLayoutBinding: AddressFormLayoutBinding
    private lateinit var descriptionLayoutFormBinding: DescriptionLayoutFormBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        personalFormBinding =
            PersonalFormLayoutBinding.bind(binding.PersonalFormLayoutTo.personalFormLayout)
        addressFormLayoutBinding =
            AddressFormLayoutBinding.bind(binding.AddressFormLayoutTo.addressFormLayout)
        descriptionLayoutFormBinding =
            DescriptionLayoutFormBinding.bind(binding.DescriptionFormLayoutTo.descriptionLayout)

        var receivedData = intent?.getStringExtra("userKey")
        if (receivedData != null) {
            // do something with the data
//            binding.tvUid.text = receivedData.toString()


        }

        // setting default as personal Frag..
        binding.bottomNav.selectedItemId = R.id.personalDetails
        binding.PersonalFormLayoutTo.root.visibility = View.VISIBLE
        binding.AddressFormLayoutTo.root.visibility = View.GONE
        binding.DescriptionFormLayoutTo.root.visibility = View.GONE


        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.personalDetails -> {
                    binding.PersonalFormLayoutTo.root.visibility = View.VISIBLE
                    binding.AddressFormLayoutTo.root.visibility = View.GONE
                    binding.DescriptionFormLayoutTo.root.visibility = View.GONE
                }


                R.id.addressForm -> {
                    binding.PersonalFormLayoutTo.root.visibility = View.GONE
                    binding.AddressFormLayoutTo.root.visibility = View.VISIBLE
                    binding.DescriptionFormLayoutTo.root.visibility = View.GONE
                }


                R.id.detailsForm -> {
                    binding.PersonalFormLayoutTo.root.visibility = View.GONE
                    binding.AddressFormLayoutTo.root.visibility = View.GONE
                    binding.DescriptionFormLayoutTo.root.visibility = View.VISIBLE
                }

            }
            true
        }


    }
}