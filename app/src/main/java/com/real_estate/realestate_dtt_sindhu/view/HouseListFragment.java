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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.real_estate.realestate_dtt_sindhu.R;
import com.real_estate.realestate_dtt_sindhu.adapter.CustomAdapter;
import com.real_estate.realestate_dtt_sindhu.model.HouseDataModel;
import com.real_estate.realestate_dtt_sindhu.model.HouseRepository;
import com.real_estate.realestate_dtt_sindhu.model.HouseViewModel;
import com.real_estate.realestate_dtt_sindhu.services.Constants;
import com.real_estate.realestate_dtt_sindhu.services.GPSTracker;
import com.real_estate.realestate_dtt_sindhu.services.RecyclerViewEmptySupport;

import java.util.ArrayList;
import java.util.Locale;

public class HouseListFragment extends Fragment implements CustomAdapter.ItemClickListener {
    View rootView;
    RecyclerViewEmptySupport recyclerViewHouseList;
    ProgressBar progressBar;
    LinearLayoutCompat emptyView;
    EditText etSearch;
    ImageView imgSearch;
    ImageView imgClose;
    HouseViewModel viewModel;
    private CustomAdapter adapter;
    final Observer<ArrayList<HouseDataModel>> houseListUpdateObserver =
            new Observer<ArrayList<HouseDataModel>>() {
        @Override
        public void onChanged(ArrayList<HouseDataModel> houseArrayList) {
            adapter.setHouseList(houseArrayList);
            recyclerViewHouseList.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            adapter.setClickListener(HouseListFragment.this);
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
        imgSearch = rootView.findViewById(R.id.searchIcon);
        imgClose = rootView.findViewById(R.id.closeIcon);

        recyclerViewHouseList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewHouseList.setHasFixedSize(true);
        recyclerViewHouseList.setEmptyView(emptyView);
        recyclerViewHouseList.setVisibility(View.GONE);

        viewModel = ViewModelProviders.of(requireActivity()).get(HouseViewModel.class);
        adapter = new CustomAdapter();
        recyclerViewHouseList.setAdapter(adapter);
        viewModel.getUserMutableLiveData().observe(requireActivity(), houseListUpdateObserver);

        editText();

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
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

    /**
     * Receives the text typed in the Edit text
     * Performs the search during editing the text
     * Search option will be disabled if the text is empty.
     */
    private void editText() {
        TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence search, int start, int before, int count) {
            String searchText = etSearch.getText().toString().toLowerCase(Locale.getDefault());
            if (adapter != null) {
                adapter.getFilter().filter(searchText);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence search, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable search) {
            if (etSearch.getText().toString().length() != 0) {
                imgSearch.setVisibility(View.GONE);
                imgClose.setVisibility(View.VISIBLE);
            } else {
                imgSearch.setVisibility(View.VISIBLE);
                imgClose.setVisibility(View.GONE);
            }
        }
    };
        etSearch.addTextChangedListener(textWatcher);
        etSearch.clearFocus();
        imgClose.setOnClickListener(v -> etSearch.setText(""));
    }

    private void closeActivity() {
        getActivity().finish();
        getActivity().finishAffinity();
    }

    @Override
    public void onItemClick(View view, int position) {

        final Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
        intent.putExtra(Constants.KEY, new HouseDataModel(
                adapter.getItem(position).getPrice(),
                adapter.getItem(position).getZip(),
                adapter.getItem(position).getCity(),
                adapter.getItem(position).getBedrooms(),
                adapter.getItem(position).getBathroom(),
                adapter.getItem(position).getSizes(),
                adapter.getItem(position).getPicturePath(),
                adapter.getItem(position).getDescription(),
                adapter.getItem(position).getLatitude(),
                adapter.getItem(position).getLongitude()));

        startActivity(intent);
        requireActivity().finish();
    }
}