package com.example.agroseva.api.network;


import com.example.agroseva.api.pincodeRes.PincodeModelRes;
import com.example.agroseva.api.pincodeRes.PincodeModelResItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PincodeInterface {


    // india
    @GET("pincode/{Pincode}")
    Call<List<PincodeModelResItem>> getPincodeDetails(
            @Path("Pincode") String Pincode

    );


}
