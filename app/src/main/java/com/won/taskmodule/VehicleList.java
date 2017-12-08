package com.won.taskmodule;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Oshan Wickramaratne on 2017-09-07.
 */

public class VehicleList extends ArrayAdapter<Vehicle> {
    List<Vehicle> vehicles;
    private Activity context;

    public VehicleList(Activity context, List<Vehicle> vehicles) {
        super(context, R.layout.layout_vehicle_list, vehicles);
        this.context = context;
        this.vehicles = vehicles;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_vehicle_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewVehicleRegNo);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewVehicleBrandModel);

        Vehicle vehicle = vehicles.get(position);
        textViewName.setText(vehicle.getRegNo());
        textViewGenre.setText(vehicle.getBrand()+" "+vehicle.getModel());

        return listViewItem;
    }
}
