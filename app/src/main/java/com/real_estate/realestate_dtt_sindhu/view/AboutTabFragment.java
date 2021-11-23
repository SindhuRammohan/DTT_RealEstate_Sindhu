package com.real_estate.realestate_dtt_sindhu.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.real_estate.realestate_dtt_sindhu.R;

public class AboutTabFragment extends Fragment {
    TextView tv_Websites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_tab, container, false);
        tv_Websites = rootView.findViewById(R.id.dttWebsite);
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
        Spannable s = new SpannableString(tv_Websites.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        tv_Websites.setText(s);
        tv_Websites.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
