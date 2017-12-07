package com.won.taskmodule;

/**
 * Created by Oshan Wickramaratne on 2017-12-07.
 */

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Oshan Wickramaratne on 2017-09-06.
 */

        import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Vehicle {

    int vehicleNo ;
    String regNo ;
    String brand ;
    String model ;
    String picture;

    public Vehicle() {

    }

    public Vehicle(int vehicleNo,String regNo, String brand, String model,String picture) {
        this.vehicleNo = vehicleNo;
        this.regNo = regNo;
        this.brand = brand;
        this.model = model;
        this.picture=picture;
    }

    public int getVehicleNo() {
        return vehicleNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPicture() {
        return picture;
    }
}
