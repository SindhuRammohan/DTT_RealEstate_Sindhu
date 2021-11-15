package com.real_estate.realestate_dtt_sindhu.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.real_estate.realestate_dtt_sindhu.DataModel;
import com.real_estate.realestate_dtt_sindhu.GPSTracker;
import com.real_estate.realestate_dtt_sindhu.HouseDetailScreen;
import com.real_estate.realestate_dtt_sindhu.HouseViewModel;
import com.real_estate.realestate_dtt_sindhu.R;
import com.real_estate.realestate_dtt_sindhu.adapter.CustomAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class HouseListFragment extends Fragment implements CustomAdapter.ItemClickListener{
    View rootView;
    RecyclerView listView;
    LinearLayoutCompat emptyText;
    EditText editText;
    ImageView search;
    ImageView close;
    private CustomAdapter adapter;
    HouseViewModel viewModel;
    public HouseListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.houselist_tab, container, false);

        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);




        viewModel = ViewModelProviders.of(getActivityNonNull()).get(HouseViewModel.class);
        viewModel.getUserMutableLiveData().observe(getActivityNonNull(), userListUpdateObserver);



        listView = rootView.findViewById(R.id.simpleListView);
        emptyText = rootView.findViewById(R.id.emptyview);




        editText = rootView.findViewById(R.id.search);
        search = rootView.findViewById(R.id.searchicon);
        close = rootView.findViewById(R.id.closeicon);


        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                if (adapter != null) {
                    adapter.getFilter().filter(text);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().length() != 0) {
                    search.setVisibility(View.GONE);
                    close.setVisibility(View.VISIBLE);
                } else {
                    search.setVisibility(View.VISIBLE);
                    close.setVisibility(View.GONE);
                }


                adapter.notifyDataSetChanged();
            }
        };

        editText.addTextChangedListener(textWatcher);
        editText.clearFocus();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() != null) {
                    getActivity().finish();
                    getActivity().finishAffinity();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivityNonNull(), callback);
        return rootView;

    }


    Observer<ArrayList<DataModel>> userListUpdateObserver = new Observer<ArrayList<DataModel>>() {
        @Override
        public void onChanged(ArrayList<DataModel> userArrayList) {
            adapter = new CustomAdapter(userArrayList, getActivityNonNull());
            listView =  rootView.findViewById(R.id.simpleListView);
            ProgressBar mProgressBar =  rootView.findViewById(R.id.progress_bar);
            mProgressBar.setVisibility(View.GONE);
            listView.setLayoutManager(new LinearLayoutManager(getActivityNonNull()));
            adapter.setClickListener(HouseListFragment.this);
            listView.setAdapter(adapter);

            emptyText = rootView.findViewById(R.id.emptyview);


        }
    };
    protected FragmentActivity getActivityNonNull() {
        if (super.getActivity() != null) {
            return super.getActivity();
        } else {

            throw new RuntimeException("null returned from getActivity()");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String price = ((DataModel) adapter.getItem(position)).getPrice();
        String img = ((DataModel) adapter.getItem(position)).getPicture_path();
        String bed = ((DataModel) adapter.getItem(position)).getBedrooms();
        String bath = ((DataModel) adapter.getItem(position)).getBathroom();
        String size = ((DataModel) adapter.getItem(position)).getSizes();
        String description = ((DataModel) adapter.getItem(position)).getDescription();

        String lat = ((DataModel) adapter.getItem(position)).getLat();
        String longi = ((DataModel) adapter.getItem(position)).getLongi();

        GPSTracker mGPS = new GPSTracker(getContext());
        double dist = adapter.distance(mGPS.getLatitude(), mGPS.getLongitude(), Double.parseDouble(adapter.getItem(position).getLat()), Double.parseDouble(adapter.getItem(position).getLongi()));

        String distance = String.format(Locale.US, "%.2f", dist);


        Intent intent = new Intent(getActivity(), HouseDetailScreen.class);

        intent.putExtra("price", price);
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
}
