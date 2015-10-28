package com.example.pinkprincess.meetmetest2;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by pinkprincess on 27.10.15.
 */
public class OfflineTester implements HttpRequestInterface {
    @Override
    public void doGetOtherUsers(Context context) {

        HttpResponseInterface activity = (HttpResponseInterface) context;
        InputStream stream = null;
        try {
            stream = MapsActivity.assetManager.open("otherusers.json"); //open JSON document (later: HTTP response, not asset)

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stream != null) {
            ResponseImportierer mResponseImportierer = new ResponseImportierer(); //create new JSON Parser Object
            try {
                OwnUser.nearestUserArray = mResponseImportierer.<OtherUser>readJsonStream(stream);
                activity.displayOtherUser(OwnUser.nearestUserArray);}
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {return;}
    }

    @Override
    public void doGetUserMeeting(Context context, String otherUserName, String verificationCode) {

    }

    @Override
    public void doGetTeamRanking(Context context) {
        final HttpResponseInterface activity = (HttpResponseInterface) context;
        String[][] responseStringArray; //= new String[2][2]; //currently only team german and not-german plus team scores
        ArrayList<String[]> responseArrayList = new ArrayList<String[]>();
        InputStream stream = null;
        try {
            stream = MapsActivity.assetManager.open("teamranking.json"); //open JSON document (later: HTTP response, not asset)

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stream != null) {
            ResponseImportierer mResponseImportierer = new ResponseImportierer(); //create new JSON Parser Object
            try {
                responseArrayList = mResponseImportierer.<String[]>readJsonStream(stream);
                responseStringArray = new String[responseArrayList.size()][2];
                responseStringArray[0][0] = "a";
                for (int i = 0; i < responseArrayList.size(); i++) {
                    String[] current = responseArrayList.get(i);
                    responseStringArray[i][0] = current[0]; //nation
                    responseStringArray[i][1] = current[1]; //score
                }
                activity.displayTeamRanking(responseStringArray);

            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {return;}

    }

    @Override
    public void doGetUserRanking(Context context) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;
        String[][] responseStringArray; //= new String[2][2]; //currently only team german and not-german plus team scores
        ArrayList<String[]> responseArrayList = new ArrayList<String[]>();
        InputStream stream = null;
        try {
            stream = MapsActivity.assetManager.open("topusers.json"); //open JSON document (later: HTTP response, not asset)

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stream != null) {
            ResponseImportierer mResponseImportierer = new ResponseImportierer(); //create new JSON Parser Object
            try {
                responseArrayList = mResponseImportierer.<String[]>readJsonStream(stream);
                responseStringArray = new String[responseArrayList.size()][3];
                for (int i = 0; i < responseArrayList.size(); i++) {
                    String[] current = responseArrayList.get(i);
                    responseStringArray[i][0] = current[0]; //name
                    responseStringArray[i][1] = current[1]; //nation
                    responseStringArray[i][2] = current[2];
                }
                activity.displayBestUserRanking(responseStringArray);

            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {return;}
    }

    @Override
    public void doGetFriends(Context context) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;
        String[][] responseStringArray; //= new String[2][2]; //currently only team german and not-german plus team scores
        ArrayList<String[]> responseArrayList = new ArrayList<String[]>();
        InputStream stream = null;
        try {
            stream = MapsActivity.assetManager.open("topusers.json"); //open JSON document (later: HTTP response, not asset)

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stream != null) {
            ResponseImportierer mResponseImportierer = new ResponseImportierer(); //create new JSON Parser Object
            try {
                responseArrayList = mResponseImportierer.<String[]>readJsonStream(stream);
                responseStringArray = new String[responseArrayList.size()][3];
                for (int i = 0; i < responseArrayList.size(); i++) {
                    String[] current = responseArrayList.get(i);
                    responseStringArray[i][0] = current[0]; //name
                    responseStringArray[i][1] = current[1]; //nation or score
                }
                activity.displayFriends(responseStringArray);

            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {return;}
    }


}
