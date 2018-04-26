package com.example.pengyuchen.hw91;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
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
import android.content.SharedPreferences;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Math.E;

public class resultActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SharedPreferences sp ;
    private MyAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private JSONObject obj;
    public ArrayList<String> resultData= new ArrayList<>();
    private int page;
    private String nextPageToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        final RequestQueue queue = Volley.newRequestQueue(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.resultToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();



        page = 0;
        resultData.add(bundle.getString("fromMain"));

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pDialog = new ProgressDialog(resultActivity.this);
                pDialog.setMessage("Fetching next page");
                pDialog.show();

                String url="http://place-env.us-west-1.elasticbeanstalk.com/next?nextToken="+nextPageToken;
                //Log.v("msg", url);
                page +=1;
                if(resultData.size() <= page) {

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<org.json.JSONObject>() {

                                @Override
                                public void onResponse(org.json.JSONObject response) {
                                    String res = response.toString();

                                    resultData.add(res);
                                    initData();
                                    initView();
                                    pDialog.hide();

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.v("msg", error.toString());
                                    page -=1;
                                    pDialog.hide();

                                }
                            });
                        queue.add(jsonObjectRequest);
                }else{
                    initData();
                    initView();
                    pDialog.hide();
                }


            }

        });

        Button previous = (Button) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pDialog = new ProgressDialog(resultActivity.this);
                pDialog.setMessage("Fetching previous page");
                pDialog.show();
                page -=1;
                initData();
                initView();
                pDialog.hide();
            }

        });



        initData();
        initView();


    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
        initView();
    }

    private void initData() {

        obj = JSONObject.fromObject(resultData.get(page));
        String status = obj.get("status").toString();
        Object nextPage = obj.get("next_page_token");


        if(status.equals("OK")){
            findViewById(R.id.resultWarning).setVisibility(TextView.GONE);
            if(nextPage != null){
                findViewById(R.id.next).setEnabled(true);
                nextPageToken = nextPage.toString();
            }else{
                findViewById(R.id.next).setEnabled(false);
            }

            if(page > 0 ){
                findViewById(R.id.previous).setEnabled(true);
            }else{
                findViewById(R.id.previous).setEnabled(false);
            }

            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mAdapter = new MyAdapter(getData());
            mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                String resultLike = obj.get("results").toString();
                JSONArray likeArray = JSONArray.fromObject(resultLike);

                @Override
                public void onItemLike(ImageView view, int position) {
                    sp = getSharedPreferences("Favorite", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    String key = likeArray.getJSONObject(position).getString("place_id").toString();
                    String val = likeArray.getJSONObject(position).toString();

                    if(view.getDrawable().getCurrent().getConstantState()==getResources().getDrawable(R.drawable.heart_black).getConstantState()){
                        view.setImageResource(R.drawable.heart_fill_red);

                        editor.putString(key, val);

                    }else{
                        view.setImageResource(R.drawable.heart_black);
                        editor.remove(key);
                    }
                    editor.commit();

                }

                @Override
                public void onItemDetail(View view, int position) {
                    final ProgressDialog pDialog = new ProgressDialog(resultActivity.this);
                    final RequestQueue queue = Volley.newRequestQueue(resultActivity.this);
                    pDialog.setMessage("Fetching details");
                    pDialog.show();
                    final String placeId = likeArray.getJSONObject(position).getString("place_id").toString();
                    final String name = likeArray.getJSONObject(position).getString("name").toString();
                    final String vicinity = likeArray.getJSONObject(position).getString("vicinity").toString();
                    final String icon = likeArray.getJSONObject(position).getString("icon").toString();
                    JSONObject geometry = (JSONObject)likeArray.getJSONObject(position).get("geometry");
                    JSONObject location = (JSONObject)geometry.get("location");
                    final String lat = location.getString("lat");
                    final String lon = location.getString("lng");


                    String url = "http://place-env.us-west-1.elasticbeanstalk.com/detail?place_id="+placeId;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<org.json.JSONObject>() {
                                @Override
                                public void onResponse(org.json.JSONObject response) {
                                    String detailJson=response.toString();
                                    Intent intent = new Intent();
                                    intent.setClass(resultActivity.this, detailActivity.class);
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
                                    intent.setClass(resultActivity.this, detailActivity.class);
                                    intent.putExtra("fromResult", "error");
                                    startActivity(intent);
                                    pDialog.hide();

                                }
                            });
                    queue.add(jsonObjectRequest);

                }
            });




        }else{
            findViewById(R.id.resultWarning).setVisibility(TextView.VISIBLE);
        }


    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.resultView);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);

    }

    private ArrayList<entity> getData( ) {
        ArrayList<entity> list = new ArrayList<entity>();
        String result = obj.get("results").toString();
        JSONArray jsonArray = JSONArray.fromObject(result);
        sp = getSharedPreferences("Favorite", Context.MODE_PRIVATE);



        for (int i = 0; i < jsonArray.size(); i++) {
            String place_id = jsonArray.getJSONObject(i).getString("place_id");

            if (sp.contains(place_id)){
                list.add(new entity(jsonArray.getJSONObject(i).getString("icon"), jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("vicinity"),1));

            }else{
                list.add(new entity(jsonArray.getJSONObject(i).getString("icon"), jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("vicinity"),0));

            }

        }

        return list;
    }




}
