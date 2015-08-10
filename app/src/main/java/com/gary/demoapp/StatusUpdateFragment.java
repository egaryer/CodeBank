package com.gary.demoapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.share.widget.LikeView;

public class StatusUpdateFragment extends Fragment {
    TextView loginHint;
    LikeView likeView;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StatusUpdateFragment newInstance(int sectionNumber) {
        StatusUpdateFragment fragment = new StatusUpdateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public StatusUpdateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statusupdate, container, false);
        WebView webView = (WebView) rootView.findViewById(R.id.webView);
        webView.loadUrl("http://www.linkwish.com/info");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        loginHint = (TextView) rootView.findViewById(R.id.tv_login);
        likeView = (LikeView) rootView.findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        likeView.setObjectIdAndType(
                "http://www.linkwish.com/info",
                LikeView.ObjectType.OPEN_GRAPH);
        refreshButtonsState();
        return rootView;
    }

    private void refreshButtonsState() {
        if (AccessToken.getCurrentAccessToken() == null) {
            loginHint.setVisibility(View.VISIBLE);
            likeView.setVisibility(View.GONE);
        } else {
            loginHint.setVisibility(View.GONE);
            likeView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}