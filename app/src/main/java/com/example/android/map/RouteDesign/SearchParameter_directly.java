package com.example.android.map.RouteDesign;

/**
 * Created by Howard on 2017/7/22.
 */

public class SearchParameter_directly {
    private String mEstimateTime;
    private String mNumber;
    private int mRoutId; //use to find the bus route
    private String mFrom;
    private String mTo;
    private String mEndNameZh;
    private String mSartName;


    public SearchParameter_directly ( String EstimateTime_us, String Number_us, String From_us, String To_us, String EndNameZh_us, String SartName_us){

        mEstimateTime = EstimateTime_us;
        mNumber = Number_us;
        //mRoutId = RoutId_us; //use to find the bus route
        mFrom = From_us;
        mTo = To_us;
        mEndNameZh = EndNameZh_us;
        mSartName = SartName_us;
    }
    String getmEstimateTime (){return mEstimateTime;}
    String getmNumber() {return mNumber;}
    int getmRoutId() {return mRoutId;}
    String getmFrom() {return mFrom;}
    String getmTo() { return mTo;}
    String getmEndNameZh() { return  mEndNameZh;}
    String getmSartName(){ return mSartName;}
}
