package com.example.agroseva.api.pincodeRes;

import java.util.List;

public class PincodeModelResItem {
    String Message;
    List<PostOffice> PostOffice;
    String Status;

    public String getMessage() {
        return Message;
    }

    @Override
    public String toString() {
        return "PincodeModelResItem{" +
                "Message='" + Message + '\'' +
                ", PostOffice=" + PostOffice +
                ", Status='" + Status + '\'' +
                '}';
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<com.example.agroseva.api.pincodeRes.PostOffice> getPostOffice() {
        return PostOffice;
    }

    public void setPostOffice(List<com.example.agroseva.api.pincodeRes.PostOffice> postOffice) {
        PostOffice = postOffice;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
