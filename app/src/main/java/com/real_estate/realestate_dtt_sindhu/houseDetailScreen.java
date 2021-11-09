package com.real_estate.realestate_dtt_sindhu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class HouseDetailScreen extends AppCompatActivity {


    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getActionBar().hide();
        }
        catch (NullPointerException ignored){}
        setContentView(R.layout.detail_house);
        TextView textView = findViewById(R.id.pricedetail);
        TextView no_of_bedroom = findViewById(R.id.bedroomdetail);
        TextView bathroom = findViewById(R.id.batroomdetail);
        TextView layers = findViewById(R.id.layerdetail);
        TextView description = findViewById(R.id.description);
        TextView distance = findViewById(R.id.distancedetail);
        ImageView houseImageView = findViewById(R.id.houseimagedetail);
        ImageView back = findViewById(R.id.back);


        Intent i = getIntent();

        String price = i.getStringExtra("price");
        String img_url = i.getStringExtra("selected_image");
        String bed = i.getStringExtra("bed");
        String bath = i.getStringExtra("bath");
        String size = i.getStringExtra("size");
        String descriptions = i.getStringExtra("description");
        String dist = i.getStringExtra("distance");
        String lat = i.getStringExtra("lat");
        String longi = i.getStringExtra("longi");


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.dtt_banner)
                .error(R.mipmap.dtt_banner);
        Glide.with(this).load(img_url).apply(options).into(houseImageView);



        textView.setText(getResources().getString(R.string.currency,price) );
        no_of_bedroom.setText(bed);
        bathroom.setText(bath);
        layers.setText(size);
        description.setText(descriptions);
        distance.setText(dist);


        MapView mapView = findViewById(R.id.map);
        GPSTracker mGPS = new GPSTracker(this);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                if ( ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                }

                if(mGPS.canGetLocation ){
                    mGPS.getLocation();
                }
                LatLng currentLocation = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HouseDetailScreen.this, HouseOverview.class);
                        startActivity(intent);
                        finish();

                    }
                });
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(@NonNull LatLng arg0) {


                        String mapurlfrom = getResources().getString(R.string.mapurlfrom);
                        String mapurlto = getResources().getString(R.string.mapurlto);
                        String coma = getResources().getString(R.string.coma);
                        String mappathfromandto = mapurlfrom + mGPS.getLatitude()+coma+mGPS.getLongitude()+mapurlto+lat+coma+longi;

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(mappathfromandto));
                        startActivity(intent);
                    }
                });


               CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HouseDetailScreen.this, HouseOverview.class);
        startActivity(intent);
        finish();
    }

}


