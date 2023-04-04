package com.example.agroseva.data.campaign

data class Campaigns(

    var campaign_title: String,
    var campaign_description: String,
    var product_description: String,
    var amount: String,
    var adhar_no: String,
    var adhar_url: String,
    var contact: Contact,
    var donations: List<Donation>

)
