package com.example.pengyuchen.hw91;

import android.util.Log;

import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by pengyuchen on 4/23/18.
 */

public class ComparatorYelpTime implements Comparator<JSONObject> {
    long long1;
    long long2;
    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        String key1 = o1.getString("time_created");
        String key2 = o2.getString("time_created");



        Date date1 = new Date();
        Date date2 = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            date1 = dateFormat.parse(key1);
            date2 = dateFormat.parse(key2);
            long1 = date1.getTime();
            long2 = date2.getTime();
        } catch(ParseException e) {

            e.printStackTrace();
        }


        if(long1 > long2) return 1;
        return -1;

    }


}
