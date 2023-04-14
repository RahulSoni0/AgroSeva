package com.example.agroseva.data.campaign;

import java.io.Serializable;

public class PaymentOptions implements Serializable {
    String name;
    String bank_name;
    String account_no;
    String ifsc;
    String upi;
    String upi_qr_url;

    public PaymentOptions() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }

    public String getUpi_qr_url() {
        return upi_qr_url;
    }

    public void setUpi_qr_url(String upi_qr_url) {
        this.upi_qr_url = upi_qr_url;
    }

    @Override
    public String toString() {
        return "PaymentOptions{" +
                "name='" + name + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", account_no='" + account_no + '\'' +
                ", ifsc='" + ifsc + '\'' +
                ", upi='" + upi + '\'' +
                ", upi_qr_url='" + upi_qr_url + '\'' +
                '}';
    }
}
