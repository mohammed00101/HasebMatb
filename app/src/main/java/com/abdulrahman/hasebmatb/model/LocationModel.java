package com.abdulrahman.hasebmatb.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/***
 *   Created by abdulrahman on 12/26/16.
 ***/

public class LocationModel  implements Comparable<LocationModel>{
    public String description;
    public LatLng latLng;
    public LocationModel(String description, LatLng latLng) {
        this.description = description;
        this.latLng = latLng;
    }
     public  LocationModel(){

     }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public int compareTo(LocationModel locationModel) {
        return (this.description.compareTo(locationModel.description));
    }
}
