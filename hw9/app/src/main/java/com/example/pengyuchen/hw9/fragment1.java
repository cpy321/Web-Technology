package com.example.pengyuchen.hw9;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class fragment1 extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_one, null);

        Button search = (Button) view.findViewById(R.id.search);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("123","232");
            }
        });


        return view;
    }

}