package com.sdex.webteb.dialogs;

import com.sdex.webteb.R;

import butterknife.OnClick;

public class TermsOfServiceDialog extends BaseDialogFragment {

    public static TermsOfServiceDialog newInstance() {
        return new TermsOfServiceDialog();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dialog_terms_of_service;
    }

    @OnClick(R.id.cancel)
    public void close() {
        this.dismiss();
    }

}
