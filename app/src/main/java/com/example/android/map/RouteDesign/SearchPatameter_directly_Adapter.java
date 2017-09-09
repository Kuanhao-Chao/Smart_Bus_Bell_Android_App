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

public class SearchPatameter_directly_Adapter extends ArrayAdapter<SearchParameter_directly> {
    public SearchPatameter_directly_Adapter(Activity context, List<SearchParameter_directly> directlyList) {
        super(context, 0, directlyList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.result_of_call_the_bus_directly, parent, false);
        }
        SearchParameter_directly currentSearch_directly= getItem(position);

        TextView busNumber = listItemView.findViewById(R.id.IDBusNumberDirectly);
        busNumber.setText(currentSearch_directly.getmNumber());

        TextView busInWard = listItemView.findViewById(R.id.IDinWordDirectly);
        busInWard.setText("往");

        TextView busToDirectly = listItemView.findViewById(R.id.IDtoDirectly);
        busToDirectly.setText(currentSearch_directly.getmTo());


        TextView busStart = listItemView.findViewById(R.id.IDStartDirectly);
        busStart.setText("起站:");

        TextView busStartDirectlyStop = listItemView.findViewById(R.id.IDStartDirectlyStop);
        busStartDirectlyStop.setText(currentSearch_directly.getmSartName());

        TextView busEnd = listItemView.findViewById(R.id.IDEndDirectly);
        busEnd.setText("目的:");

        TextView busEndDirectlyStop = listItemView.findViewById(R.id.IDEndDirectlyStop);
        busEndDirectlyStop.setText(currentSearch_directly.getmEndNameZh());

        TextView busEstimateTime = listItemView.findViewById(R.id.IDMinuteDirectly);
        busEstimateTime.setText(currentSearch_directly.getmEstimateTime());
        return listItemView;
    }
}



