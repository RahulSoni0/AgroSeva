package com.example.agroseva.data.campaign;

import java.io.Serializable;
import java.util.List;

public class Campaign implements Serializable {
    List<CampaignItem> campaignsList;

    public Campaign() {
    }


    public List<CampaignItem> getCampaignsList() {
        return campaignsList;
    }

    public void setCampaignsList(List<CampaignItem> campaignsList) {
        this.campaignsList = campaignsList;
    }


    @Override
    public String toString() {
        return "Campaign{" +
                "campaignsList=" + campaignsList +
                '}';
    }

}
