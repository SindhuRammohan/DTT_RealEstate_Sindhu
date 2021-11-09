package com.real_estate.realestate_dtt_sindhu.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
import com.real_estate.realestate_dtt_sindhu.Api;
import com.real_estate.realestate_dtt_sindhu.DataModel;
import com.real_estate.realestate_dtt_sindhu.GPSTracker;
import com.real_estate.realestate_dtt_sindhu.HouseDetailScreen;
import com.real_estate.realestate_dtt_sindhu.R;
import com.real_estate.realestate_dtt_sindhu.RetrofitClient;
import com.real_estate.realestate_dtt_sindhu.adapter.CustomAdapter;
import com.real_estate.realestate_dtt_sindhu.HouseSortByPrice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseListFragment extends Fragment {

    ArrayList<DataModel> dataModels;
    ListView listView;
    private CustomAdapter adapter;

    EditText editText;
    ImageView search;
    ImageView close;




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

        hideSoftKeyboard(getActivityNonNull());

        Call<List<DataModel>> call = RetrofitClient.getInstance().getMyApi().getHouseList();
        String imageurl = Api.BASE_URL;
        call.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<DataModel>> call, @NonNull Response<List<DataModel>> response) {

                List<DataModel> houseList = response.body();

                for (int i = 0; i < Objects.requireNonNull(houseList).size(); i++) {
                    GPSTracker mGPS = new GPSTracker(getContext());
                    double distance = distance(mGPS.getLatitude(),mGPS.getLongitude(),Double.parseDouble(houseList.get(i).getLat()),Double.parseDouble(houseList.get(i).getLongi()));
                    houseList.get(i).setdistance(distance);

                    dataModels.add(new DataModel(
                            houseList.get(i).getPrice(),
                            houseList.get(i).getZip(),
                            houseList.get(i).getCity(),
                            houseList.get(i).getBedrooms(),
                            houseList.get(i).getBathroom(),
                            houseList.get(i).getSizes(),
                            imageurl + houseList.get(i).getPicture_path(),
                            houseList.get(i).getDescription(),
                            houseList.get(i).getdistance(),
                            houseList.get(i).getLat(),
                            houseList.get(i).getLongi()));

                    Collections.sort(dataModels,new HouseSortByPrice());
                }
                adapter= new CustomAdapter(dataModels,getActivity());


                listView.setAdapter(adapter);


            }

            @Override
            public void onFailure(@NonNull Call<List<DataModel>> call, @NonNull Throwable t) {
                //handle error or failure cases here
            }
        });


        listView = rootView.findViewById(R.id.simpleListView);
        LinearLayout emptyText = rootView.findViewById(R.id.emptyview);
        listView.setEmptyView(emptyText);
        editText = rootView.findViewById(R.id.search);
        search = rootView.findViewById(R.id.searchicon);
        close = rootView.findViewById(R.id.closeicon);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {

                String price  = ((DataModel) listView.getItemAtPosition(i)).getPrice();
                String img  = ((DataModel) listView.getItemAtPosition(i)).getPicture_path();
                String bed  = ((DataModel) listView.getItemAtPosition(i)).getBedrooms();
                String bath  = ((DataModel) listView.getItemAtPosition(i)).getBathroom();
                String size  = ((DataModel) listView.getItemAtPosition(i)).getSizes();
                String description  = ((DataModel) listView.getItemAtPosition(i)).getDescription();

                String lat  = ((DataModel) listView.getItemAtPosition(i)).getLat();
                String longi  = ((DataModel) listView.getItemAtPosition(i)).getLongi();


                double dist = ((DataModel) listView.getItemAtPosition(i)).getdistance();
                String distance = String.format(Locale.US,"%.2f", dist);



                Intent intent = new Intent(getActivity(), HouseDetailScreen.class);

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
                getActivityNonNull().finish();

            }
        });

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c)
            {
                String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                if(adapter!=null) {
                    adapter.getFilter().filter(text);
                }
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
                    if(adapter!=null) {
                        adapter.getFilter().filter(text);
                    }
                    hideSoftKeyboard(getActivityNonNull());
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

       OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(getActivity()!=null) {
                    getActivity().finish();
                    getActivity().finishAffinity();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivityNonNull(), callback);
        return rootView;

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
    protected FragmentActivity getActivityNonNull() {
        if (super.getActivity() != null) {
            return super.getActivity();
        } else {
            throw new RuntimeException("null returned from getActivity()");
        }
    }
}
