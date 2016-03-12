package com.example.myconnections_android.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

import com.example.myconnections_android.R;
import com.example.myconnections_android.helpers.Logger;
import com.example.myconnections_android.ui.fragments.SyncedMapFragment;
import com.example.myconnections_android.ui.map.animations.LatLngInterpolator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class GoogleAnimationActivity extends FragmentActivity implements SyncedMapFragment.OnAnimated {
    private SyncedMapFragment mMapFragment;
    private GoogleMap mMap;
    private Marker mMarker;
    private Object marketImage;
    private boolean isAnimated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapFragment = new SyncedMapFragment();
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, mMapFragment)
                .commit();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mMap = mMapFragment.getMap();
        mMap.setOnMapClickListener(mMapClickListener);
    }


    private GoogleMap.OnMapClickListener mMapClickListener = new GoogleMap.OnMapClickListener() {
        private LatLngInterpolator mLatLngInterpolator;


        @Override
        public void onMapClick(LatLng point) {
            Logger.debug(getClass(), "onMapClick " + isAnimated);
//            BitmapDescriptor subwayBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.plane_huge);

            if (!isAnimated) {
                Logger.debug(getClass(), "CAN BE CLICKED " + isAnimated);
                if (mMarker != null) {
                    mMapFragment.animateMarkerToGB(mMarker, point, mLatLngInterpolator, 1500);
                } else {
                    mLatLngInterpolator = new LatLngInterpolator.Linear();
                    // Uses a custom icon on marker.
                    mMarker = mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.plane_huge)));
                }
            }
        }
    };


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Logger.debug(getClass(), "dispatchTouchEvent" + isAnimated);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onAnimated(boolean isAnimated) {
        Logger.debug(getClass(), "isAnimated ?? " + isAnimated);
        this.isAnimated = isAnimated;
    }


}