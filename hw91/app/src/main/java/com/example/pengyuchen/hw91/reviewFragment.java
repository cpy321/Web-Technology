package com.example.pengyuchen.hw91;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
        final Spinner spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        final Spinner spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);

        String resultJson = ((detailActivity)getActivity()).getmTitle();
        String yelpJson = ((detailActivity)getActivity()).getYelp();

        String status = JSONObject.fromObject(resultJson).get("status").toString();
        JSONObject yelpObj = (JSONObject)JSONObject.fromObject(yelpJson);

        if(status.equals("OK")){
            JSONObject resultObj = (JSONObject) JSONObject.fromObject(resultJson).get("result");

            if(resultObj.get("reviews")!= null){
                reviewJson = resultObj.get("reviews").toString();

                reviewArr = JSONArray.fromObject(reviewJson);
                initGoogle = reviewArr;

                initData(1);
                initView();
            }

        }

        if(!yelpObj.isNullObject() && yelpObj.get("failed") == null){
            String yelp = yelpObj.get("reviews").toString();
            initYelp = JSONArray.fromObject(yelp);

        }









        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if(initGoogle == null || initGoogle.size() ==0){
                            rootView.findViewById(R.id.warning).setVisibility(TextView.VISIBLE);
                        }else{
                            rootView.findViewById(R.id.warning).setVisibility(TextView.GONE);
                            switch(spinner2.getSelectedItemPosition()){
                                case 0:
                                    reviewArr = initGoogle;
                                    break;
                                case 1:
                                    reviewArr = ratingHigh(initGoogle);
                                    break;
                                case 2:
                                    reviewArr = ratingLow(initGoogle);
                                    break;
                                case 3:
                                    reviewArr = timeHigh(initGoogle);
                                    break;
                                case 4:
                                    reviewArr = timeLow(initGoogle);
                                    break;
                            };

                            initData(1);
                            initView();
                        }

                        return;
                    case 1:
                        if(initYelp == null || initYelp.size() ==0){
                            rootView.findViewById(R.id.warning).setVisibility(TextView.VISIBLE);
                        }else{
                            rootView.findViewById(R.id.warning).setVisibility(TextView.GONE);
                            switch(spinner2.getSelectedItemPosition()){
                                case 0:
                                    reviewArr = initYelp;
                                    break;
                                case 1:
                                    reviewArr = ratingHigh(initYelp);
                                    break;
                                case 2:
                                    reviewArr = ratingLow(initYelp);
                                    break;
                                case 3:
                                    reviewArr = timeYelpHigh(initYelp);
                                    break;
                                case 4:
                                    reviewArr = timeYelpLow(initYelp);
                                    break;
                            };
                            initData(2);
                            initView();
                        }
                        return;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner1.getSelectedItemPosition() == 0){
                    if(initGoogle == null || initGoogle.size() == 0) return;
                    switch (position){
                        case 0:
                            reviewArr = initGoogle;
                            initData(1);
                            initView();
                            return;
                        case 1:

                            reviewArr = ratingHigh(initGoogle);
                            initData(1);
                            initView();
                            return;
                        case 2:
                            reviewArr = ratingLow(initGoogle);
                            initData(1);
                            initView();
                            return;
                        case 3:
                            reviewArr = timeHigh(initGoogle);
                            initData(1);
                            initView();
                            return;
                        case 4:
                            reviewArr = timeLow(initGoogle);
                            initData(1);
                            initView();
                            return;

                    }
                }else{
                    if(initYelp == null || initYelp.size() == 0) return;

                    switch (position){
                        case 0:
                            reviewArr = initYelp;
                            initData(2);
                            initView();
                            return;
                        case 1:

                            reviewArr = ratingHigh(initYelp);
                            initData(2);
                            initView();
                            return;
                        case 2:
                            reviewArr = ratingLow(initYelp);
                            initData(2);
                            initView();
                            return;
                        case 3:
                            reviewArr = timeYelpHigh(initYelp);
                            initData(2);
                            initView();
                            return;
                        case 4:
                            reviewArr = timeYelpLow(initYelp);
                            initData(2);
                            initView();
                            return;

                    }
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

    private JSONArray timeLow(JSONArray reviewArr){
        List<JSONObject> list = new ArrayList<JSONObject> ();
        JSONObject jsonObj = null;
        for (int i = 0; i < reviewArr.size(); i++) {
            jsonObj = (JSONObject)reviewArr.get(i);
            list.add(jsonObj);
        }
        Collections.sort(list,new ComparatorTime());
        JSONArray res = new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            jsonObj = list.get(i);
            res.add(jsonObj);
        }
        return res;
    }

    private JSONArray timeHigh(JSONArray reviewArr){
        List<JSONObject> list = new ArrayList<JSONObject> ();
        JSONObject jsonObj = null;
        for (int i = 0; i < reviewArr.size(); i++) {
            jsonObj = (JSONObject)reviewArr.get(i);
            list.add(jsonObj);
        }
        Collections.sort(list,new ComparatorTime());
        JSONArray res = new JSONArray();

        for (int i = list.size()-1; i >=0 ; i--) {
            jsonObj = list.get(i);
            res.add(jsonObj);
        }
        return res;
    }

    private JSONArray timeYelpHigh(JSONArray reviewArr){
        List<JSONObject> list = new ArrayList<JSONObject> ();
        JSONObject jsonObj = null;
        for (int i = 0; i < reviewArr.size(); i++) {
            jsonObj = (JSONObject)reviewArr.get(i);
            list.add(jsonObj);
        }
        Collections.sort(list,new ComparatorYelpTime());
        JSONArray res = new JSONArray();

        for (int i = list.size()-1; i >=0 ; i--) {
            jsonObj = list.get(i);
            res.add(jsonObj);
        }
        return res;
    }

    private JSONArray timeYelpLow(JSONArray reviewArr){
        List<JSONObject> list = new ArrayList<JSONObject> ();
        JSONObject jsonObj = null;
        for (int i = 0; i < reviewArr.size(); i++) {
            jsonObj = (JSONObject)reviewArr.get(i);
            list.add(jsonObj);
        }
        Collections.sort(list,new ComparatorYelpTime());
        JSONArray res = new JSONArray();

        for (int i=0; i<list.size(); i++) {
            jsonObj = list.get(i);
            res.add(jsonObj);
        }
        return res;
    }


    private void initData(final int i) {

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        if(i == 1){
            mAdapter = new reviewAdapter(getData1());
        }else{
            mAdapter = new reviewAdapter(getData2());
        }
        mAdapter.setOnItemClickListener(new reviewAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                String url = new String();
                if(i == 1){
                    url = reviewArr.getJSONObject(position).getString("author_url");
                }else{
                    url = reviewArr.getJSONObject(position).getString("url");
                }

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });




    }

    private void initView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviewView);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);

    }

    private ArrayList<entity2> getData1( ) {
        ArrayList<entity2> list = new ArrayList<entity2>();



        for (int i = 0; i < reviewArr.size(); i++) {


            list.add(new entity2(reviewArr.getJSONObject(i).getString("profile_photo_url"),
                    reviewArr.getJSONObject(i).getString("author_name"),
                    reviewArr.getJSONObject(i).getString("rating"),
                   reviewArr.getJSONObject(i).getString("time"),
                   reviewArr.getJSONObject(i).getString("text")));

        }

        return list;
    }

    private ArrayList<entity2> getData2( ) {
        ArrayList<entity2> list = new ArrayList<entity2>();



        for (int i = 0; i < reviewArr.size(); i++) {

            String user = reviewArr.getJSONObject(i).getString("user");
            JSONObject userObj = JSONObject.fromObject(user);
            String image_url = userObj.get("image_url").toString();
            String name = userObj.get("name").toString();

            list.add(new entity2(image_url,
                    name,
                    reviewArr.getJSONObject(i).getString("rating"),
                    reviewArr.getJSONObject(i).getString("time_created"),
                    reviewArr.getJSONObject(i).getString("text")));

        }

        return list;
    }
}
