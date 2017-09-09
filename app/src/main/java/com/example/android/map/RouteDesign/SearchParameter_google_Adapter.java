package com.example.android.map.RouteDesign;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.map.R;

import java.util.List;

/**
 * Created by Howard on 2017/7/22.
 */

public class SearchParameter_google_Adapter extends ArrayAdapter<SearchParameter_Google>{

    public SearchParameter_google_Adapter(Activity context, List<SearchParameter_Google> directlyList) {
        super(context, 0, directlyList);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.result_of_call_the_bus_google, parent, false);

        }



        SearchParameter_Google currentSearch_google = null;
        currentSearch_google = getItem(position);

        TextView busNumber = listItemView.findViewById(R.id.IDBusNumberGoogle);
        busNumber.setText(currentSearch_google.getgNumber());

        TextView busToGoogle = listItemView.findViewById(R.id.IDinWordGoogle);
        busToGoogle.setText("往");

        TextView busTo = listItemView.findViewById(R.id.IDTo);
        busTo.setText(currentSearch_google.getgTo());

        TextView busStartGoogle = listItemView.findViewById(R.id.IDStartGoogleStop);
        busStartGoogle.setText(currentSearch_google.getgStartBusName());

        TextView busStart = listItemView.findViewById(R.id.IDStartGoogle);
        busStart.setText("起站:");

        TextView busEndGoogleStop = listItemView.findViewById(R.id.IDEndGoogleStop);
        busEndGoogleStop.setText(currentSearch_google.getgEndBusName());

        TextView busEnd = listItemView.findViewById(R.id.IDEndGoogle);
        busEnd.setText("目的:");

        TextView busEstimateTime = listItemView.findViewById(R.id.IDMinuteGoogle);
        busEstimateTime.setText(currentSearch_google.getgEstimateTime());

        TextView busInstructionConstant = listItemView.findViewById(R.id.IDInstruction);
        busInstructionConstant.setText("指示:");

        TextView busInstructionContent = listItemView.findViewById(R.id.IDInstructionContent);
        busInstructionContent.setText(currentSearch_google.getgInstruction());


        return listItemView;
    }
}
