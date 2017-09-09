package com.example.android.map.BusList;


/**
 * Created by Howard on 2017/7/15.
 */

public class BusParameter {
    //private int mImageResourceID;

    private String mStopLocationId;
    private String mEstimateTimeOfTheUpComingBus;
    private String mBusType;
    private String mStartStop;
    private String mEndStop;
    private String mBusNumber;

    public BusParameter(/*int ImageResourceID,*/String StopLocationId, String EstimateTimeOfTheUpComingBus, String BusType,  String StartStop, String EndStop, String BusNumber){
        //mImageResourceID = ImageResourceID;

        mStopLocationId = StopLocationId;
        mEstimateTimeOfTheUpComingBus = EstimateTimeOfTheUpComingBus;
        mBusType = BusType;
        mStartStop = StartStop;
        mEndStop = EndStop;
        mBusNumber = BusNumber;
    }

    /*public int  getmImageResourceID(){
        return mImageResourceID;
    }*/

    public String getmStopLocationId() {return mStopLocationId;}
    public String getmBusType() { return mBusType;}
    public String getmEstimateTimeOfTheUpComingBus() {return mEstimateTimeOfTheUpComingBus;}
    public String getmStartStop() { return mStartStop;}
    public String getmEndStop() {return  mEndStop;}
    public String getmBusNumber() { return  mBusNumber;}
}
