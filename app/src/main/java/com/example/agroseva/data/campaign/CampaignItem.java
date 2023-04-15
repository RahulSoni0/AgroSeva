package com.example.agroseva.data.campaign;

import java.io.Serializable;
import java.util.List;

public class CampaignItem implements Serializable {

    String campaign_title;
    String campaign_image_url;
    String campaign_description;
    String product_description;
    String amount;
    String adhar_no;
    String adhar_url;
    Contact contact;
    String doc_id;

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    List<DonationItems> donors;
    List<PaymentOptions> paymentOptions;


    public List<PaymentOptions> getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(List<PaymentOptions> paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public List<DonationItems> getDonors() {
        return donors;
    }

    public void setDonors(List<DonationItems> donors) {
        this.donors = donors;
    }

    public String getCampaign_image_url() {
        return campaign_image_url;
    }

    public void setCampaign_image_url(String campaign_image_url) {
        this.campaign_image_url = campaign_image_url;
    }

    public String getCampaign_title() {

        return campaign_title;
    }

    public void setCampaign_title(String campaign_title) {
        this.campaign_title = campaign_title;
    }

    public String getCampaign_description() {
        return campaign_description;
    }

    public void setCampaign_description(String campaign_description) {
        this.campaign_description = campaign_description;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAdhar_no() {
        return adhar_no;
    }

    public void setAdhar_no(String adhar_no) {
        this.adhar_no = adhar_no;
    }

    public String getAdhar_url() {
        return adhar_url;
    }

    public void setAdhar_url(String adhar_url) {
        this.adhar_url = adhar_url;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }


    public CampaignItem() {
    }


    @Override
    public String toString() {
        return "CampaignItem{" +
                "campaign_title='" + campaign_title + '\'' +
                ", campaign_image_url='" + campaign_image_url + '\'' +
                ", campaign_description='" + campaign_description + '\'' +
                ", product_description='" + product_description + '\'' +
                ", amount='" + amount + '\'' +
                ", adhar_no='" + adhar_no + '\'' +
                ", adhar_url='" + adhar_url + '\'' +
                ", contact=" + contact +
                ", doc_id='" + doc_id + '\'' +
                ", donors=" + donors +
                ", paymentOptions=" + paymentOptions +
                '}';
    }
}
