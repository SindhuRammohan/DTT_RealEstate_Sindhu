package com.real_estate.realestate_dtt_sindhu.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.real_estate.realestate_dtt_sindhu.R;
import com.real_estate.realestate_dtt_sindhu.services.GPSTracker;

public class HouseDetailActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener {
    private GoogleMap googleMap;
    private String strLatitude;
    private String strLongitude;
    private TextView txtPrice;
    private TextView txtBedroom;
    private TextView txtBathroom;
    private TextView txtLayers;
    private TextView txtDescription;
    private TextView txtDistance;
    private ImageView houseImageView;
    private ImageView back;
    private String img_url;
    GPSTracker mGPS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.detail_house);
        mGPS = new GPSTracker(this);
        init();
        setHouseDetails();
        loadHouseImage();
        displayMap();
        back.setOnClickListener(v -> clickBack());
    }

    private void setHouseDetails() {
        Intent i = getIntent();
        String price = i.getStringExtra(getResources().getString(R.string.price));
        img_url = i.getStringExtra(getResources().getString(R.string.selected_image));
        String bed = i.getStringExtra(getResources().getString(R.string.bed));
        String bath = i.getStringExtra(getResources().getString(R.string.bath));
        String size = i.getStringExtra(getResources().getString(R.string.size));
        String descriptions = i.getStringExtra(getResources().getString(R.string.description_txt));
        String dist = i.getStringExtra(getResources().getString(R.string.distance));
        strLatitude = i.getStringExtra(getResources().getString(R.string.latitude));
        strLongitude = i.getStringExtra(getResources().getString(R.string.longitude));

        txtPrice.setText(price);
        txtBedroom.setText(bed);
        txtBathroom.setText(bath);
        txtLayers.setText(size);
        txtDescription.setText(descriptions);
        txtDistance.setText(dist);
    }

    //To load the image
    private void loadHouseImage() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.dtt_banner)
                .error(R.mipmap.dtt_banner);
        Glide.with(this).load(img_url).apply(options).into(houseImageView);
    }

    //Page Initialization
    private void init() {
        txtPrice = findViewById(R.id.priceDetail);
        txtBedroom = findViewById(R.id.bedroomDetail);
        txtBathroom = findViewById(R.id.batroomDetail);
        txtLayers = findViewById(R.id.layerDetail);
        txtDescription = findViewById(R.id.description);
        txtDistance = findViewById(R.id.distanceDetail);
        houseImageView = findViewById(R.id.houseImageDetail);
        back = findViewById(R.id.back);
    }

    //To display the map
    private void displayMap() {
        MapView mapView = findViewById(R.id.map);
        mapView.onCreate(new Bundle());
        mapView.onResume();
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(mMap -> {
            googleMap = mMap;
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
            if (mGPS.canGetLocation) {
                mGPS.getLocation();
            }
            LatLng currentLocation = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
            BitmapDescriptor defaultMarker =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            googleMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .icon(defaultMarker));

            googleMap.setOnMapClickListener(arg0 -> {
                viewMap();
            });

            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(googleMap))
        {
            viewMap();
        }
        return false;
    }

    private void viewMap() {
        String mapurlfrom = getResources().getString(R.string.mapurlfrom);
        String mapurlto = getResources().getString(R.string.mapurlto);
        String coma = getResources().getString(R.string.coma);
        String mappathfromandto = mapurlfrom + mGPS.getLatitude() + coma + mGPS.getLongitude() + mapurlto + strLatitude + coma + strLongitude;

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(mappathfromandto));
        startActivity(intent);
    }

    private void clickBack() {
        Intent intent = new Intent(HouseDetailActivity.this, HouseOverview.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }
}


