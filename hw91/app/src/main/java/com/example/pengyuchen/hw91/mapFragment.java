package com.example.pengyuchen.hw91;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Created by pengyuchen on 4/21/18.
 */

public class mapFragment extends Fragment implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener{
    private GoogleMap mgoogleMap;
    private float lon;
    private float lat;
    private String name;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView startEditor;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private Spinner spinner;

    public static mapFragment newInstance() {
        mapFragment fragment = new mapFragment();
        return fragment;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.map, container, false);
        final String placeId = ((detailActivity)getActivity()).getPlaceId();
        spinner = (Spinner) rootView.findViewById(R.id.mode);

        startEditor = rootView.findViewById(R.id.start);


        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(),mGoogleApiClient,LAT_LNG_BOUNDS, null);

        startEditor.setAdapter(mPlaceAutocompleteAdapter);

        startEditor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                mgoogleMap.clear();
                mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                        .title("Marker"));
                mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lat, lon), 10));
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    String start = startEditor.getText().toString();
                    if(!start.trim().equals("")){
                        int pos = spinner.getSelectedItemPosition();
                        switch (pos){
                            case 0:
                                nav(start,"DRIVING");
                                break;
                            case 1:
                                nav(start,"BICYCLING");
                                break;
                            case 2:
                                nav(start,"TRANSIT");
                                break;
                            case 3:
                                nav(start,"WALKING");
                                break;
                        }
                    }

                }

                return false;
            }
        });

        name = ((detailActivity)getActivity()).getName();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String start = startEditor.getText().toString();
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
