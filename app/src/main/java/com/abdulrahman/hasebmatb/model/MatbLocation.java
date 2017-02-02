package com.abdulrahman.hasebmatb.model;

/**
 * Created by Mohamed Yossif on 04/01/2017.
 */
public class MatbLocation {


    private int matbId;
    private double lat , lng ;
    public MatbLocation(){

    }
    public MatbLocation(int matbId , double lat , double lng){
        this.matbId = matbId;
        this.lat = lat;
        this.lng = lng;

    }

    public int getMatbId() {
        return matbId;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
