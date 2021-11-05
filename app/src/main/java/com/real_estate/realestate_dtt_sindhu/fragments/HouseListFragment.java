package com.real_estate.realestate_dtt_sindhu.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.real_estate.realestate_dtt_sindhu.DataModel;
import com.real_estate.realestate_dtt_sindhu.GPSTracker;
import com.real_estate.realestate_dtt_sindhu.R;
import com.real_estate.realestate_dtt_sindhu.adapter.CustomAdapter;
import com.real_estate.realestate_dtt_sindhu.houseSortByPrice;
import com.real_estate.realestate_dtt_sindhu.houseDetailScreen;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class HouseListFragment extends Fragment {

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;
    private GoogleMap googleMap;
    private MapView mapView;
    private static final int LOCATION_REQUEST_CODE = 101;


    ArrayList<String> ListViewClickItemArrayId = new ArrayList<String>();
    ArrayList<String> ListViewClickItemArrayprice = new ArrayList<String>();
    ArrayList<String> ListViewClickItemArrayImagepath = new ArrayList<String>();
    ArrayList<String> ListViewClickItemArrayBedroom = new ArrayList<String>();
    ArrayList<String> ListViewClickItemArraybathrooms = new ArrayList<String>();
    ArrayList<String> ListViewClickItemArraysize = new ArrayList<String>();
    ArrayList<String> ListViewClickItemArraydescription = new ArrayList<String>();
    String TempHolder ;
    EditText editText;
    ImageView search;
    ImageView close;


    private final static String URL = "https://intern.docker-dev.d-tt.nl/api/house";


    private final static String imageURL = "https://intern.docker-dev.d-tt.nl";

    public HouseListFragment() {
        // Required empty public constructor
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0

            );
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.houselist_tab, container, false);
        dataModels= new ArrayList<>();

        hideSoftKeyboard(getActivity());
        new FetchDataTask().execute(URL);
        listView = (ListView) rootView.findViewById(R.id.simpleListView);
        LinearLayout emptyText = (LinearLayout)rootView.findViewById(R.id.emptyview);
        listView.setEmptyView(emptyText);
        editText = (EditText) rootView.findViewById(R.id.search);
        search = (ImageView) rootView.findViewById(R.id.searchicon);
        close = (ImageView) rootView.findViewById(R.id.closeicon);
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            {
                String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                HouseListFragment.this.adapter.getFilter().filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(editText.getText().toString().length() != 0) {
                    search.setVisibility(View.GONE);
                    close.setVisibility(View.VISIBLE);
                }
                else {
                    search.setVisibility(View.VISIBLE);
                    close.setVisibility(View.GONE);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                    HouseListFragment.this.adapter.getFilter().filter(text);
                    hideSoftKeyboard(getActivity());
                    editText.setText("");
                    return true;
                }
                return false;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });



        mapView = (MapView) rootView.findViewById(R.id.mapf);
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
                googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
                requestPermissionLauncher.launch(
                            Manifest.permission.ACCESS_FINE_LOCATION);

                if(mGPS.canGetLocation() ){
                    mGPS.getLocation();
                }else{
                    System.out.println("Unable");
                }
                //To add marker
                LatLng currentLocation = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());

                CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        return rootView;
        // Inflate the layout for this fragment

    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    getActivity().finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });


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

    private class FetchDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            InputStream inputStream = null;
            String result= null;
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);
            httpGet.setHeader("Access-Key","98bww4ezuzfePCYFxJEWyszbUXc7dxRx");
            try {
                HttpResponse response = client.execute(httpGet);
                inputStream = response.getEntity().getContent();

                // convert inputstream to string
                if(inputStream != null){
                    result = convertInputStreamToString(inputStream);
                }
                else
                    result = "Failed to fetch data";

                return result;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String dataFetched) {
            //parse the JSON data and then display
            parseJSON(dataFetched);
        }


        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

        private void parseJSON(String data){

            try{
                JSONArray jsonMainNode = new JSONArray(data);
                for(int i=0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String id = jsonChildNode.getString("id");
                    String price = jsonChildNode.getString("price");
                    String address = jsonChildNode.getString("zip");
                    String city = jsonChildNode.getString("city");
                    String bed = jsonChildNode.getString("bedrooms");
                    String bath = jsonChildNode.getString("bathrooms");
                    String size = jsonChildNode.getString("size");
                    String path = jsonChildNode.getString("image");
                    String description = jsonChildNode.getString("description");


                    String lat = jsonChildNode.getString("latitude");
                    String longi = jsonChildNode.getString("longitude");

                    GPSTracker mGPS = new GPSTracker(getContext());
                    double distance = distance(mGPS.getLatitude(),mGPS.getLongitude(),Double.parseDouble(lat),Double.parseDouble(longi));

                    dataModels.add(new DataModel(price, address+city, bed,bath,size,imageURL+path,description,distance,lat,longi));

                    Collections.sort(dataModels,new houseSortByPrice());


                    //Getting json Array item into String.
                    TempHolder = jsonMainNode.optString(i);

                    //Adding selected item into Array List .
                    ListViewClickItemArrayId.add(jsonChildNode.getString("id"));
                    ListViewClickItemArrayprice.add(jsonChildNode.getString("price"));
                    ListViewClickItemArrayImagepath.add(jsonChildNode.getString("image"));
                    ListViewClickItemArrayBedroom.add(jsonChildNode.getString("bedrooms"));
                    ListViewClickItemArraybathrooms.add(jsonChildNode.getString("bathrooms"));
                    ListViewClickItemArraysize.add(jsonChildNode.getString("size"));
                    ListViewClickItemArraydescription.add(jsonChildNode.getString("description"));
                }


                adapter= new CustomAdapter(dataModels,getActivity());

                Collections.sort(ListViewClickItemArrayprice, new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        return extractInt(o1) - extractInt(o2);
                    }

                    int extractInt(String s) {
                        String num = s.replaceAll("\\D", "");
                        // return 0 if no digits found
                        return num.isEmpty() ? 0 : Integer.parseInt(num);
                    }
                });



                listView.setAdapter(adapter);

                listView.setTextFilterEnabled(true);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView adapterView, View view, int i, long l) {

                        String price  = (String) ((DataModel) listView.getItemAtPosition(i)).getPrice();
                        String img  = (String) ((DataModel) listView.getItemAtPosition(i)).getPicture_path();
                        String bed  = (String) ((DataModel) listView.getItemAtPosition(i)).getBedrooms();
                        String bath  = (String) ((DataModel) listView.getItemAtPosition(i)).getBathroom();
                        String size  =(String) ((DataModel) listView.getItemAtPosition(i)).getSizes();
                        String description  = (String) ((DataModel) listView.getItemAtPosition(i)).getDescription();

                        String lat  =(String) ((DataModel) listView.getItemAtPosition(i)).getLat();
                        String longi  = (String) ((DataModel) listView.getItemAtPosition(i)).getLongi();


                        double dist = (double) ((DataModel) listView.getItemAtPosition(i)).getdistance();
                        String distance = String.format("%.2f", dist);



                        Intent intent = new Intent(getActivity(), houseDetailScreen.class);

                        intent.putExtra("price",price);
                        intent.putExtra("selected_image", img);
                        intent.putExtra("bed", bed);
                        intent.putExtra("bath", bath);
                        intent.putExtra("size", size);
                        intent.putExtra("description", description);
                        intent.putExtra("distance", distance);

                        intent.putExtra("lat", lat);
                        intent.putExtra("longi", longi);



                        startActivity(intent);
                        getActivity().finish();

                    }
                });


            }catch(Exception e){

            }
        }
    }


}
