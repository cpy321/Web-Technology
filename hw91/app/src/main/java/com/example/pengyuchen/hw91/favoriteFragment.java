package com.example.pengyuchen.hw91;

/**
 * Created by pengyuchen on 4/16/18.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class favoriteFragment extends  Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite, container, false);

        return rootView;
    }
}
