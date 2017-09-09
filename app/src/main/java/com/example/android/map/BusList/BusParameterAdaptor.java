package com.example.android.map.BusList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.map.R;

import java.util.ArrayList;

/**
 * Created by Howard on 2017/7/15.
 */

public class BusParameterAdaptor extends ArrayAdapter<BusParameter> {
        public BusParameterAdaptor(Activity context, ArrayList<BusParameter> busList){
            super(context, 0, busList);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            View listItemView = convertView;
            if(listItemView == null){
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }
            BusParameter currentBus = getItem(position);

            //ImageView busImage = (ImageView) listItemView.findViewById(R.id.IDBusImage);
            //busImage.setImageResource(currentBus.getmImageResourceID());

            TextView busNumber = listItemView.findViewById(R.id.IDbusNumber);
            busNumber.setText(currentBus.getmBusNumber());

            TextView busType = listItemView.findViewById(R.id.IDbusType);
            busType.setText(currentBus.getmBusType());

            TextView busStart = listItemView.findViewById(R.id.IDfrom);
            busStart.setText(currentBus.getmStartStop());

            TextView busEnd = listItemView.findViewById(R.id.IDto);
            busEnd.setText(currentBus.getmEndStop());

            TextView timeLeft = listItemView.findViewById(R.id.IDminute);
            timeLeft.setText(currentBus.getmEstimateTimeOfTheUpComingBus());

            TextView inWord = listItemView.findViewById((R.id.IDinWord));
            inWord.setText("往");
            return listItemView;
        }

}
