package com.real_estate.realestate_dtt_sindhu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.real_estate.realestate_dtt_sindhu.HouseOverview;
import com.real_estate.realestate_dtt_sindhu.R;

import java.util.Objects;

public class AboutTabFragment extends Fragment {
    TextView tv_Websites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_tab, container, false);
        tv_Websites = rootView.findViewById(R.id.dtt_website);
        tv_Websites.setMovementMethod(new ScrollingMovementMethod());
        setupHyperlink();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity().getApplication(), HouseOverview.class);
                    startActivity(intent);
                }
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        return rootView;
    }

    private void setupHyperlink() {
        tv_Websites.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
