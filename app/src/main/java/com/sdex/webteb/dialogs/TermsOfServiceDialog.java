package com.sdex.webteb.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sdex.webteb.R;

import butterknife.OnClick;

public class TermsOfServiceDialog extends BaseDialogFragment {

    public static final int RESOURCE = R.layout.dialog_terms_of_service;

    public static TermsOfServiceDialog newInstance() {
        TermsOfServiceDialog frag = new TermsOfServiceDialog();
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return RESOURCE;
    }

    @OnClick(R.id.cancel)
    public void close(final View v){
        this.dismiss();
    }

}
