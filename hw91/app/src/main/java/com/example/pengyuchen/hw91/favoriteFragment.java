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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class favoriteFragment extends  Fragment {
    private RecyclerView mRecyclerView;
    private MyAdapter2 mAdapter;
    private View rootView;
    private TextView resultWarning;
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

    @Override
    public void onResume() {
        super.onResume();

            initData();
            initView();


    }


    private void initData() {
        resultData.clear();
        resultWarning = rootView.findViewById(R.id.resultWarning);
        sp = getActivity().getSharedPreferences("Favorite", Context.MODE_PRIVATE);


            Map<String, ?> allEntries = sp.getAll();

            if(allEntries.size() != 0 ){

                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    resultData.add(entry.getValue().toString());
                }
                resultWarning.setVisibility(TextView.GONE);
            }else{
                resultWarning.setVisibility(TextView.VISIBLE);
            }






            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mAdapter = new MyAdapter2(getData());
            mAdapter.setOnItemClickListener(new MyAdapter2.OnItemClickListener() {

                @Override
                public void onItemLike(ImageView view, int position) {
                    SharedPreferences.Editor editor = sp.edit();
                    String name = JSONObject.fromObject(resultData.get(position)).getString("name");
                    editor.remove(JSONObject.fromObject(resultData.get(position)).get("place_id").toString());
                    editor.commit();
                    initData();
                    initView();
                    Toast.makeText(getActivity(), name+" was removed to favorites", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onItemDetail(View view, int position) {
                    final ProgressDialog pDialog = new ProgressDialog(getActivity());
                    final RequestQueue queue = Volley.newRequestQueue(getActivity());
                    pDialog.setMessage("Fetching details");
                    pDialog.show();

                    final String placeId = JSONObject.fromObject(resultData.get(position)).get("place_id").toString();
                    final String name = JSONObject.fromObject(resultData.get(position)).getString("name");
                    final String vicinity = JSONObject.fromObject(resultData.get(position)).getString("vicinity");
                    final String icon = JSONObject.fromObject(resultData.get(position)).getString("icon");
                    JSONObject geometry = JSONObject.fromObject(resultData.get(position)).getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    final String lat = location.getString("lat");
                    final String lon = location.getString("lng");


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
                                    intent.putExtra("fromResultPlaceName", name);
                                    intent.putExtra("fromResultAddress",vicinity);
                                    intent.putExtra("fromResultIcon",icon);
                                    intent.putExtra("fromResultLat",lat);
                                    intent.putExtra("fromResultLon",lon);
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
