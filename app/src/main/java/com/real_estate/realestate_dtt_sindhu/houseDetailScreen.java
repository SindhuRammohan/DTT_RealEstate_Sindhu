package com.real_estate.realestate_dtt_sindhu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
    private MapView mapView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.detail_house);
        TextView textView = (TextView)findViewById(R.id.pricedetail);
        TextView no_of_beedroom = (TextView)findViewById(R.id.bedroomdetail);
        TextView bathroom = (TextView)findViewById(R.id.batroomdetail);
        TextView layersnew = (TextView)findViewById(R.id.layerdetail);
        TextView descriptionn = (TextView)findViewById(R.id.description);
        TextView distance = (TextView)findViewById(R.id.distancedetail);
        ImageView houseImageView = (ImageView)findViewById(R.id.houseimagedetail);
        ImageView back = (ImageView)findViewById(R.id.back);


        Intent i = getIntent();

        String price = i.getStringExtra("price");
        String img_url = i.getStringExtra("selected_image");
        String bed = i.getStringExtra("bed");
        String bath = i.getStringExtra("bath");
        String size = i.getStringExtra("size");
        String description = i.getStringExtra("description");
        String dist = i.getStringExtra("distance");
        String lat = i.getStringExtra("lat");
        String longi = i.getStringExtra("longi");


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.dtt_banner)
                .error(R.mipmap.dtt_banner);
        Glide.with(this).load(img_url).apply(options).into(houseImageView);



        textView.setText(getResources().getString(R.string.currency) + price);
        no_of_beedroom.setText(bed);
        bathroom.setText(bath);
        layersnew.setText(size);
        descriptionn.setText(description);
        distance.setText(dist);


        mapView = (MapView) findViewById(R.id.map);
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
            public void onMapReady(GoogleMap mMap) {
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
                    public void onMapClick(LatLng arg0) {


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


