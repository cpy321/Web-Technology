package com.example.pengyuchen.hw91;


import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by pengyuchen on 4/21/18.
 */

public class mapFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap mMap;

    public static mapFragment newInstance() {
        mapFragment fragment = new mapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        final String placeId = ((detailActivity)getActivity()).getPlaceId();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    public void onMapReady(GoogleMap googleMap) {

    }

}
