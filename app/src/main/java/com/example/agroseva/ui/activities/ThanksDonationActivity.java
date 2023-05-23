package com.example.agroseva.ui.activities;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agroseva.data.campaign.Campaign;
import com.example.agroseva.data.campaign.CampaignItem;
import com.example.agroseva.databinding.ActivityThanksDonationBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ThanksDonationActivity extends AppCompatActivity {
    ActivityThanksDonationBinding binding;
    CampaignItem c;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThanksDonationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();

        if (getIntent() != null) {
            c = (CampaignItem) getIntent().getSerializableExtra("data");
        }
        Toast.makeText(this, c.toString(), Toast.LENGTH_SHORT).show();
        long currentTimestamp = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat2.format(new Date(currentTimestamp));
        String currentDateTime = dateFormat.format(new Date(currentTimestamp));
        binding.Campaignname.setText("Campaign Name: " + c.getCampaign_title());
        binding.recieptName.setText("This certificate is presented to " + c.getDonors().get(c.getDonors().size() - 1).getContact().getName() + " in recognition and sincere appreciation of your generous contribution to " +

                c.getContact().getName() +


                ". Your donation of :  ₹ " + c.getDonors().get(c.getDonors().size() - 1).getAmount() + " on " + currentDate + " has greatly supported our efforts to help farmers to contribute to the society");

        String add = c.getContact().getAddress().getVill() + " " + c.getContact().getAddress().getCity() + " " + c.getContact().getAddress().getDistrict() + " " + c.getContact().getAddress().getState() + " - " + c.getContact().getAddress().getPincode() + " ";
        binding.recieptDate.setText("Date :  " + currentDateTime + " ");
        binding.recieptNumber.setText("Receipt no :  " + currentTimestamp);
        binding.name.setText("Name : " + c.getContact().getName());
        binding.age.setText("Age : " + c.getContact().getAge());
        binding.emailId.setText("Email Id : " + c.getContact().getEmail());
        binding.phoneNumber.setText("Phone no : " + c.getContact().getPhone_no());
        binding.address.setText("Address : " + add);


        String msg = "Dear " + c.getDonors().get(c.getDonors().size() - 1).getContact().getName() + ", " + "On behalf of " + c.getContact().getName() + ", we would like to express our sincere gratitude for your generous donation towards our agriculture farm. Your contribution is greatly appreciated and will go a long way in supporting our mission to cultivate healthy and sustainable produce for our community. We have also included your Transaction ID number for your records. Thank you again for your support, and we look forward to continuing our partnership to create a more sustainable and healthy future." + "\n Sincerely,";

        binding.recieptDesc.setText(msg);
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = saveBitMap(ThanksDonationActivity.this, binding.recieptLayout);
                if (file != null) {

                    Log.i("TAG", "Drawing saved to the gallery!");
                    Toast.makeText(ThanksDonationActivity.this, "saved to the gallery!", Toast.LENGTH_SHORT).show();
                } else {

                    Log.i("TAG", "Oops! Image could not be saved.");
                }
            }
        });


    }

    private File saveBitMap(ThanksDonationActivity reciept, LinearLayout linear) {

        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "KnowEco");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated) Log.i("TAG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(linear);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(reciept, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;


    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }
}