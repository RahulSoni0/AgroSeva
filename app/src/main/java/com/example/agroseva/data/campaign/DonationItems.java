package com.example.agroseva.data.campaign;

import java.io.Serializable;

public class DonationItems implements Serializable {

    Contact contact;
    String amount;
    String reciept_url;
    String message;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReciept_url() {
        return reciept_url;
    }

    public void setReciept_url(String reciept_url) {
        this.reciept_url = reciept_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DonationItems() {
    }
}
