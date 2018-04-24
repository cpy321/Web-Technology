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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class searchFragment extends Fragment {
    private LocationManager locationManager;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.search, container, false);
        final RequestQueue queue = Volley.newRequestQueue(getActivity());

        Button search = (Button) rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 EditText keyword = (EditText) rootView.findViewById(R.id.keyword);
                 EditText distance = (EditText) rootView.findViewById(R.id.distance);
                 AutoCompleteTextView location =(AutoCompleteTextView)rootView.findViewById(R.id.location);
                 Spinner category = (Spinner)rootView.findViewById(R.id.category);
                  RadioButton radio1 = (RadioButton) rootView.findViewById(R.id.radio1);
                  RadioButton radio2 = (RadioButton) rootView.findViewById(R.id.radio2);

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





//        try {
//            Intent intent =
//                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                            .build(getActivity());
//            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//        } catch (Exception e) {
//            Log.e(TAG, e.getStackTrace().toString());
//        }



//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if(requestCode == 2){
//                if (resultCode == RESULT_OK) {
//                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
//                    Toast.makeText(getActivity(), "place "+place.toString(),
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        }



        return rootView;


    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
//                Toast.makeText(getActivity(), "place "+place.toString(),
//                        Toast.LENGTH_LONG).show();
//            }
//        }
//    }



}
