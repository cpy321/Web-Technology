package com.example.pengyuchen.hw91;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pengyuchen on 4/21/18.
 */

public class reviewFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private reviewAdapter mAdapter;
    private String reviewJson;
    private JSONObject reviewObj;
    private JSONArray reviewArr;
    private JSONArray initGoogle;
    private JSONArray initYelp;





    View rootView;

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.review, container, false);
        Spinner spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        Spinner spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);

        String resultJson = ((detailActivity)getActivity()).getmTitle();

        String status = JSONObject.fromObject(resultJson).get("status").toString();

        if(status.equals("OK")){
            JSONObject resultObj = (JSONObject) JSONObject.fromObject(resultJson).get("result");

            if(resultObj.get("reviews")!= null){
                reviewJson = resultObj.get("reviews").toString();

                reviewArr = JSONArray.fromObject(reviewJson);
                initGoogle = reviewArr;

                initData();
                initView();
            }

        }









        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                    case 1:
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        reviewArr = initGoogle;
                        initData();
                        initView();

                    case 1:
                        reviewArr = ratingHigh(initGoogle);
                        initData();
                        initView();

                    case 2:
                        reviewArr = ratingLow(initGoogle);
                        initData();
                        initView();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //未选中时候的操作
            }
        });


        return rootView;
    }

    private JSONArray ratingHigh(JSONArray reviewArr){
        List<JSONObject> list = new ArrayList<JSONObject> ();
        JSONObject jsonObj = null;
        for (int i = 0; i < reviewArr.size(); i++) {
            jsonObj = (JSONObject)reviewArr.get(i);
            list.add(jsonObj);
        }
        Collections.sort(list,new ComparatorRating());
        JSONArray res = new JSONArray();
        for (int i = list.size()-1; i >=0 ; i--) {
            jsonObj = list.get(i);
            res.add(jsonObj);
        }
        return res;
    }

    private JSONArray ratingLow(JSONArray reviewArr){
        List<JSONObject> list = new ArrayList<JSONObject> ();
        JSONObject jsonObj = null;
        for (int i = 0; i < reviewArr.size(); i++) {
            jsonObj = (JSONObject)reviewArr.get(i);
            list.add(jsonObj);
        }
        Collections.sort(list,new ComparatorRating());

        JSONArray res = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            jsonObj = list.get(i);
            res.add(jsonObj);
        }
        return res;
    }


    private void initData() {

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new reviewAdapter(getData());



    }

    private void initView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviewView);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);

    }

    private ArrayList<entity2> getData( ) {
        ArrayList<entity2> list = new ArrayList<entity2>();



        for (int i = 0; i < reviewArr.size(); i++) {

            Log.v("1234", reviewArr.getJSONObject(i).getString("rating"));

            list.add(new entity2(reviewArr.getJSONObject(i).getString("profile_photo_url"),
                    reviewArr.getJSONObject(i).getString("author_name"),
                    reviewArr.getJSONObject(i).getString("rating"),
                   reviewArr.getJSONObject(i).getString("time"),
                   reviewArr.getJSONObject(i).getString("text")));

        }

        return list;
    }
}
