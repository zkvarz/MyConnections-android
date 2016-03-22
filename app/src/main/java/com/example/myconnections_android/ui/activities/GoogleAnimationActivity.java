package com.example.myconnections_android.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.example.myconnections_android.R;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.ui.fragments.SyncedMapFragment;
import com.example.myconnections_android.ui.map.animations.LatLngInterpolator;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class GoogleAnimationActivity extends FragmentActivity implements SyncedMapFragment.OnAnimated, OnMapReadyCallback {
    private SyncedMapFragment mMapFragment;
    private GoogleMap mMap;
    private Marker mMarker;
    private boolean isAnimated;

    private static final LatLng PARIS_LAT_LONG = new LatLng(48.85661, 2.35222);
    private static final LatLng WARSAW_LAT_LONG = new LatLng(52.22968, 21.01223);

    private LatLngInterpolator mLatLngInterpolator;


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
        mMapFragment.getMapAsync(this);
    }


    private GoogleMap.OnMapClickListener mMapClickListener = new GoogleMap.OnMapClickListener() {


        @Override
        public void onMapClick(LatLng point) {
            Logger.debug(getClass(), "onMapClick " + isAnimated);


        }
    };


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Logger.debug(getClass(), "dispatchTouchEvent" + isAnimated);

        if (mMap != null)
            Logger.debug(getClass(), "Zoom Value: " + mMap.getCameraPosition().zoom);

        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onAnimated(boolean isAnimated) {
        Logger.debug(getClass(), "isAnimated ?? " + isAnimated);
        this.isAnimated = isAnimated;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//            @Override
//            public void onMapLoaded() {
        //            }
//        });
                Logger.info(GoogleAnimationActivity.this.getClass(), "Map loaded successfully :)");
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                mMap.getUiSettings().setAllGesturesEnabled(false);

                ArrayList<Marker> markers = new ArrayList<>();
                Marker parisMarket = mMap.addMarker(new MarkerOptions()
                        .position(PARIS_LAT_LONG)
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_circle_small)));

                Marker warsawMarket = mMap.addMarker(new MarkerOptions()
                        .position(WARSAW_LAT_LONG)
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_circle_small)));
                markers.add(parisMarket);
                markers.add(warsawMarket);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker m : markers) {
                    builder.include(m.getPosition());
                }
                LatLngBounds bounds = builder.build();

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                int padding = 60; // offset from edges of the map in pixels

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, metrics.widthPixels, metrics.heightPixels, padding);
                mMap.moveCamera(cu);

                createDashedLine();
                Logger.debug(getClass(), "pre beginPlaneAnimation");
                beginPlaneAnimation();

    }

    private void createDashedLine() {
        ArrayList<LatLng> arrayPoints = new ArrayList<>();
        arrayPoints.add(PARIS_LAT_LONG);
        arrayPoints.add(WARSAW_LAT_LONG);

        LatLng lastElement = arrayPoints.get(arrayPoints.size() - 1);

        double difLat = arrayPoints.get(arrayPoints.size() - 1).latitude - arrayPoints.get(0).latitude;
        double difLng = arrayPoints.get(arrayPoints.size() - 1).longitude - arrayPoints.get(0).longitude;

        double zoom = mMap.getCameraPosition().zoom;

        double divLat = difLat / (zoom * 2);
        double divLng = difLng / (zoom * 2);

        LatLng tmpLatOri = arrayPoints.get(0);

        for (int i = 0; i < (zoom * 2); i++) {
            LatLng loopLatLng = tmpLatOri;

            if (i > 0) {
                loopLatLng = new LatLng(tmpLatOri.latitude + (divLat * 0.25f), tmpLatOri.longitude + (divLng * 0.25f));
            }

            Logger.debug(getClass(), "LoopLat latitude" + loopLatLng.latitude);
            Logger.debug(getClass(), "LoopLat longitude" + loopLatLng.longitude);

            //TEMP solution when polyline goes beyond last marker.
            if (lastElement.latitude < loopLatLng.latitude || lastElement.longitude < loopLatLng.longitude) {
                Logger.debug(getClass(), "lastElement.latitude > loopLatLng.latitude");
                return;
            }

            Polyline polyline = mMap.addPolyline(new PolylineOptions()
                    .add(loopLatLng)
                    .add(new LatLng(tmpLatOri.latitude + divLat, tmpLatOri.longitude + divLng))
                    .color(Color.BLUE)
                    .width(5f));

            tmpLatOri = new LatLng(tmpLatOri.latitude + divLat, tmpLatOri.longitude + divLng);
        }
    }

    private void beginPlaneAnimation() {
        Logger.debug(getClass(), "beginPlaneAnimation");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.debug(GoogleAnimationActivity.class.getClass(), "beginPlaneAnimation run");
                if (!isAnimated) {
                    Logger.debug(getClass(), "CAN BE CLICKED " + isAnimated);
                    if (mMarker != null) {
                        mMapFragment.animateMarkerToGB(mMarker, WARSAW_LAT_LONG, mLatLngInterpolator, 1500);
                    } else {
                        mLatLngInterpolator = new LatLngInterpolator.Linear();
                        // Uses a custom icon on marker.
                        Logger.debug(GoogleAnimationActivity.class.getClass(), "beginPlaneAnimation run marker");
                        mMarker = mMap.addMarker(new MarkerOptions()
                                .position(PARIS_LAT_LONG)
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.plane_huge)));
                        Logger.debug(getClass(), "beginPlaneAnimation run after");
                        if (mMarker != null) {
                            mMapFragment.animateMarkerToGB(mMarker, WARSAW_LAT_LONG, mLatLngInterpolator, 5500);
                        }
                    }
                }
            }
        }, 1000);


    }
}