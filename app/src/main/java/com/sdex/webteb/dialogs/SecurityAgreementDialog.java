package com.sdex.webteb.dialogs;

import com.sdex.webteb.R;

import butterknife.OnClick;

public class SecurityAgreementDialog extends BaseDialogFragment {

    public static SecurityAgreementDialog newInstance() {
        return new SecurityAgreementDialog();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dialog_security_agreement;
    }

    @OnClick(R.id.cancel)
    public void close() {
        this.dismiss();
    }

}
