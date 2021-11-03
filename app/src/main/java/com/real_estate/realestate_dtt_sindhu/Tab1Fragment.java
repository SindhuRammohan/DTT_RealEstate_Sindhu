package com.real_estate.realestate_dtt_sindhu;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class Tab1Fragment extends Fragment {

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;



    private GoogleMap googleMap;
    private MapView mapView;
    private static final int LOCATION_REQUEST_CODE = 101;


    ArrayList<String> ListViewClickItemArray = new ArrayList<String>();
    ArrayList<String> ListViewClickItemArrayprice = new ArrayList<String>();
    ArrayList<String> ListViewClickItemArraypath = new ArrayList<String>();
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

    public Tab1Fragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);
        dataModels= new ArrayList<>();

        hideSoftKeyboard(getActivity());
        new FetchDataTask().execute(URL);
        listView = (ListView) rootView.findViewById(R.id.simpleListView);
        editText = (EditText) rootView.findViewById(R.id.search);
        search = (ImageView) rootView.findViewById(R.id.searchicon);
        close = (ImageView) rootView.findViewById(R.id.closeicon);
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            {
                adapter.getFilter().filter(s.toString());
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
//                    performSearch();
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



        return rootView;
        // Inflate the layout for this fragment

    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.i("App", "kl" );

                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    Log.i("App", "kli" );
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
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
                    Log.i("App", "Data received:" +result);

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
                    String postTitle = jsonChildNode.getString("price");
                    String address = jsonChildNode.getString("zip");
                    String city = jsonChildNode.getString("city");
                    String bed = jsonChildNode.getString("bedrooms");
                    String bath = jsonChildNode.getString("bathrooms");
                    String size = jsonChildNode.getString("size");
                    String path = jsonChildNode.getString("image");


                    dataModels.add(new DataModel(postTitle, address+city, bed,bath,size,imageURL+path));

                    Collections.sort(dataModels,new NameSorter());


                    //Getting json Array item into String.
                    TempHolder = jsonMainNode.optString(i);

                    //Adding selected item into Array List .
                    ListViewClickItemArray.add(jsonChildNode.getString("id"));
                    ListViewClickItemArrayprice.add(jsonChildNode.getString("price"));
                    ListViewClickItemArraypath.add(jsonChildNode.getString("image"));
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
//                Toast.makeText(getApplicationContext(),fruitNames[i],Toast.LENGTH_LONG).show();
                        ListdataActivity selectedFragment = null;
                        selectedFragment = new ListdataActivity();


                        String t  = ListViewClickItemArray.get(i).toString();
                        String price  = ListViewClickItemArrayprice.get(i).toString();
                        String img  = ListViewClickItemArraypath.get(i).toString();
                        String bed  = ListViewClickItemArrayBedroom.get(i).toString();
                        String bath  = ListViewClickItemArraybathrooms.get(i).toString();
                        String size  = ListViewClickItemArraysize.get(i).toString();
                        String description  = ListViewClickItemArraydescription.get(i).toString();


                        Bundle args = new Bundle();
                        args.putString("Key",t);
                        args.putString("price",price);
                        args.putString("selected_image", imageURL+img);
                        args.putString("bed", bed);
                        args.putString("bath", bath);
                        args.putString("size", size);
                        args.putString("description", description);

                        Log.i("App", "Data imgimg:"+imageURL+img );
                        selectedFragment .setArguments(args);


                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();

                        Log.i("App", "Data hhhdsgs:" );
                    }
                });


            }catch(Exception e){
                Log.i("App", "Error parsing data" +e.getMessage());

            }
        }
    }


}
