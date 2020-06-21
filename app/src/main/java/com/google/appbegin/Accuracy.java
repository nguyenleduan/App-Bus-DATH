package com.google.appbegin;

public class Accuracy {

    public String  CostAccuracy;
    public String  IDUserSendAccuracy;
    public String  LatBeginAccuracy;
    public String  LatEndAccuracy;
    public String  LocationBeginAccuracy;
    public String  LocationEndAccuracy;
    public String  LongBeginAccuracy;
    public String  LongEndAccuracy;
    public String  StatusAccuracy;

    public Accuracy() {
    }

    public Accuracy(String costAccuracy, String IDUserSendAccuracy, String latBeginAccuracy, String latEndAccuracy, String locationBeginAccuracy, String locationEndAccuracy, String longBeginAccuracy, String longEndAccuracy, String statusAccuracy) {
        CostAccuracy = costAccuracy;
        this.IDUserSendAccuracy = IDUserSendAccuracy;
        LatBeginAccuracy = latBeginAccuracy;
        LatEndAccuracy = latEndAccuracy;
        LocationBeginAccuracy = locationBeginAccuracy;
        LocationEndAccuracy = locationEndAccuracy;
        LongBeginAccuracy = longBeginAccuracy;
        LongEndAccuracy = longEndAccuracy;
        StatusAccuracy = statusAccuracy;
    }
}
