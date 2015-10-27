package com.example.pinkprincess.meetmetest2;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by pinkprincess on 25.10.15.
 */
public class HttpRequestSender implements HttpRequestInterface {

    private static String IpAdresse = "172.20.10.6";
    private static String port = "8087";
    private static URL requestUrl;
    //private Context callbackActivity;


    private static String getOtherUserLink = "meetmeserver/api/gps";
    private static String getUserMeetingLink = "meetmeserver/api/interact";
    private static String getTeamRanking = "meetmeserver/api/reporting/getTeamStatistics";

    private String requestType;
    private Context mapsActivity;
    ArrayList<OtherUser> userArray;


    @Override
    public void doGetOtherUsers(Context context) {

        HttpResponseInterface activity = (HttpResponseInterface) context;
        new AsyncTask<String, String, ArrayList<OtherUser>>() {

            @Override
            protected ArrayList<OtherUser> doInBackground(String... params) {
                InputStream response = null;
                try {
                    requestUrl = new URL("http://"
                            + IpAdresse
                            + ":"
                            + port
                            + "/"
                            + getOtherUserLink
                            + "/"
                            + OwnUser.userName
                            + "/"
                            + OwnUser.ownLocation.latitude
                            + "/"
                            + OwnUser.ownLocation.longitude);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (requestUrl != null){
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) requestUrl.openConnection();
                        response = urlConnection.getInputStream();
                        if (response != null) {
                            ResponseImportierer mResponseImportierer = new ResponseImportierer(); //create new JSON Parser Object
                            try {
                                OwnUser.nearestUserArray = mResponseImportierer.readJsonStream(response); //store JSON Objects from JSON Array as OtherUser Objects in userArray
                            } catch (IOException e) {
                                e.printStackTrace();
                            }}
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        urlConnection.disconnect();
                    }
                    finally {
                        urlConnection.disconnect();
                    }


                }
                return OwnUser.nearestUserArray;
            }

            protected void onPostExecute(ArrayList<OtherUser> userArray) {
                HttpResponseInterface activity = (HttpResponseInterface) mapsActivity;
                activity.displayOtherUser(userArray);
            }

        }.execute();
    }

    @Override
    public void doGetUserMeeting(final Context context, final String otherUserName, final String verificationCode) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;
        final String otherUser = otherUserName;
        final String code = verificationCode;
        new AsyncTask<String, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(String... params) {
                Boolean validationResponse = false;
                InputStream response = null;
                try {
                    requestUrl = new URL("http://"
                            + IpAdresse
                            + ":"
                            + port
                            + "/"
                            + getUserMeetingLink
                            + "/"
                            + OwnUser.userName
                            + "/"
                            + otherUser
                            + "/"
                            + code);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (requestUrl != null){
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) requestUrl.openConnection();
                        response = urlConnection.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(response));
                        StringBuilder total = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            total.append(line);
                        }
                        if (total != null) {
                            String[] responseElements = new String[2];
                            int i = 0;
                            StringTokenizer seperator = new StringTokenizer(total.toString(),";");
                            while(seperator.hasMoreElements()) {
                                responseElements[i] = seperator.nextElement().toString();
                                i++;
                            }
                            validationResponse = Boolean.parseBoolean(responseElements[0]);
                            OwnUser.score = Integer.parseInt(responseElements[1]);
                        }
                        else {System.out.println("response is null!");}


                    } catch (IOException e) {
                        e.printStackTrace();}
                    finally {urlConnection.disconnect();}
                }
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return validationResponse;
            }

            protected void onPostExecute(Boolean validationResponse){
                activity.userMeetingValidation(otherUser, validationResponse);
            }
            }.execute();
        }



    @Override
    public void doGetUserRanking(Context context) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;
        new AsyncTask<String, String, ArrayList>(){

            @Override
            protected ArrayList doInBackground(String... params) {
                InputStream response = null;
                try {
                    requestUrl = new URL("http://"
                            + IpAdresse
                            + ":"
                            + port
                            + "/"
                            + getTeamRanking);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (requestUrl != null){
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) requestUrl.openConnection();
                        response = urlConnection.getInputStream();
                        if (response != null) {
                            ResponseImportierer mResponseImportierer = new ResponseImportierer(); //create new JSON Parser Object
                            try {
                                OwnUser.bestuserArray = mResponseImportierer.readJsonStream(response); //store JSON Objects from JSON Array as OtherUser Objects in userArray
                            } catch (IOException e) {
                                e.printStackTrace();
                            }}
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        urlConnection.disconnect();
                    }
                    finally {
                        urlConnection.disconnect();
                    }


                }
                return OwnUser.bestuserArray;
            }

            protected void onPostExecute(ArrayList userRanking){
                activity.displayBestUserRanking(userRanking);
            }
        }.execute();

    }

    @Override
    public void doGetTeamRanking(Context context) {

        String[][] response = new String[3][2];
        response[0][0] = "123";
        response[0][1] = "Deutschland";
        response[1][0] = "12";
        response[1][1] = "USA";
        response[2][0] = "1";
        response[2][1] = "Frankreich";
        HttpResponseInterface activity = (HttpResponseInterface) context;
        activity.displayTeamRanking(response);
    }

    @Override
    public void doGetFriends(Context context) {

    }


}
