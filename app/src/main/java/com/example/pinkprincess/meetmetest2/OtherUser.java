package com.example.pinkprincess.meetmetest2;

import com.google.android.gms.maps.model.LatLng;

public class OtherUser {
    String name;
    String color;
    LatLng loc;


    public OtherUser(String name, Double lat, Double lng, String color) {
        this.name = name;
        LatLng currentLoc = new LatLng(lat, lng);
        this.loc = currentLoc;
        this.color = color;
    }

    public LatLng getLocation() {
        return loc;
    }


}
