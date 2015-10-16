package com.example.pinkprincess.meetmetest2;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pinkprincess on 16.10.15.
 */
public class HttpRequestSender extends AsyncTask<String, Integer, ArrayList<OtherUser>> {

    String IpAdresse = "172.20.10.5";
    public String UserName;
    public Double UserLat;
    public Double UserLong;
    private InputStream httpResponse;
    private Context mapsActivity;
    ArrayList<OtherUser> userArray;

    String getOtherUsersURLString = IpAdresse + ":8087/meetmeserver/api/gps/";

    /*public InputStream getOtherUsers(String username, Double lat, Double lng) {
        this.UserLat = lat;
        this.UserLong = lng;
        this.UserName = username;
    }*/

    public HttpRequestSender(Context activity, String username, Double lat, Double lng){
        this.mapsActivity = activity;
        this.UserName = username;
        this.UserLat = lat;
        this.UserLong = lng;
    }

    protected void onPostExecute(ArrayList<OtherUser> userArray) {
        HttpRequestInterface activity = (HttpRequestInterface) mapsActivity;
        activity.displayOtherUser(userArray);
    }

    @Override
    protected ArrayList<OtherUser> doInBackground(String... params) {
        URL getOtherUsersURL = null;
        InputStream response = null;
        try {
            getOtherUsersURL = new URL("http://" + getOtherUsersURLString + this.UserName + "/" + this.UserLat + "/" + this.UserLong);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (getOtherUsersURL != null){
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) getOtherUsersURL.openConnection();
                // response = new BufferedInputStream(urlConnection.getInputStream());
                response = urlConnection.getInputStream();
                if (response != null) {
                UserImportierer mUserImportierer = new UserImportierer(); //create new JSON Parser Object
                try {
                    userArray = mUserImportierer.readJsonStream(response); //store JSON Objects from JSON Array as OtherUser Objects in userArray
                } catch (IOException e) {
                    e.printStackTrace();
                }}
                this.httpResponse = response;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
                urlConnection.disconnect();
            }
            finally {
                urlConnection.disconnect();
            }


        }
        return userArray;
    }


    /*@Override
    protected String doInBackground(String... params) {

        int lenght = params.length;
        if  (params.length == 2) {

        }

        URL getOtherUsersURL = null;
        InputStream response = null;
        try {
            getOtherUsersURL = new URL("http://" + getOtherUsersURLString + this.UserName + "/" + this.UserLat + "/" + this.UserLong);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (getOtherUsersURL != null){
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) getOtherUsersURL.openConnection();
                // response = new BufferedInputStream(urlConnection.getInputStream());
                this.httpResponse = urlConnection.getInputStream();
                response = this.httpResponse;
            } catch (IOException e) {
                e.printStackTrace();
                urlConnection.disconnect();
            }
            finally {
                urlConnection.disconnect();
            }


    }
        return response.toString();
}

    @Override
    protected Double doInBackground(String... params) {
        return null;
    }*/
}
