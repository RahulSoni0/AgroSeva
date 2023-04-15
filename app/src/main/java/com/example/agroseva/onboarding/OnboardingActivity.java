package com.example.agroseva.onboarding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agroseva.R;
import com.example.agroseva.api.network.PincodeInterface;
import com.example.agroseva.api.network.RetrofitBuilder;
import com.example.agroseva.api.pincodeRes.PincodeModelResItem;
import com.example.agroseva.data.models.Address;
import com.example.agroseva.data.models.Profile;
import com.example.agroseva.databinding.ActivityOnboardingBinding;
import com.example.agroseva.ui.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnboardingActivity extends AppCompatActivity {

    ActivityOnboardingBinding binding;
    int PICK_IMAGE_REQUEST = 3000;
    String gender;
    String imageUrl = "";
    FirebaseStorage storage;
    StorageReference storageRef;
    private Uri filePath;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gender = "male";
        storage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageRef = storage.getReference();

        binding.PersonalFormLayoutTo.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        binding.PersonalFormLayoutTo.personalFormLayout.setVisibility(View.VISIBLE);
        binding.AddressFormLayoutTo.btnVerifyPincode.setEnabled(false);

        final String[] pin = {""};
        binding.AddressFormLayoutTo.editPincode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.equals("") || s.length() < 6) {
                    binding.AddressFormLayoutTo.editPincode.getEditText().requestFocus();
                    binding.AddressFormLayoutTo.btnVerifyPincode.setEnabled(false);
                } else {
                    binding.AddressFormLayoutTo.btnVerifyPincode.setEnabled(true);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                pin[0] = s.toString();

            }
        });


        binding.AddressFormLayoutTo.btnVerifyPincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

// Hide the soft keyboard
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                progressDialog = ProgressDialog.show(OnboardingActivity.this, "Loading", "Please wait...", true);
                PincodeInterface pincode = RetrofitBuilder.getPincode();

                Call<List<PincodeModelResItem>> call = pincode.getPincodeDetails(pin[0]);
                call.enqueue(new Callback<List<PincodeModelResItem>>() {
                    @Override
                    public void onResponse(Call<List<PincodeModelResItem>> call, Response<List<PincodeModelResItem>> response) {

                        if (response != null && response.body() != null) {

                            List<PincodeModelResItem> r = (List<PincodeModelResItem>) response.body();
                            if (r.get(0).getPostOffice() != null) {
                                binding.AddressFormLayoutTo.editCityName.getEditText().setText(r.get(0).getPostOffice().get(0).getDivision().toString());
                                binding.AddressFormLayoutTo.editDistrict.getEditText().setText(r.get(0).getPostOffice().get(0).getDistrict().toString());
                                binding.AddressFormLayoutTo.editCountryName.getEditText().setText(r.get(0).getPostOffice().get(0).getCountry().toString());
                                binding.AddressFormLayoutTo.editStateName.getEditText().setText(r.get(0).getPostOffice().get(0).getState().toString());
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(OnboardingActivity.this, "Enter correct Pincode", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<List<PincodeModelResItem>> call, Throwable t) {
                        progressDialog.dismiss();
                    }


                });


            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        binding.PersonalFormLayoutTo.genderSpinner.setAdapter(adapter);

        binding.PersonalFormLayoutTo.genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                gender = parent.getItemAtPosition(position).toString();
                Toast.makeText(OnboardingActivity.this, "" + gender, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.AddressFormLayoutTo.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = binding.PersonalFormLayoutTo.editFirstName.getEditText().getText().toString().trim() + " " + binding.PersonalFormLayoutTo.editMiddleName.getEditText().getText().toString().trim() + " " + binding.PersonalFormLayoutTo.editLastName.getEditText().getText().toString().trim();
                String age = binding.PersonalFormLayoutTo.editAge.getEditText().getText().toString().trim();
                String gen = "";
                gen = gender;

                String email = binding.PersonalFormLayoutTo.editEmail.getEditText().getText().toString().trim();
                String phoneNo = binding.PersonalFormLayoutTo.editPhone.getEditText().getText().toString().trim();
                String profileImageUrl = "";
                profileImageUrl = imageUrl;
                String vill = binding.AddressFormLayoutTo.editHouseName.getEditText().getText().toString().trim() + " " + binding.AddressFormLayoutTo.editRoadAreaColony.getEditText().getText().toString().trim();
                String city = binding.AddressFormLayoutTo.editCityName.getEditText().getText().toString().trim();
                String district = binding.AddressFormLayoutTo.editDistrict.getEditText().getText().toString().trim();
                String state = binding.AddressFormLayoutTo.editStateName.getEditText().getText().toString().trim();
                String country = binding.AddressFormLayoutTo.editCountryName.getEditText().getText().toString().trim();
                String pincod = binding.AddressFormLayoutTo.editPincode.getEditText().getText().toString().trim();
                String passcode = binding.PersonalFormLayoutTo.editPassword.getEditText().getText().toString().trim();

                if (!imageUrl.equals("") && !binding.PersonalFormLayoutTo.editPhone.getEditText().getText().toString().trim().equals("") && !gender.equals("") && !binding.PersonalFormLayoutTo.editFirstName.getEditText().getText().toString().trim().equals("") && !binding.PersonalFormLayoutTo.editLastName.getEditText().getText().toString().trim().equals("") && !binding.PersonalFormLayoutTo.editEmail.getEditText().getText().toString().trim().equals("") && !binding.PersonalFormLayoutTo.editPassword.getEditText().getText().toString().trim().equals("") && (!binding.AddressFormLayoutTo.editHouseName.getEditText().getText().toString().trim().equals("") || !binding.AddressFormLayoutTo.editRoadAreaColony.getEditText().getText().toString().trim().equals("")) && !binding.AddressFormLayoutTo.editPincode.getEditText().getText().toString().trim().equals("") && !binding.PersonalFormLayoutTo.editAge.getEditText().getText().toString().trim().equals("")

                ) {

                    //upload and create entry here......
                    Address a = new Address(vill, city, district, state, country, pincod);

                    Profile p = new Profile();
                    p.setName(name);
                    p.setAge(age);
                    p.setGender(gender);
                    p.setEmail(email);
                    p.setPhone_no(phoneNo);
                    p.setProfileImageUrl(profileImageUrl);
                    p.setAddress(a);
                    p.setPasscode(passcode);

                    firebaseFirestore.collection("users").document(auth.getUid()).set(p);


                    Toast.makeText(OnboardingActivity.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(OnboardingActivity.this, MainActivity.class);
                    startActivity(i);


                }


            }
        });


        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.personalDetails: {

                        binding.PersonalFormLayoutTo.personalFormLayout.setVisibility(View.VISIBLE);
                        binding.AddressFormLayoutTo.addressFormLayout.setVisibility(View.GONE);
                        break;

                    }
                    case R.id.addressForm: {
                        binding.PersonalFormLayoutTo.personalFormLayout.setVisibility(View.GONE);
                        binding.AddressFormLayoutTo.addressFormLayout.setVisibility(View.VISIBLE);
                        break;
                    }


                }
                return true;
            }
        });
        binding.PersonalFormLayoutTo.btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
// Create a reference to the image file you want to upload
            filePath = data.getData();
            try {
                // on below line getting bitmap for image from file uri.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                binding.PersonalFormLayoutTo.ivProfilePreview.setImageBitmap(bitmap);
                // on below line setting bitmap for our image view.

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


//            ProgressDialog progressDialog = new ProgressDialog(this);
//            // on below line setting title and message for our progress dialog and displaying our progress dialog.
//            progressDialog.setTitle("Uploading...");
//            progressDialog.setMessage("Uploading your image..");
//            progressDialog.show();
            // Upload the image to Firebase Storage

            // Use the image URI as needed
        }


    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Image uploaded successfully
                            // Dismiss dialog

                            // url...
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    imageUrl = task.getResult().toString();
                                    //next work with URL

                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(OnboardingActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(OnboardingActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

}
