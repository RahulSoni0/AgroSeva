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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.agroseva.R;
import com.example.agroseva.api.network.PincodeInterface;
import com.example.agroseva.api.network.RetrofitBuilder;
import com.example.agroseva.api.pincodeRes.PincodeModelResItem;
import com.example.agroseva.data.campaign.Campaign;
import com.example.agroseva.data.campaign.CampaignItem;
import com.example.agroseva.data.campaign.Contact;
import com.example.agroseva.data.campaign.PaymentOptions;
import com.example.agroseva.data.models.Address;
import com.example.agroseva.data.models.Profile;
import com.example.agroseva.databinding.ActivityCampaignBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignActivity extends AppCompatActivity {
    ActivityCampaignBinding binding;
    private FirebaseAuth auth;

    int PICK_CAMPAIGN_IMAGE_REQUEST = 3100;
    int PICK_ADHAR_IMAGE_REQUEST = 3200;
    int PICK_PROFILE_IMAGE_REQUEST = 3300;
    int PICK_QR_IMAGE_REQUEST = 3400;
    FirebaseStorage storage;
    StorageReference storageRef;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;

    private Uri filePathforCampaignImage;
    private Uri filePathforProfileImage;
    private Uri filePathforAdharImage;
    private Uri filePathforQrImage;

    List<CampaignItem> campLists;
    Profile p;
    Address ad;
    Campaign c;
    List<PaymentOptions> paymentOptions;

    String profileImage = "";
    String campaignImage = "";
    String adharImage = "";
    String QrImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCampaignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        storageRef = storage.getReference();
        initData();

        binding.campaignAuthor.UserCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    binding.campaignAuthorAddress.editHouseName.getEditText().setText(ad.getVill());
                    binding.campaignAuthorAddress.editPincode.getEditText().setText(ad.getPincode());
                    binding.campaignAuthorAddress.editCityName.getEditText().setText(ad.getCity());
                    binding.campaignAuthorAddress.editDistrict.getEditText().setText(ad.getDistrict());
                    binding.campaignAuthorAddress.editStateName.getEditText().setText(ad.getState());
                    binding.campaignAuthorAddress.editCountryName.getEditText().setText(ad.getCountry());

                    binding.campaignAuthor.editName.getEditText().setText(p.getName());
                    binding.campaignAuthor.editEmail.getEditText().setText(p.getEmail());
                    binding.campaignAuthor.editGender.getEditText().setText(p.getGender());
                    binding.campaignAuthor.editPhone.getEditText().setText(p.getPhone_no());
                    binding.campaignAuthor.editAge.getEditText().setText(p.getAge());
                    profileImage = p.getProfileImageUrl();
                    Glide.with(CampaignActivity.this).load(profileImage).into(binding.campaignAuthor.ivProfilePreview);


                }

            }
        });
        final String[] pin = {""};

        binding.campaignDetails.campainDetailsLayout.setVisibility(View.VISIBLE);


        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.campaignDetailsMenu: {

                        binding.campaignDetails.campainDetailsLayout.setVisibility(View.VISIBLE);
                        binding.campaignPayment.campaignPaymentDetails.setVisibility(View.GONE);
                        binding.campaignAuthor.campaignAuthorLayout.setVisibility(View.GONE);
                        binding.campaignAuthorAddress.campaignAuthorAddress.setVisibility(View.GONE);
                        break;

                    }
                    case R.id.paymentsDetailsMenu: {
                        binding.campaignDetails.campainDetailsLayout.setVisibility(View.GONE);
                        binding.campaignPayment.campaignPaymentDetails.setVisibility(View.VISIBLE);
                        binding.campaignAuthor.campaignAuthorLayout.setVisibility(View.GONE);
                        binding.campaignAuthorAddress.campaignAuthorAddress.setVisibility(View.GONE);
                        break;


                    }
                    case R.id.personalDetailsMenu: {
                        binding.campaignDetails.campainDetailsLayout.setVisibility(View.GONE);
                        binding.campaignAuthor.campaignAuthorLayout.setVisibility(View.VISIBLE);
                        binding.campaignPayment.campaignPaymentDetails.setVisibility(View.GONE);
                        ;
                        binding.campaignAuthorAddress.campaignAuthorAddress.setVisibility(View.GONE);
                        break;
                    }
                    case R.id.addressFormMenu: {
                        binding.campaignDetails.campainDetailsLayout.setVisibility(View.GONE);
                        binding.campaignPayment.campaignPaymentDetails.setVisibility(View.GONE);
                        binding.campaignAuthor.campaignAuthorLayout.setVisibility(View.GONE);
                        binding.campaignAuthorAddress.campaignAuthorAddress.setVisibility(View.VISIBLE);
                        break;
                    }


                }
                return true;
            }
        });

        binding.campaignAuthorAddress.editPincode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.equals("") || s.length() < 6) {
                    binding.campaignAuthorAddress.editPincode.getEditText().requestFocus();
                    binding.campaignAuthorAddress.btnVerifyPincode.setEnabled(false);
                } else {
                    binding.campaignAuthorAddress.btnVerifyPincode.setEnabled(true);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                pin[0] = s.toString();

            }
        });


        binding.campaignDetails.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_CAMPAIGN_IMAGE_REQUEST);
            }
        });
        binding.campaignPayment.btnUploadQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.campaignAuthor.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_PROFILE_IMAGE_REQUEST);
            }
        });
        binding.campaignPayment.btnSelectQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_QR_IMAGE_REQUEST);
            }
        });
        binding.campaignAuthor.btnSelectAdhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_ADHAR_IMAGE_REQUEST);
            }
        });


        binding.campaignDetails.btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCampaignImage();
            }
        });

        binding.campaignAuthor.btnUploadAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAdharImage();
            }
        });
        binding.campaignPayment.btnUploadQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadQrImage();
            }
        });

        binding.campaignAuthor.btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });

        binding.campaignAuthorAddress.btnVerifyPincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

// Hide the soft keyboard
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                progressDialog = ProgressDialog.show(CampaignActivity.this, "Loading", "Please wait...", true);
                PincodeInterface pincode = RetrofitBuilder.getPincode();

                Call<List<PincodeModelResItem>> call = pincode.getPincodeDetails(pin[0]);
                call.enqueue(new Callback<List<PincodeModelResItem>>() {
                    @Override
                    public void onResponse(Call<List<PincodeModelResItem>> call, Response<List<PincodeModelResItem>> response) {

                        if (response != null && response.body() != null) {

                            List<PincodeModelResItem> r = (List<PincodeModelResItem>) response.body();
                            if (r.get(0).getPostOffice() != null) {
                                binding.campaignAuthorAddress.editCityName.getEditText().setText(r.get(0).getPostOffice().get(0).getDivision().toString());
                                binding.campaignAuthorAddress.editDistrict.getEditText().setText(r.get(0).getPostOffice().get(0).getDistrict().toString());
                                binding.campaignAuthorAddress.editCountryName.getEditText().setText(r.get(0).getPostOffice().get(0).getCountry().toString());
                                binding.campaignAuthorAddress.editStateName.getEditText().setText(r.get(0).getPostOffice().get(0).getState().toString());
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(CampaignActivity.this, "Enter correct Pincode", Toast.LENGTH_SHORT).show();
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

        binding.campaignAuthorAddress.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verify and create campaign....

                if (!binding.campaignAuthorAddress.editPassword.getEditText().getText().toString().trim().equals(p.getPasscode())) {
                    Toast.makeText(CampaignActivity.this, "Passcode MisMatched", Toast.LENGTH_SHORT).show();
                    binding.campaignAuthorAddress.editPassword.getEditText().requestFocus();

                } else {
                    Toast.makeText(CampaignActivity.this, "Passcode Matched", Toast.LENGTH_SHORT).show();

                    if (!profileImage.equals("") && !QrImage.equals("")

                            && !binding.campaignPayment.editAcHolderName.getEditText().getText().toString().trim().equals("") && !binding.campaignPayment.editAcNo.getEditText().getText().toString().trim().equals("") && !binding.campaignPayment.editBankName.getEditText().getText().toString().trim().equals("") && !binding.campaignPayment.editIfsc.getEditText().getText().toString().trim().equals("") && !binding.campaignPayment.editupiId.getEditText().getText().toString().trim().equals("") && !campaignImage.equals("") && !adharImage.equals("") && !binding.campaignAuthor.editPhone.getEditText().getText().toString().trim().equals("") && !binding.campaignAuthor.editGender.getEditText().getText().toString().trim().equals("") && !binding.campaignAuthor.editName.getEditText().getText().toString().trim().equals("") && !binding.campaignAuthor.editEmail.getEditText().getText().toString().trim().equals("") && !binding.campaignAuthorAddress.editPassword.getEditText().getText().toString().trim().equals("") && (!binding.campaignAuthorAddress.editHouseName.getEditText().getText().toString().trim().equals("") || !binding.campaignAuthorAddress.editRoadAreaColony.getEditText().getText().toString().trim().equals("")) && !binding.campaignAuthorAddress.editPincode.getEditText().getText().toString().trim().equals("") && !binding.campaignAuthor.editAdhar.getEditText().getText().toString().trim().equals("") && !binding.campaignAuthorAddress.editCityName.getEditText().getText().toString().trim().equals("") && !binding.campaignAuthorAddress.editStateName.getEditText().getText().toString().trim().equals("") && !binding.campaignAuthorAddress.editCountryName.getEditText().getText().toString().trim().equals("") && !binding.campaignAuthorAddress.editDistrict.getEditText().getText().toString().trim().equals("") && !binding.campaignDetails.editCmpTitle.getEditText().getText().toString().trim().equals("") && !binding.campaignDetails.editCmpDescription.getEditText().getText().toString().trim().equals("") && !binding.campaignDetails.editCmpProduct.getEditText().getText().toString().trim().equals("") && !binding.campaignDetails.editAmount.getEditText().getText().toString().trim().equals("") && !binding.campaignDetails.editDeadline.getEditText().getText().toString().trim().equals("")) {

                        //create campaign with these data....
                        campLists = new ArrayList<>();
                        String title = binding.campaignDetails.editCmpTitle.getEditText().getText().toString();
                        String description = binding.campaignDetails.editCmpDescription.getEditText().getText().toString();
                        String product = binding.campaignDetails.editCmpProduct.getEditText().getText().toString();
                        String date = binding.campaignDetails.editDeadline.getEditText().getText().toString();
                        String amount = binding.campaignDetails.editAmount.getEditText().getText().toString();
                        String Bannerurl = campaignImage;
                        String adharNo = binding.campaignAuthor.editAdhar.getEditText().getText().toString();
                        String adharUrl = adharImage;

                        String holderName = binding.campaignPayment.editAcHolderName.getEditText().getText().toString();
                        String bankName = binding.campaignPayment.editBankName.getEditText().getText().toString();
                        String accno = binding.campaignPayment.editAcNo.getEditText().getText().toString();
                        String upi = binding.campaignPayment.editupiId.getEditText().getText().toString();
                        String ifsc = binding.campaignPayment.editIfsc.getEditText().getText().toString();
                        String qrUrl = QrImage;


                        Campaign c1 = new Campaign();
                        CampaignItem campitem = new CampaignItem();
                        campitem.setCampaign_title(title);
                        campitem.setCampaign_description(description);
                        campitem.setProduct_description(product);
                        campitem.setAmount(amount);
                        campitem.setCampaign_image_url(Bannerurl);
                        campitem.setAdhar_no(adharNo);
                        campitem.setAdhar_url(adharUrl);
                        campitem.setDonors(new ArrayList<>());


                        String address = ad.getVill() + " " + ad.getCity() + " " + ad.getDistrict() + " " + ad.getState() + " " + ad.getCountry() + "   -  " + ad.getPincode();


                        Contact cc = new Contact();
                        cc.setAddress(ad);
                        cc.setName(p.getName());
                        cc.setEmail(p.getEmail());
                        cc.setAge(p.getAge());
                        cc.setPhone_no(p.getPhone_no());
                        cc.setGender(p.getGender());


                        campitem.setContact(cc);

                        PaymentOptions payment = new PaymentOptions();
                        payment.setAccount_no(accno);
                        payment.setBank_name(bankName);
                        payment.setName(holderName);
                        payment.setUpi(upi);
                        payment.setIfsc(ifsc);
                        payment.setUpi_qr_url(qrUrl);
                        paymentOptions = new ArrayList<>();
                        paymentOptions.add(payment);
                        campitem.setPaymentOptions(paymentOptions);

                        campLists.add(campitem);
                        c1.setCampaignsList(campLists);
                        // now put this c1 to new collection as well as user's profile..
                        p.setCampaign(c1);
                        firestore.collection("users").document(auth.getUid()).set(p);
                        firestore.collection("campaigns").document(auth.getUid()).set(c1);


                        Toast.makeText(CampaignActivity.this, "Campaign created successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CampaignActivity.this, MainActivity.class);
                        startActivity(i);

                        //here set both user and campaign....


                    } else {
                        Toast.makeText(CampaignActivity.this, "please fill all the field correctly", Toast.LENGTH_LONG).show();
                        Toast.makeText(CampaignActivity.this, "please upload all the images correctly", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CAMPAIGN_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
// Create a reference to the image file you want to upload
            filePathforCampaignImage = data.getData();
            try {
                // on below line getting bitmap for image from file uri.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathforCampaignImage);
                binding.campaignDetails.ivProfilePreview.setImageBitmap(bitmap);
                // on below line setting bitmap for our image view.

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        if (requestCode == PICK_ADHAR_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
// Create a reference to the image file you want to upload
            filePathforAdharImage = data.getData();
            try {
                // on below line getting bitmap for image from file uri.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathforAdharImage);
                binding.campaignAuthor.ivAdharPreview.setImageBitmap(bitmap);
                // on below line setting bitmap for our image view.

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        if (requestCode == PICK_PROFILE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
// Create a reference to the image file you want to upload
            filePathforProfileImage = data.getData();
            try {
                // on below line getting bitmap for image from file uri.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathforProfileImage);
                binding.campaignAuthor.ivProfilePreview.setImageBitmap(bitmap);
                // on below line setting bitmap for our image view.

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (requestCode == PICK_QR_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri imageUri = data.getData();
// Create a reference to the image file you want to upload
            filePathforQrImage = data.getData();
            try {
                // on below line getting bitmap for image from file uri.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathforQrImage);
                binding.campaignPayment.ivQRPreview.setImageBitmap(bitmap);
                // on below line setting bitmap for our image view.

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void uploadProfileImage() {
        if (filePathforProfileImage != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePathforProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Image uploaded successfully
                            // Dismiss dialog

                            // url...
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    profileImage = task.getResult().toString();
                                    //next work with URL

                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(CampaignActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(CampaignActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void uploadQrImage() {
        if (filePathforQrImage != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePathforQrImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Image uploaded successfully
                            // Dismiss dialog

                            // url...
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    QrImage = task.getResult().toString();
                                    //next work with URL

                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(CampaignActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(CampaignActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void uploadCampaignImage() {
        if (filePathforCampaignImage != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePathforCampaignImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Image uploaded successfully
                            // Dismiss dialog

                            // url...
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    campaignImage = task.getResult().toString();
                                    //next work with URL

                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(CampaignActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(CampaignActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void uploadAdharImage() {
        if (filePathforAdharImage != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePathforAdharImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Image uploaded successfully
                            // Dismiss dialog

                            // url...
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    adharImage = task.getResult().toString();
                                    //next work with URL

                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(CampaignActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(CampaignActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void initData() {
        progressDialog = ProgressDialog.show(CampaignActivity.this, "Loading", "Please wait...", true);

        firestore.collection("users").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Address a = documentSnapshot.get("address", Address.class);
                    ad = new Address();
                    ad = a;

                    p = new Profile();
                    p.setAddress(a);
                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String gender = documentSnapshot.getString("gender");
                    String phone = documentSnapshot.getString("phone_no");
                    String age = documentSnapshot.getString("age");
                    String passcode = documentSnapshot.getString("passcode");

                    String imageUrl = documentSnapshot.getString("profileImageUrl");
                    p.setName(name);
                    p.setEmail(email);
                    p.setGender(gender);
                    p.setPhone_no(phone);
                    p.setAge(age);
                    p.setProfileImageUrl(imageUrl);
                    p.setPasscode(passcode);
                    progressDialog.dismiss();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });

    }
}