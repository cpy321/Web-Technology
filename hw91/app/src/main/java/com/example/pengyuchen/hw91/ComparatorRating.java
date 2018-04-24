package com.example.pengyuchen.hw91;

import net.sf.json.JSONObject;

import java.util.Comparator;

/**
 * Created by pengyuchen on 4/23/18.
 */

public class ComparatorRating implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        String key1 = o1.getString("rating");
        String key2 = o2.getString("rating");
        int int1 = Integer.parseInt(key1);
        int int2 = Integer.parseInt(key2);


        return int1-int2;
    }


}
