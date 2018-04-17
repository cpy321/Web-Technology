package com.example.pengyuchen.hw91;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.String;
import android.widget.Toast;


public class searchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.search, container, false);


        Button search = (Button) rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 EditText keyword = (EditText) rootView.findViewById(R.id.keyword);
                 AutoCompleteTextView location =(AutoCompleteTextView)rootView.findViewById(R.id.location);
                 String keywordInput= keyword.getText().toString();
                 String locationInput = location.getText().toString();


                 // Log.v("msg",input);
                 if(keywordInput.trim().equals("") || keywordInput.equals("")  ){
                     Toast.makeText(getActivity(), "Please fix all fields with error", Toast.LENGTH_LONG).show();

                     rootView.findViewById(R.id.warning1).setVisibility(TextView.VISIBLE);
                  }

                  if(rootView.findViewById(R.id.radio2).isSelected()){
                      Toast.makeText(getActivity(), "Please fix all fields with error", Toast.LENGTH_LONG).show();
                      rootView.findViewById(R.id.warning2).setVisibility(TextView.VISIBLE);
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


        return rootView;


    }


}
