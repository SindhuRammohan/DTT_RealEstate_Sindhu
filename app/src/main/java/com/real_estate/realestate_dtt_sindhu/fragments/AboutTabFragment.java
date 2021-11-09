package com.real_estate.realestate_dtt_sindhu.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.real_estate.realestate_dtt_sindhu.HouseOverview;
import com.real_estate.realestate_dtt_sindhu.R;

public class AboutTabFragment extends Fragment {
    TextView websitelink;
    public AboutTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.about_tab, container, false);
        websitelink = (TextView) rootView.findViewById(R.id.dtt_website);
        websitelink.setMovementMethod(new ScrollingMovementMethod());
        setupHyperlink();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(getActivity()!=null) {
                    Intent intent = new Intent(getActivity().getApplication(), HouseOverview.class);
                    startActivity(intent);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        return rootView;
    }

    private void setupHyperlink() {
        websitelink.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
