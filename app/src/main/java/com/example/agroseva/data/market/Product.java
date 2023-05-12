package com.example.agroseva.data.market;

import com.example.agroseva.data.campaign.Contact;

import java.io.Serializable;

public class Product implements Serializable {
    String prod_id;
    String uid;
    String product_name;
    String product_desc;
    String product_price;
    String product_Imageurl;
    String product_availability;
    String whatsappno;
    Contact contact;
    String cat;

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_Imageurl() {
        return product_Imageurl;
    }

    public void setProduct_Imageurl(String product_Imageurl) {
        this.product_Imageurl = product_Imageurl;
    }

    public String getProduct_availability() {
        return product_availability;
    }

    public void setProduct_availability(String product_availability) {
        this.product_availability = product_availability;
    }

    public String getWhatsappno() {
        return whatsappno;
    }

    public void setWhatsappno(String whatsappno) {
        this.whatsappno = whatsappno;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Product{" +
                "prod_id='" + prod_id + '\'' +
                ", uid='" + uid + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_desc='" + product_desc + '\'' +
                ", product_price='" + product_price + '\'' +
                ", product_Imageurl='" + product_Imageurl + '\'' +
                ", product_availability='" + product_availability + '\'' +
                ", whatsappno='" + whatsappno + '\'' +
                ", contact=" + contact +
                ", cat='" + cat + '\'' +
                '}';
    }

    public Product() {
    }
}
