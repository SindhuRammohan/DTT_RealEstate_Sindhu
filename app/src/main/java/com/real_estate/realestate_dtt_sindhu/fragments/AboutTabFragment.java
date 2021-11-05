package com.real_estate.realestate_dtt_sindhu.fragments;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                getActivity().finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        return rootView;
    }

    private void setupHyperlink() {
        websitelink.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
