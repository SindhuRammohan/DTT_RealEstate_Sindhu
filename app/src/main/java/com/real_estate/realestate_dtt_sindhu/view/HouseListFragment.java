package com.real_estate.realestate_dtt_sindhu.view;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.real_estate.realestate_dtt_sindhu.R;
import com.real_estate.realestate_dtt_sindhu.adapter.CustomAdapter;
import com.real_estate.realestate_dtt_sindhu.model.HouseDataModel;
import com.real_estate.realestate_dtt_sindhu.model.HouseViewModel;
import com.real_estate.realestate_dtt_sindhu.services.GPSTracker;
import com.real_estate.realestate_dtt_sindhu.services.RecyclerViewEmptySupport;

import java.util.ArrayList;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class HouseListFragment extends Fragment implements CustomAdapter.ItemClickListener {
    View rootView;
    RecyclerViewEmptySupport recyclerViewHouseList;
    ProgressBar progressBar;
    LinearLayoutCompat emptyView;
    EditText etSearch;
    ImageView image_search;
    ImageView image_close;
    HouseViewModel viewModel;
    private CustomAdapter mAdapter;
    final Observer<ArrayList<HouseDataModel>> userListUpdateObserver = new Observer<ArrayList<HouseDataModel>>() {
        @Override
        public void onChanged(ArrayList<HouseDataModel> userArrayList) {
            mAdapter.setHouseList(userArrayList);
            progressBar.setVisibility(GONE);
            recyclerViewHouseList.setVisibility(VISIBLE);
            mAdapter.notifyDataSetChanged();
            mAdapter.setClickListener(HouseListFragment.this);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.houselist_tab, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        recyclerViewHouseList = rootView.findViewById(R.id.recycleviewHouseList);

        emptyView = rootView.findViewById(R.id.emptyView);
        progressBar = rootView.findViewById(R.id.progressBar);
        etSearch = rootView.findViewById(R.id.search);
        image_search = rootView.findViewById(R.id.searchIcon);
        image_close = rootView.findViewById(R.id.closeIcon);

        recyclerViewHouseList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHouseList.setHasFixedSize(true);
        recyclerViewHouseList.setEmptyView(emptyView);
        progressBar.setVisibility(VISIBLE);
        recyclerViewHouseList.setVisibility(GONE);

        viewModel = ViewModelProviders.of(requireActivity()).get(HouseViewModel.class);
        mAdapter = new CustomAdapter();
        recyclerViewHouseList.setAdapter(mAdapter);
        viewModel.getUserMutableLiveData().observe(requireActivity(), userListUpdateObserver);

        editText();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() != null) {
                    closeActivity();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        return rootView;
    }

    private void editText() {
        TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int st, int b, int c) {
            String strSearch = etSearch.getText().toString().toLowerCase(Locale.getDefault());
            if (mAdapter != null) {
                mAdapter.getFilter().filter(strSearch);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int st, int c, int a) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (etSearch.getText().toString().length() != 0) {
                image_search.setVisibility(GONE);
                image_close.setVisibility(VISIBLE);
            } else {
                image_search.setVisibility(VISIBLE);
                image_close.setVisibility(GONE);
            }
        }
    };

        etSearch.addTextChangedListener(textWatcher);
        etSearch.clearFocus();
        image_close.setOnClickListener(v -> etSearch.setText(""));
    }

    private void closeActivity() {
        getActivity().finish();
        getActivity().finishAffinity();
    }

    @Override
    public void onItemClick(View view, int position) {
        String strPrice = mAdapter.currencyFormat(mAdapter.getItem(position).getPrice());
        String strHouseImg = mAdapter.getItem(position).getPicture_path();
        String strBed = mAdapter.getItem(position).getBedrooms();
        String strBath = mAdapter.getItem(position).getBathroom();
        String strSize = mAdapter.getItem(position).getSizes();
        String strDescription = mAdapter.getItem(position).getDescription();
        String strlat = mAdapter.getItem(position).getLat();
        String strlongi = mAdapter.getItem(position).getLongi();

        GPSTracker mGps = new GPSTracker(getContext());
        String dDist = mAdapter.distance(mGps.getLatitude(), mGps.getLongitude(), Double.parseDouble(mAdapter.getItem(position).getLat()), Double.parseDouble(mAdapter.getItem(position).getLongi()));


        final Intent intent = new Intent(getActivity(), HouseDetailActivity.class);

        intent.putExtra(getResources().getString(R.string.price), strPrice);
        intent.putExtra(getResources().getString(R.string.selected_image), strHouseImg);
        intent.putExtra(getResources().getString(R.string.bed), strBed);
        intent.putExtra(getResources().getString(R.string.bath), strBath);
        intent.putExtra(getResources().getString(R.string.size), strSize);
        intent.putExtra(getResources().getString(R.string.description_txt), strDescription);
        intent.putExtra(getResources().getString(R.string.distance), dDist);
        intent.putExtra(getResources().getString(R.string.latitude), strlat);
        intent.putExtra(getResources().getString(R.string.longitude), strlongi);

        startActivity(intent);
        requireActivity().finish();

    }
}