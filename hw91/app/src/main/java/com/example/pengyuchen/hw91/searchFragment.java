package com.example.pengyuchen.hw91;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.Manifest;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.lang.String;
import java.net.URLEncoder;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


import org.json.JSONObject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class searchFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener
        {

    private LocationManager locationManager;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private EditText keyword;
    private EditText distance;
    private AutoCompleteTextView location;
    private Spinner category;
    private RadioButton radio1;
    private RadioButton radio2;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
                    new LatLng(-40, -168), new LatLng(71, 136));
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.search, container, false);
        final RequestQueue queue = Volley.newRequestQueue(getActivity());

        keyword = (EditText) rootView.findViewById(R.id.keyword);
        distance = (EditText) rootView.findViewById(R.id.distance);
        location =(AutoCompleteTextView)rootView.findViewById(R.id.location);
        category = (Spinner)rootView.findViewById(R.id.category);
        radio1 = (RadioButton) rootView.findViewById(R.id.radio1);
        radio2 = (RadioButton) rootView.findViewById(R.id.radio2);

        Button search = (Button) rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  String lon =Double.toString(MainActivity.lon);
                  String lat =Double.toString(MainActivity.lat);
                 String keywordInput= keyword.getText().toString();
                 String locationInput = location.getText().toString();
                 String categoryInput = category.getSelectedItem().toString().toLowerCase();
                 String girdRadios= radio1.isChecked()?"option1":"option2";
                 String distanceInput = distance.getText().toString().trim().equals("")?"10":distance.getText().toString().trim();


                 // Log.v("msg",input);
                 if(keywordInput.trim().equals("")  ){
                     Toast.makeText(getActivity(), "Please fix all fields with error", Toast.LENGTH_LONG).show();

                     rootView.findViewById(R.id.warning1).setVisibility(TextView.VISIBLE);
                  }else{
                     rootView.findViewById(R.id.warning1).setVisibility(TextView.GONE);
                 }

                  if(radio2.isChecked() &&  locationInput.trim().equals("") ){
                      Toast.makeText(getActivity(), "Please fix all fields with error", Toast.LENGTH_LONG).show();
                      rootView.findViewById(R.id.warning2).setVisibility(TextView.VISIBLE);
                  }else{
                      rootView.findViewById(R.id.warning2).setVisibility(TextView.GONE);
                  }

                  if(!keywordInput.trim().equals("") && !(radio2.isChecked() &&  locationInput.trim().equals("")) ){
                        String url="http://place-env.us-west-1.elasticbeanstalk.com/search?keyword="+URLEncoder.encode(keywordInput);
                        url+="&category="+URLEncoder.encode(categoryInput);
                        url+="&inputDistance="+distanceInput;
                        url+="&gridRadios="+girdRadios;
                        if(girdRadios == "option2") url+="&inputLocation="+URLEncoder.encode(locationInput);
                        url+="&lat="+lat;
                        url+="&lon="+lon;

                      final ProgressDialog pDialog = new ProgressDialog(getActivity());
                      pDialog.setMessage("Fetching results");
                      pDialog.show();


                      //Log.v("msg", url);
                      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                              (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                  @Override
                                  public void onResponse(JSONObject response) {
                                      String res=response.toString();
                                      //Log.v("msg", res);
                                      Intent intent = new Intent();
                                      intent.setClass(getActivity(), resultActivity.class);
                                      intent.putExtra("fromMain", res);
                                      startActivity(intent);
                                      pDialog.hide();

                                  }
                              }, new Response.ErrorListener() {

                                  @Override
                                  public void onErrorResponse(VolleyError error) {
                                      pDialog.hide();
                                      Toast.makeText(getActivity(), "NetWork error", Toast.LENGTH_LONG).show();

                                      Log.v("msg", error.toString());

                                  }
                              });
                     // MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

                      queue.add(jsonObjectRequest);


                  }


              }

        });

        Button clear = (Button) rootView.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.warning1).setVisibility(TextView.GONE);
                rootView.findViewById(R.id.warning2).setVisibility(TextView.GONE);
                keyword.setText("");
                distance.setText("");
                location.setText("");
                category.setSelection(0);
                radio1.setChecked(true);

            }

        });

        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio1:
                        rootView.findViewById(R.id.location).setEnabled(false);
                        rootView.findViewById(R.id.warning2).setVisibility(TextView.GONE);
                        break;
                    case R.id.radio2:
                        rootView.findViewById(R.id.location).setEnabled(true);
                        break;
                }


            }
        });


        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(),mGoogleApiClient,LAT_LNG_BOUNDS, null);

        location.setAdapter(mPlaceAutocompleteAdapter);




        return rootView;


    }





}
