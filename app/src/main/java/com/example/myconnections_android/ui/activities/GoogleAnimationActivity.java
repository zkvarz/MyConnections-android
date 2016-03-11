package com.example.myconnections_android.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.example.myconnections_android.ui.map.animations.LatLngInterpolator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Iterator;


public class GoogleAnimationActivity extends FragmentActivity {
    private SyncedMapFragment mMapFragment;
    private GoogleMap mMap;
    private Marker mMarker;


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
            if (mMarker != null) {
                mMapFragment.animateMarkerToGB(mMarker, point, mLatLngInterpolator, 1500);
            } else {
                mLatLngInterpolator = new LatLngInterpolator.Linear();
                mMarker = mMap.addMarker(new MarkerOptions().position(point));
            }
        }
    };


    public static class SyncedMapFragment extends SupportMapFragment {
        private MapViewWrapper mWrapper;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mWrapper = new MapViewWrapper(getActivity());
            mWrapper.addView(super.onCreateView(inflater, container, savedInstanceState));
            return mWrapper;
        }

        public void animateMarkerToGB(Marker marker, LatLng finalPosition, LatLngInterpolator latLngInterpolator,
                                      long duration) {
            if (mWrapper == null) {
                throw new IllegalStateException("MapFragment view not yet created.");
            }
            mWrapper.animateMarkerToGB(marker, finalPosition, latLngInterpolator, duration);
        }


        public static class MapViewWrapper extends FrameLayout {
            private HashSet<MarkerAnimation> mAnimations = new HashSet<MarkerAnimation>();


            public MapViewWrapper(Context context) {
                super(context);
                setWillNotDraw(false);
            }

            public void animateMarkerToGB(Marker marker, LatLng finalPosition, LatLngInterpolator latLngInterpolator,
                                          long duration) {
                mAnimations.add(new MarkerAnimation(marker, finalPosition, latLngInterpolator, duration));
                invalidate();
            }

            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                boolean shouldPost = false;

                Iterator<MarkerAnimation> iterator = mAnimations.iterator();
                while (iterator.hasNext()) {
                    MarkerAnimation markerAnimation = iterator.next();
                    if (markerAnimation.animate()) {
                        shouldPost = true;
                    } else {
                        iterator.remove();
                    }
                }

                if (shouldPost) {
                    postInvalidateOnAnimation();
                }
            }

            @SuppressLint("NewApi")
            public void postInvalidateOnAnimation() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    super.postInvalidateOnAnimation();
                } else {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
            }


            private static class MarkerAnimation {
                final static Interpolator sInterpolator = new AccelerateDecelerateInterpolator();

                private final Marker mMarker;
                private final LatLngInterpolator mLatLngInterpolator;
                private final LatLng mStartPosition;
                private final LatLng mFinalPosition;
                private final long mDuration;
                private final long mStartTime;


                public MarkerAnimation(Marker marker, LatLng finalPosition, LatLngInterpolator latLngInterpolator,
                                       long duration) {
                    mMarker = marker;
                    mLatLngInterpolator = latLngInterpolator;
                    mStartPosition = marker.getPosition();
                    mFinalPosition = finalPosition;
                    mDuration = duration;
                    mStartTime = AnimationUtils.currentAnimationTimeMillis();
                }

                public boolean animate() {
                    // Calculate progress using interpolator
                    long elapsed = AnimationUtils.currentAnimationTimeMillis() - mStartTime;
                    float t = elapsed / (float) mDuration;
                    float v = sInterpolator.getInterpolation(t);
                    mMarker.setPosition(mLatLngInterpolator.interpolate(v, mStartPosition, mFinalPosition));

                    // Repeat till progress is complete.
                    return (t < 1);
                }

                @Override
                public int hashCode() {
                    // So we only get one animation for the same marker in our HashSet
                    return mMarker.hashCode();
                }

                @Override
                public boolean equals(Object o) {
                    if (o instanceof Marker) {
                        return mMarker.equals(o);
                    }
                    return super.equals(o);
                }
            }
        }
    }
}