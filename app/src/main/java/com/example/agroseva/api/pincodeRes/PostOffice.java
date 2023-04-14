package com.example.agroseva.api.pincodeRes;

public class PostOffice {

    String Block;
    String BranchType;
    String Circle;
    String Country;
    String DeliveryStatus;
    String Description;
    String District;
    String Division;
    String Name;
    String Pincode;
    String Region;
    String State;

    @Override
    public String toString() {
        return "PostOffice{" +
                "Block='" + Block + '\'' +
                ", BranchType='" + BranchType + '\'' +
                ", Circle='" + Circle + '\'' +
                ", Country='" + Country + '\'' +
                ", DeliveryStatus='" + DeliveryStatus + '\'' +
                ", Description='" + Description + '\'' +
                ", District='" + District + '\'' +
                ", Division='" + Division + '\'' +
                ", Name='" + Name + '\'' +
                ", Pincode='" + Pincode + '\'' +
                ", Region='" + Region + '\'' +
                ", State='" + State + '\'' +
                '}';
    }

    public String getBlock() {
        return Block;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public String getBranchType() {
        return BranchType;
    }

    public void setBranchType(String branchType) {
        BranchType = branchType;
    }

    public String getCircle() {
        return Circle;
    }

    public void setCircle(String circle) {
        Circle = circle;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
