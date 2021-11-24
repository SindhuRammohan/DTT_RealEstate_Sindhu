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
import com.real_estate.realestate_dtt_sindhu.adapter.CustomAdapter;
import com.real_estate.realestate_dtt_sindhu.model.HouseDataModel;
import com.real_estate.realestate_dtt_sindhu.services.Constants;
import com.real_estate.realestate_dtt_sindhu.services.GPSTracker;

public class HouseDetailActivity extends AppCompatActivity {
    GPSTracker gps;
    private GoogleMap googleMap;
    private String latitude;
    private String longitude;
    private TextView txtPrice;
    private TextView txtBedroom;
    private TextView txtBathroom;
    private TextView txtLayers;
    private TextView txtDescription;
    private TextView txtDistance;
    private ImageView imgHouseImageView;
    private ImageView imgBack;
    private String imgUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.detail_house);
        gps = new GPSTracker(this);
        init();
        setHouseDetails();
        loadHouseImage();
        displayMap();
        imgBack.setOnClickListener(v -> clickBack());
    }

    /**
     * Recieved the clicked house deatils and set the valued to the view
     */
    private void setHouseDetails() {
        Bundle data = getIntent().getExtras();
        HouseDataModel houseDetail = (HouseDataModel) data.getParcelable(Constants.KEY);
        imgUrl = houseDetail.getPicturePath();
        txtPrice.setText(CustomAdapter.currencyFormat(houseDetail.getPrice()));
        txtBedroom.setText(houseDetail.getBedrooms());
        txtBathroom.setText(houseDetail.getBathroom());
        txtLayers.setText(houseDetail.getSizes());
        txtDescription.setText(houseDetail.getDescription());
        latitude = houseDetail.getLatitude();
        longitude = houseDetail.getLongitude();
        CustomAdapter adapter = new CustomAdapter();
        GPSTracker mGps = new GPSTracker(this);
        String distance = adapter.distance(this,
                mGps.getLatitude(),
                mGps.getLongitude(),
                Double.parseDouble(houseDetail.getLatitude()),
                Double.parseDouble(houseDetail.getLongitude())
        );
        txtDistance.setText(distance);
    }

    //To load the image
    private void loadHouseImage() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.dtt_banner)
                .error(R.mipmap.dtt_banner);
        Glide.with(this).load(imgUrl).apply(options).into(imgHouseImageView);
    }

    //Page Initialization
    private void init() {
        txtPrice = findViewById(R.id.priceDetail);
        txtBedroom = findViewById(R.id.bedroomDetail);
        txtBathroom = findViewById(R.id.batroomDetail);
        txtLayers = findViewById(R.id.layerDetail);
        txtDescription = findViewById(R.id.description);
        txtDistance = findViewById(R.id.distanceDetail);
        imgHouseImageView = findViewById(R.id.houseImageDetail);
        imgBack = findViewById(R.id.back);
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
            if (gps.canGetLocation) {
                gps.getLocation();
            }
            LatLng currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
            BitmapDescriptor defaultMarker =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            googleMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .icon(defaultMarker));

            googleMap.setOnMapClickListener(arg0 -> {
                viewMap();
            });
            // adding on click listener to marker of google maps.
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // on marker click we are getting the title of our marker
                    // which is clicked and displaying it in a toast message.
                    if (marker.equals(googleMap)) {
                        viewMap();
                    }
                    return false;
                }
            });
            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });
    }


    // View the path on the system map, when the marker is clicked
    private void viewMap() {
        String mapurlfrom = getResources().getString(R.string.mapurlfrom);
        String mapurlto = getResources().getString(R.string.mapurlto);
        String coma = getResources().getString(R.string.coma);
        String mappathfromandto = mapurlfrom + gps.getLatitude() + coma + gps.getLongitude() + mapurlto + latitude + coma + longitude;

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(mappathfromandto));
        startActivity(intent);
    }

    private void clickBack() {
        Intent intent = new Intent(HouseDetailActivity.this, HouseOverviewActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }
}


