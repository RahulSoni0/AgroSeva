package com.example.agroseva.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.agroseva.data.campaign.Contact;
import com.example.agroseva.data.market.Product;
import com.example.agroseva.data.models.Address;
import com.example.agroseva.databinding.ActivityCreateProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class CreateProductActivity extends AppCompatActivity {
    ActivityCreateProductBinding binding;

    int PICK_IMAGE_REQUEST = 3000;
    String cat;
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
        binding = ActivityCreateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cat = "Equipments";
        storage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageRef = storage.getReference();


        binding.btnVerifyPincode.setEnabled(false);

        final String[] pin = {""};
        binding.editPincode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.equals("") || s.length() < 6) {
                    binding.editPincode.getEditText().requestFocus();
                    binding.btnVerifyPincode.setEnabled(false);
                } else {
                    binding.btnVerifyPincode.setEnabled(true);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                pin[0] = s.toString();

            }
        });


        binding.btnVerifyPincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

// Hide the soft keyboard
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                progressDialog = ProgressDialog.show(CreateProductActivity.this, "Loading", "Please wait...", true);
                PincodeInterface pincode = RetrofitBuilder.getPincode();

                Call<List<PincodeModelResItem>> call = pincode.getPincodeDetails(pin[0]);
                call.enqueue(new Callback<List<PincodeModelResItem>>() {
                    @Override
                    public void onResponse(Call<List<PincodeModelResItem>> call, Response<List<PincodeModelResItem>> response) {

                        if (response != null && response.body() != null) {

                            List<PincodeModelResItem> r = (List<PincodeModelResItem>) response.body();
                            if (r.get(0).getPostOffice() != null) {
                                binding.editCityName.getEditText().setText(r.get(0).getPostOffice().get(0).getDivision().toString());
                                binding.editDistrict.getEditText().setText(r.get(0).getPostOffice().get(0).getDistrict().toString());
                                binding.editCountryName.getEditText().setText(r.get(0).getPostOffice().get(0).getCountry().toString());
                                binding.editStateName.getEditText().setText(r.get(0).getPostOffice().get(0).getState().toString());
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(CreateProductActivity.this, "Enter correct Pincode", Toast.LENGTH_SHORT).show();
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


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        binding.categorySpinner.setAdapter(adapter);

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                cat = parent.getItemAtPosition(position).toString();
                Toast.makeText(CreateProductActivity.this, "" + cat, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        binding.btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // verify and submit here in market with uid from auth .get uid...
                // and rest will be entered by user


                String name = binding.editdnrName.getEditText().getText().toString().trim();
                String age = binding.editdnrAge.getEditText().getText().toString().trim();
                String gender = binding.editdnrGenger.getEditText().getText().toString().trim();


                String email = binding.editdnrEmail.getEditText().getText().toString().trim();
                String phoneNo = binding.editdnrphone.getEditText().getText().toString().trim();
                String productUrl = "";
                productUrl = imageUrl;
                String vill = binding.editHouseName.getEditText().getText().toString().trim() + " " + binding.editRoadAreaColony.getEditText().getText().toString().trim();
                String city = binding.editCityName.getEditText().getText().toString().trim();
                String district = binding.editDistrict.getEditText().getText().toString().trim();
                String state = binding.editStateName.getEditText().getText().toString().trim();
                String country = binding.editCountryName.getEditText().getText().toString().trim();
                String pincod = binding.editPincode.getEditText().getText().toString().trim();

                String productTitle = binding.editTitle.getEditText().getText().toString().trim();
                String productDesc = binding.editDesc.getEditText().getText().toString().trim();
                String productAvaillability = binding.editAvailability.getEditText().getText().toString().trim();
                String productRate = binding.editAmount.getEditText().getText().toString().trim();
                String category = cat;
                String whatsappNumber = binding.editWhatsapp.getEditText().getText().toString().trim();


                if (!productTitle.equals("") && !productDesc.equals("") && !imageUrl.equals("") && !binding.editdnrphone.getEditText().getText().toString().trim().equals("") && !binding.editdnrGenger.getEditText().getText().toString().trim().equals("") && !binding.editdnrName.getEditText().getText().toString().trim().equals("") && !binding.editdnrEmail.getEditText().getText().toString().trim().equals("") && (!binding.editHouseName.getEditText().getText().toString().trim().equals("") || !binding.editRoadAreaColony.getEditText().getText().toString().trim().equals("")) && !binding.editPincode.getEditText().getText().toString().trim().equals("") && !binding.editdnrAge.getEditText().getText().toString().trim().equals("")

                ) {

                    //upload and create entry here......
                    Address a = new Address(vill, city, district, state, country, pincod);
                    Contact cc = new Contact();
                    cc.setName(name);
                    cc.setAddress(a);
                    cc.setPhone_no(phoneNo);
                    cc.setAge(age);
                    cc.setEmail(email);
                    cc.setGender(gender);

                    // now set this to curr doc it thing..
                    Product p = new Product();
                    p.setCat(cat);
                    p.setProduct_name(productTitle);
                    p.setProduct_availability(productAvaillability);
                    p.setProduct_desc(productDesc);
                    p.setProduct_Imageurl(imageUrl);
                    p.setProduct_price(productRate);
                    p.setWhatsappno(whatsappNumber);
                    p.setContact(cc);

                    firebaseFirestore.collection("market").document(auth.getUid()).set(p);


                    Toast.makeText(CreateProductActivity.this, "Created successfully! check market", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CreateProductActivity.this, MainActivity.class);
                    startActivity(i);


                }


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

                binding.ivProfilePreview.setImageBitmap(bitmap);
                // on below line setting bitmap for our image view.

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


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
                            Toast.makeText(CreateProductActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(CreateProductActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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