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

    private static String IpAdresse = "192.168.2.3";
    private static String port = "8087";
    private static URL requestUrl;
    //private Context callbackActivity;


    private static String getOtherUserLink = "meetmeserver/api/gps";
    private static String getUserMeetingLink = "meetmeserver/api/interact";
    private static String getTeamRanking = "meetmeserver/api/ranking/teamleaderboard";
    private static String getTopUserRankingLink = "meetmeserver/api/ranking/topplayer";
    private static String getFriendsLink = "meetmeserver/api/ranking/friendlist";

    private String requestType;
    private Context mapsActivity;
    ArrayList<OtherUser> userArray;


    @Override
    public void doGetOtherUsers(Context context) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;
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
                                OwnUser.nearestUserArray = mResponseImportierer.<OtherUser>readJsonStream(response); //store JSON Objects from JSON Array as OtherUser Objects in userArray
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
                activity.displayOtherUser(userArray);
            }

        }.execute();
    }

    @Override
    public void doGetUserMeeting(Context context, String otherUserName, String verificationCode) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;
        final String otherUser = otherUserName;
        final String code = verificationCode;
        new AsyncTask<String, Void, String[]>(){

            @Override
            protected String[] doInBackground(String... params) {
                Boolean validationResponse = false;
                String[] responseString = null;
                String otherUserColor = null;
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
                            String[] responseElements = new String[4];
                            int i = 0;
                            StringTokenizer seperator = new StringTokenizer(total.toString(),";");
                            while(seperator.hasMoreElements()) {
                                responseElements[i] = seperator.nextElement().toString();
                                i++;
                            }
                            responseString = responseElements;
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


                return responseString;
            }

            protected void onPostExecute(String[] responseString){
                Boolean validationResponse = Boolean.parseBoolean(responseString[0]);
                //otherUserColor = responseString[2];
                OwnUser.score = Integer.parseInt(responseString[1]);
                for (int i = 0; i<OwnUser.nearestUserArray.size(); i++) {
                    if(OwnUser.nearestUserArray.get(i).name.equals(otherUser)){
                        OwnUser.nearestUserArray.get(i).color = responseString[2];
                    }
                }
                activity.userMeetingValidation(otherUser, validationResponse);
            }
            }.execute();
        }



    @Override
    public void doGetUserRanking(Context context) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;

        new AsyncTask<String, String, String[][]>() {
            @Override
            protected String[][] doInBackground(String... params) {
                String[][] responseStringArray = null; //= new String[2][2]; //currently only team german and not-german plus team scores
                ArrayList<String[]> responseArrayList = new ArrayList<String[]>();
                InputStream response = null;
                try {
                    requestUrl = new URL("http://"
                            + IpAdresse
                            + ":"
                            + port
                            + "/"
                            + getTopUserRankingLink);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (requestUrl != null){
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) requestUrl.openConnection();
                        response = urlConnection.getInputStream();
                        if (response != null) {
                            ResponseImportierer mResponseImportierer = new ResponseImportierer();
                            responseArrayList = mResponseImportierer.<String[]>readJsonStream(response);
                            responseStringArray = new String[responseArrayList.size()][3];
                            for (int i = 0; i < responseArrayList.size(); i++) {
                                String[] current = responseArrayList.get(i);
                                responseStringArray[i][0] = current[0]; //name
                                responseStringArray[i][1] = current[1]; //nation
                                responseStringArray[i][2] = current[2]; //score
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                return responseStringArray;
            }

            protected void onPostExecute(String[][] responseStringArray) {
                activity.displayBestUserRanking(responseStringArray);
            }
        }.execute();


    }

    @Override
    public void doGetTeamRanking(Context context) {

        final HttpResponseInterface activity = (HttpResponseInterface) context;
        //final String[][] responseStringArray = new String[2][2]; //currently only team german and not-german plus team scores
        new AsyncTask<String, String, String[][]>(){

            @Override
            protected String[][] doInBackground(String... params) {
                InputStream response = null;
                ArrayList<String[]> arrayListResponse = null;
                String[][] responseStringArray = null;
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
                                 arrayListResponse = mResponseImportierer.<String[]>readJsonStream(response); //store JSON Objects from JSON Array as OtherUser Objects in userArray
                                responseStringArray = new String[arrayListResponse.size()][2];
                                for (int i = 0; i < arrayListResponse.size(); i++) {
                                    String[] current = arrayListResponse.get(i);
                                    responseStringArray[i][0] = current[0]; //nation
                                    responseStringArray[i][1] = current[1]; //score
                                }
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
                return responseStringArray;
            }

            protected void onPostExecute(String[][] teamStatistics){
                activity.displayTeamRanking(teamStatistics);
            }
        }.execute();

    }

    @Override
    public void doGetFriends(Context context) {
        Context con = context;
        final HttpResponseInterface activity = (HttpResponseInterface) context;
        //String[][] responseStringArray; //= new String[2][2]; //currently only team german and not-german plus team scores

        new AsyncTask<String, String, String[][]>() {
            ArrayList<String[]> responseArrayList = new ArrayList<String[]>();
            String[][] responseStringArray = null;

            @Override
            protected String[][] doInBackground(String... params) {
                InputStream response = null;
                {
                    try {
                        requestUrl = new URL("http://"
                                + IpAdresse
                                + ":"
                                + port
                                + "/"
                                + getFriendsLink
                                + "/"
                                + OwnUser.userName
                        );
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
                                    responseArrayList = mResponseImportierer.<String[]>readJsonStream(response);
                                    responseStringArray = new String[responseArrayList.size()][2];
                                    for (int i = 0; i < responseArrayList.size(); i++) {
                                        String[] current = responseArrayList.get(i);
                                        responseStringArray[i][0] = current[0]; //name
                                        responseStringArray[i][1] = current[1]; //nation
                                    }
                                    }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            response.close();
                            }

                         } catch (IOException e) {
                            urlConnection.disconnect();
                        }
                        finally {
                            urlConnection.disconnect();
                        }

                    }

                }
                return responseStringArray;
            }

            protected void onPostExecute(String[][] responseStringArray) {
                        activity.displayBestUserRanking(responseStringArray);
            }
        }.execute();
    }


}
