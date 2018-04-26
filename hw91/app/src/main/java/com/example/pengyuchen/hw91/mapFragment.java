package com.example.pengyuchen.hw91;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Created by pengyuchen on 4/21/18.
 */

public class mapFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap mgoogleMap;
    private float lon;
    private float lat;
    private String name;

    public static mapFragment newInstance() {
        mapFragment fragment = new mapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.map, container, false);
        final String placeId = ((detailActivity)getActivity()).getPlaceId();
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.mode);
        final EditText editText = (EditText) rootView.findViewById(R.id.start);

        name = ((detailActivity)getActivity()).getName();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String start = editText.getText().toString();
                if(start.trim().equals("")) return;
                mgoogleMap.clear();
                mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                        .title("Marker"));
                mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lat, lon), 10));

                switch (position){
                    case 0:

                        nav(start,"DRIVING");
                        return;
                    case 1:
                        nav(start,"BICYCLING");
                        return;
                    case 2:
                        nav(start,"TRANSIT");
                        return;
                    case 3:
                        nav(start,"WALKING");
                        return;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lon = Float.parseFloat(((detailActivity)getActivity()).getLon());
        lat = Float.parseFloat(((detailActivity)getActivity()).getLat());

        return rootView;
    }

    public void nav(String start, String mode){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());


        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+start+"&destination="+name+"&mode="+mode.toLowerCase()+"&key=AIzaSyBujGjblZKUI3x7RPPeuuibylg57cxIikg";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject response) {
                        String res = response.toString();
                        try{
                            String routes = JSONObject.fromObject(res).get("routes").toString();
                            String legs =  JSONArray.fromObject(routes).getJSONObject(0).getString("legs");
                            String startLoc = JSONArray.fromObject(legs).getJSONObject(0).getString("start_location");
                            float dlat = Float.parseFloat(JSONObject.fromObject(startLoc).getString("lat"));
                            float dlon = Float.parseFloat(JSONObject.fromObject(startLoc).getString("lng"));
                            mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(dlat, dlon))
                                    .title("Marker"));

                            String steps = JSONArray.fromObject(legs).getJSONObject(0).getString("steps");
                            JSONArray stepsArr = JSONArray.fromObject(steps);
                            String[] polylines = new String[stepsArr.size()];

                            for(int i=0; i<stepsArr.size(); i++){
                                try{
                                    PolylineOptions options = new PolylineOptions();
                                    options.color(Color.BLUE);
                                    options.width(10);
                                    options.addAll(PolyUtil.decode(getPath(stepsArr.getJSONObject(i))));
                                    mgoogleMap.addPolyline(options);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                    
                                }
                            }




                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("msg", error.toString());


                    }
                });
        queue.add(jsonObjectRequest);

    }


    public String getPath(JSONObject googlePathJson){
        String polyline = "";
        try{
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return polyline;
    }

    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(lat, lon);
        mgoogleMap =googleMap;
        googleMap.addMarker(new MarkerOptions().position(location)
                .title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                location, 10));

    }

}
