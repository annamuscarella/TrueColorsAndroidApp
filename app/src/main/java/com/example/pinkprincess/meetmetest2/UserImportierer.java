package com.example.pinkprincess.meetmetest2;

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

public ArrayList<OtherUser> readJsonStream(InputStream in) throws IOException {
    JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
    try {
        return readMessagesArray(reader);}
    finally {reader.close();}

}

    public ArrayList<OtherUser> readMessagesArray(JsonReader reader) throws IOException {
        ArrayList<OtherUser> messages = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public OtherUser readMessage(JsonReader reader) throws IOException {
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
            } else if (test.equals("lng")) {
                lng = reader.nextDouble();
            } else if (test.equals("farbe")) {
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

