package com.sdex.webteb.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.sdex.webteb.R;

import butterknife.InjectView;
import butterknife.OnClick;

public class TermsOfServiceDialog extends BaseDialogFragment {

    @InjectView(R.id.webView)
    WebView content;

    public static TermsOfServiceDialog newInstance() {
        TermsOfServiceDialog frag = new TermsOfServiceDialog();
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        content.loadUrl("https://www.webteb.com/home/termsandconditions?UserID=&DeviceType=&NativeApi=1&utm_campaign=babyapp&utm_source=babyapp&utm_medium=link");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dialog_terms_of_service;
    }

    @OnClick(R.id.cancel)
    public void close(final View v) {
        this.dismiss();
    }

}
