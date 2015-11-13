package com.example.pinkprincess.meetmetest2;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by pinkprincess on 24.10.15.
 */
public abstract class OwnUser {

    //a class for storing user information, abstract because there will be only one user logged in
    static boolean loggedIn = false;
    static String userName = "hans";
    static String base64String = null;
    static String userColor = "red";
    static LatLng ownLocation;
    static Integer score = 2;
    static String userCode; //String for storing own code locally (recieved from server at login later)
    static ArrayList<OtherUser> nearestUserArray; //ArrayList for storing recieved JSON objects --> other users near user that should be displayed on the map
    static ArrayList<OtherUser> knownUsers;
    static ArrayList bestuserArray;

}
