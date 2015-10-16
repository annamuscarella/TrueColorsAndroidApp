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
public class UserImportierer {

    public ArrayList<LatLng> locations = new ArrayList();
    public ArrayList<String> names = new ArrayList();
    public ArrayList<String> colors = new ArrayList();


public ArrayList<OtherUser> readJsonStream(InputStream in) throws IOException, NetworkOnMainThreadException {
    JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
    try {
        return readJsonObject(reader);}
    finally {reader.close();}

}

    public ArrayList<OtherUser> readJsonObject(JsonReader reader)throws IOException, NetworkOnMainThreadException {
        ArrayList userArray = new ArrayList();
        reader.beginObject();
        while (reader.hasNext())
        {
            String test = reader.nextName();
            if (test.equals("userPosition")){
            userArray = readMessagesArray(reader);}
        }
        ArrayList<OtherUser> userArray2 = new ArrayList();

        return userArray;
    }


    public ArrayList<OtherUser> readMessagesArray(JsonReader reader) throws IOException, NetworkOnMainThreadException {
        ArrayList<OtherUser> usersArray = new ArrayList();
            reader.beginArray();
       try {
           while (reader.hasNext()) {
               usersArray.add(readMessage(reader));
           }
       }
        //    }}
        //catch (IOException e) {System.out.println(e);}
        catch (NetworkOnMainThreadException e) {e.printStackTrace();}
        finally { reader.endArray(); return usersArray;}
    }

    public OtherUser readMessage(JsonReader reader) throws IOException, NetworkOnMainThreadException {
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
        names.add(name);
        locations.add(loc);
        colors.add(color);
        return new OtherUser(name, lat, lng, color);
    }


}

