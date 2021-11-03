package com.real_estate.realestate_dtt_sindhu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

public class ListdataActivity  extends Fragment {

    public ImageLoader imageLoader;
    private GoogleMap googleMap;
    private MapView mapView;
    private static final int LOCATION_REQUEST_CODE = 101;
    public ListdataActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_house, container, false);
        TextView textView = (TextView)view.findViewById(R.id.pricenew);


        TextView no_of_beedroom = (TextView)view.findViewById(R.id.no_of_beedroomnew);

        TextView bathroom = (TextView)view.findViewById(R.id.bathroomnew);
        TextView layersnew = (TextView)view.findViewById(R.id.layersnew);
        TextView descriptionn = (TextView)view.findViewById(R.id.hhnew);


        ImageView ii = (ImageView)view.findViewById(R.id.bfe);
        String key = getArguments().getString("Key");
        String price = getArguments().getString("price");
        String img = getArguments().getString("selected_image");
        String bed = getArguments().getString("bed");
        String bath = getArguments().getString("bath");
        String size = getArguments().getString("size");
        String description = getArguments().getString("description");

        imageLoader = new ImageLoader(getContext());

        imageLoader.DisplayImage(img, ii);
        textView.setText(getResources().getString(R.string.currency) + price);
        no_of_beedroom.setText(bed);
        bathroom.setText(bath);
        layersnew.setText(size);
        descriptionn.setText(description);



        mapView = (MapView) view.findViewById(R.id.map);
        GPSTracker mGPS = new GPSTracker(getContext());
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        LOCATION_REQUEST_CODE);
                if(mGPS.canGetLocation ){
                    mGPS.getLocation();
                    Log.i("App", "mGPS.getLatitude()" +mGPS.getLatitude());
                    Log.i("App", "mGPS.getLatitude()" +mGPS.getLongitude());
                    Log.i("App", "mGPS.getLatitude()" +mGPS.getLocation());
                }else{
                    System.out.println("Unable");
                }
                //To add marker
                LatLng sydney = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());

                Location locationA = new Location("point A");

                locationA.setLatitude(mGPS.getLatitude());
                locationA.setLongitude(mGPS.getLongitude());

                Location locationB = new Location("point B");

                locationB.setLatitude(52.401339525407664);
                locationB.setLongitude(4.916404960509914);

                double distance = distance(mGPS.getLatitude(),mGPS.getLongitude(),52.401339525407664,4.916404960509914);

                Log.i("App", "mGPS.getLatitude() ding" +mGPS.getLatitude());
                Log.i("App", "mGPS.getLatitude() ding" +mGPS.getLongitude());
                Log.i("App", "mGPS.getLatitude() distance" +distance);

                googleMap.addMarker(new MarkerOptions().position(sydney).title("Title").snippet("Marker Description"));
                // For zooming functionality
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return view;
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist*1.609344);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    protected void requestPermission(String permissionType, int
            requestCode) {
        int permission = ContextCompat.checkSelfPermission(getContext(),
                permissionType);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permissionType}, requestCode
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}