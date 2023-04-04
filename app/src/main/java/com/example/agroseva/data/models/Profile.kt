package com.example.agroseva.data.models

import com.example.agroseva.data.campaign.Campaign

data class Profile (

    var name:String,
    var age:String,
    var gender:String,
    var email:String,
    var phone_no:String,
    var profileImageUrl:String,
    var address: Address,
    var campaign : Campaign



        )
