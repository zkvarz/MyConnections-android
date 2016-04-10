package com.example.myconnections_android.ui.activities.map;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myconnections_android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract class MapAnimationActivityFragment extends Fragment implements OnMapReadyCallback {

    public MapAnimationActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_animation, container, false);
    }

    public void onMapReady(GoogleMap var1){
//        this.getMapAsync();
    }
}
