package com.example.pinkprincess.meetmetest2;

import android.os.NetworkOnMainThreadException;
import android.util.JsonReader;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by pinkprincess on 11.10.15.
 */
public class ResponseImportierer {
    public <F> ArrayList<F> readJsonStream(InputStream in) throws IOException, NetworkOnMainThreadException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return this.<F>readJsonObject(reader);
        } finally {
            reader.close();
        }

    }

    public <R> ArrayList<R> readJsonObject(JsonReader reader) throws IOException, NetworkOnMainThreadException {
        ArrayList<R> objectArray = new ArrayList<R>();
        reader.beginObject();
        while (reader.hasNext()) {
            String test = reader.nextName();
            if (test.equals("userPosition")) {
                objectArray = this.<R>readUsersArray(reader);
            }
            if (test.equals("topTeamList")) {
                objectArray = readTeamRankings(reader);
            }
        }

        return objectArray;
    }


    public <R> ArrayList<R> readUsersArray(JsonReader reader) throws IOException, NetworkOnMainThreadException {
        ArrayList<R> usersArray = new ArrayList();
        reader.beginArray();
        try {
            while (reader.hasNext()) {
                String name = null;
                Double lat = null;
                Double lng = null;
                String color = null;

                reader.beginObject();
                while (reader.hasNext()) {
                    String test = reader.nextName();
                    if (test.equals("name")) {
                        name = reader.nextString();
                    } else if (test.equals("lat")) {
                        lat = reader.nextDouble();
                    } else if (test.equals("lon")) {
                        lng = reader.nextDouble();
                    } else if (test.equals("color")) {
                        color = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
                LatLng loc = new LatLng(lat, lng);
                usersArray.add((R)new OtherUser(name, lat, lng, color));
            }
        }
        //    }}
        //catch (IOException e) {System.out.println(e);}
        catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } finally {
            reader.endArray();
            return usersArray;
        }
    }

    public <R> ArrayList<R> readTeamRankings(JsonReader reader) throws IOException{
        ArrayList<R> teamArrayList = new ArrayList<R>();
        reader.beginArray();
        try {
            while (reader.hasNext()) {
                String team = null;
                String name = null;
                Integer score = null;

                reader.beginObject();
                while (reader.hasNext()) {
                    String test = reader.nextName();
                    if (test.equals("nation")) {
                        team = reader.nextString();}
                    else if (test.equals("score")) {
                        score = reader.nextInt();}
                    else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
                teamArrayList.add((R)(new String[]{team, score.toString()}));
            }
        }
        //    }}
        //catch (IOException e) {System.out.println(e);}
        catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } finally {
            reader.endArray();
            return teamArrayList;
        }
    }
}
