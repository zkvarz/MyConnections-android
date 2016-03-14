package com.example.myconnections_android.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.myconnections_android.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapAnimationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng TIMES_SQUARE = new LatLng(40.7577, -73.9857);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);

    private GoogleMap googleMap;
    private PolylineOptions polylineOptions;
    private ArrayList<LatLng> arrayPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_animation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng firstLocation = new LatLng(49.239121, 25.070801);
        LatLng secondLocation = new LatLng(49.975955, 36.543274);
        this.googleMap = googleMap;
/*        googleMap.addMarker(new MarkerOptions()
                .position(firstLocation)
                .title("Marker"));*/


//        setUpMapIfNeeded(googleMap);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("MapAnimation: ", "everything BAD");
            return;
        }
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        Log.d("MapAnimation: ", " onMapReady end");
    }

    private void setUpMapIfNeeded(GoogleMap googleMap) {
        // check if we have got the googleMap already
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (googleMap != null) {
                addLines(googleMap);
            }
        }
    }

    private void addLines(GoogleMap googleMap) {

        googleMap.addPolyline((new PolylineOptions())
                .add(TIMES_SQUARE, BROOKLYN_BRIDGE, LOWER_MANHATTAN,
                        TIMES_SQUARE).width(5).color(Color.BLUE)
                .geodesic(true));
        // move camera to zoom on map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN,
                13));
    }

    @Override
    public void onMapClick(LatLng latLng) {

        // Instantiating the class MarkerOptions to plot marker on the map
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude of the marker position
        markerOptions.position(latLng);

        // Setting titile of the infowindow of the marker
        markerOptions.title("Position");

        // Setting the content of the infowindow of the marker
        markerOptions.snippet("Latitude:" + latLng.latitude + "," + "Longitude:" + latLng.longitude);

       // Instantiating the class PolylineOptions to plot polyline in the map
        PolylineOptions polylineOptions = new PolylineOptions();

        // Setting the color of the polyline
        polylineOptions.color(Color.BLUE);

        // Setting the width of the polyline
        polylineOptions.width(3);

        // Adding the taped point to the ArrayList
        arrayPoints.add(latLng);

        // Setting points of polyline
        polylineOptions.addAll(arrayPoints);

        // Adding the polyline to the map
//        googleMap.addPolyline(polylineOptions);

        createDashedLine(googleMap, Color.BLUE, polylineOptions);

        // Adding the marker to the map
        googleMap.addMarker(markerOptions);

    }

    @Override
    public void onMapLongClick(LatLng point) {
        // Clearing the markers and polylines in the google map
        googleMap.clear();
        // Empty the array list
        arrayPoints.clear();
    }

    public  void createDashedLine(GoogleMap map, int color,  PolylineOptions polylineOptions){
//        double difLat = latLngDest.latitude - latLngOrig.latitude;
//        double difLng = latLngDest.longitude - latLngOrig.longitude;

        double difLat = arrayPoints.get(arrayPoints.size() - 1).latitude - arrayPoints.get(0).latitude;
        double difLng = arrayPoints.get(arrayPoints.size() - 1).longitude - arrayPoints.get(0).longitude;

        double zoom = map.getCameraPosition().zoom;

        double divLat = difLat / (zoom * 2);
        double divLng = difLng / (zoom * 2);

        LatLng tmpLatOri = arrayPoints.get(0);

        for(int i = 0; i < (zoom * 2); i++){
            LatLng loopLatLng = tmpLatOri;

            if(i > 0){
                loopLatLng = new LatLng(tmpLatOri.latitude + (divLat * 0.25f), tmpLatOri.longitude + (divLng * 0.25f));
            }

            Polyline polyline = map.addPolyline(new PolylineOptions()
                    .add(loopLatLng)
                    .add(new LatLng(tmpLatOri.latitude + divLat, tmpLatOri.longitude + divLng))
                    .color(color)
                    .width(5f));

            tmpLatOri = new LatLng(tmpLatOri.latitude + divLat, tmpLatOri.longitude + divLng);
        }
    }


}
