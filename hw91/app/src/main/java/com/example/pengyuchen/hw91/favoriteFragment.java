package com.example.pengyuchen.hw91;

/**
 * Created by pengyuchen on 4/16/18.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class favoriteFragment extends  Fragment {
    private RecyclerView mRecyclerView;
    private MyAdapter2 mAdapter;
    private View rootView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPreferences sp;
    public ArrayList<String> resultData= new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.favorite, container, false);



        initData();
        initView();

        return rootView;
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            initData();
            initView();

        }
    }


    private void initData() {
        resultData.clear();
        sp = getActivity().getSharedPreferences("Favorite", Context.MODE_PRIVATE);
        if(sp !=null) {
            Map<String, ?> allEntries = sp.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                resultData.add(entry.getValue().toString());
            }
        }



            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mAdapter = new MyAdapter2(getData());
            mAdapter.setOnItemClickListener(new MyAdapter2.OnItemClickListener() {

                @Override
                public void onItemLike(ImageView view, int position) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove(JSONObject.fromObject(resultData.get(position)).get("place_id").toString());
                    editor.commit();
                    initData();
                    initView();
                }

                @Override
                public void onItemDetail(View view, int position) {
                    final ProgressDialog pDialog = new ProgressDialog(getActivity());
                    final RequestQueue queue = Volley.newRequestQueue(getActivity());
                    pDialog.setMessage("Fetching details");
                    pDialog.show();

                    final String placeId = JSONObject.fromObject(resultData.get(position)).get("place_id").toString();

                    String url = "http://place-env.us-west-1.elasticbeanstalk.com/detail?place_id="+placeId;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<org.json.JSONObject>() {
                                @Override
                                public void onResponse(org.json.JSONObject response) {
                                    String detailJson=response.toString();
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), detailActivity.class);
                                    intent.putExtra("fromResult", detailJson);
                                    intent.putExtra("fromResultPlaceId", placeId);
                                    startActivity(intent);
                                    pDialog.hide();

                                    //Log.v("msg", detailJson);

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.v("msg", error.toString());
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), detailActivity.class);
                                    intent.putExtra("fromResult", "error");
                                    startActivity(intent);
                                    pDialog.hide();

                                }
                            });
                    queue.add(jsonObjectRequest);

                }
            });




        }

    private void initView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.resultView);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);

    }

    private ArrayList<entity> getData( ) {
        ArrayList<entity> list = new ArrayList<entity>();

        for (int i = 0; i < resultData.size(); i++) {
            Log.v("abc",resultData.get(i));
            JSONObject resultObj = JSONObject.fromObject(resultData.get(i));
            list.add(new entity(resultObj.getString("icon"), resultObj.getString("name"), resultObj.getString("vicinity"),1));
        }

        return list;
    }




}
