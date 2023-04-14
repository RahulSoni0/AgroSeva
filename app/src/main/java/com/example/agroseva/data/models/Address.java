package com.example.agroseva.data.models;

import java.io.Serializable;

public class Address implements Serializable {

    String vill;
    String city;
    String district;
    String state;
    String country;
    String pincode;

    @Override
    public String toString() {
        return "Address{" +
                "vill='" + vill + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pincode='" + pincode + '\'' +
                '}';
    }

    public String getVill() {
        return vill;
    }

    public void setVill(String vill) {
        this.vill = vill;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Address(String vill, String city, String district, String state, String country, String pincode) {
        this.vill = vill;
        this.city = city;
        this.district = district;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }

    public Address() {
    }
}
