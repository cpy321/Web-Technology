package com.example.pengyuchen.hw91;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.sf.json.JSONObject;

/**
 * Created by pengyuchen on 4/21/18.
 */

public class infoFragment extends Fragment {
    String detailJson;
    JSONObject detailObj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info, container, false);



        detailJson = ((detailActivity)getActivity()).getmTitle();

        String status = JSONObject.fromObject(detailJson).get("status").toString();


        if(status.equals("OK")){
            detailObj = (JSONObject) JSONObject.fromObject(detailJson).get("result");
            TextView address = (TextView)rootView.findViewById(R.id.address);
            TextView phoneNumber = (TextView)rootView.findViewById(R.id.phoneNumber);
            TextView priceLevel = (TextView)rootView.findViewById(R.id.priceLevel);
            RatingBar rating = (RatingBar) rootView.findViewById(R.id.rating);
            TextView page = (TextView)rootView.findViewById(R.id.page);
            TextView website = (TextView)rootView.findViewById(R.id.website);

            if(detailObj.get("formatted_address") != null){
                address.setText(detailObj.get("formatted_address").toString());
            }else{
                rootView.findViewById(R.id.addressRow).setVisibility(TextView.GONE);
            }

            if(detailObj.get("international_phone_number") != null){
                phoneNumber.setText(detailObj.get("international_phone_number").toString());
            }else{
                rootView.findViewById(R.id.phoneNumberRow).setVisibility(TextView.GONE);
            }

            if(detailObj.get("price_level") != null){
                int price = Integer.parseInt(detailObj.get("price_level").toString());
                String priceSign="";

                for(int i=0; i<price; i++) priceSign+="$";

                priceLevel.setText(priceSign);
            }else{
                rootView.findViewById(R.id.priceLevelRow).setVisibility(TextView.GONE);

            }

            if(detailObj.get("rating") != null){
                String num = detailObj.get("rating").toString();
                rating.setRating(Float.parseFloat(num));
            }else{
                rootView.findViewById(R.id.ratingRow).setVisibility(TextView.GONE);

            }

            if(detailObj.get("url") != null){
               page.setText(detailObj.get("url").toString());
            }else{
                rootView.findViewById(R.id.pageRow).setVisibility(TextView.GONE);

            }
            if(detailObj.get("website") != null){
               website.setText(detailObj.get("website").toString());
            }else{
                rootView.findViewById(R.id.websiteRow).setVisibility(TextView.GONE);

            }



        }


        return rootView;
    }
}
