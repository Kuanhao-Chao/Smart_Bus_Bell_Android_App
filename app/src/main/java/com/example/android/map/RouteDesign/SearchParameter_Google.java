package com.example.android.map.RouteDesign;

/**
 * Created by Howard on 2017/7/22.
 */

public class SearchParameter_Google {

    private String gEstimateTime;
    private String gNumber;
    private String gFrom;
    private String gTo;
    private String gTransfer_Time;
    private double gStartLatitude;
    private double gStartLongitude;
    private String gStartBusName;
    private String gEndBusName;
    private String gInstruction;
    private String gBusStopLocationId;
//    private String gRouteId;

    public SearchParameter_Google(String EstimateTime_g, String Number_g, String From_g,
                                  String To_g, String Transfer_Time_g, String StartBusName_g,
                                  String EndBusName_g , String Instruction, String BusStopLocationId){
        gEstimateTime = EstimateTime_g;
        gNumber = Number_g;
        //gRoutId = RoutId_g; //use to find the bus route
        gFrom = From_g;
        gTo = To_g;
        gTransfer_Time = Transfer_Time_g;
//        gStartLatitude = StartLatitude_g;
//        gStartLongitude = StartLongitude_g;
        gStartBusName = StartBusName_g;
//        gEndLatitude = EndLatitude_g;
//        gEndLongitude = EndLongitude_g;
        gEndBusName = EndBusName_g;
        gInstruction = Instruction;
//        gRouteId = RoutId;
        gBusStopLocationId = BusStopLocationId;
    }

    String getgEstimateTime() {return gEstimateTime;}
    String getgNumber (){ return  gNumber;}
//    int getgRoutId() {return  gRoutId;}
    String getgFrom() {return  gFrom;}
    String getgTo() { return  gTo;}
    String getgTransfer_Time() {return  gTransfer_Time;}
    double getgStartLatitude() {return  gStartLatitude;}
    double getgStartLongitude() {return gStartLongitude;}
    String getgStartBusName() {return gStartBusName;}
//    double getgEndLatitude() {return gEndLatitude;}
//    double getgEndLongitude() {return  gEndLongitude;}
    String getgEndBusName() {return  gEndBusName;}
    String getgInstruction() {return  gInstruction;}
//    String getgRouteId() { return  gRouteId; }
    String getgBusStopLocationId () { return gBusStopLocationId; }
}
