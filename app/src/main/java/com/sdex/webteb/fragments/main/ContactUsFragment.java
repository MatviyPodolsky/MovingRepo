package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sdex.webteb.R;
import com.sdex.webteb.utils.KeyboardUtils;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class ContactUsFragment extends BaseMainFragment {

    @InjectView(R.id.title)
    EditText mTitle;
    @InjectView(R.id.text)
    EditText mText;
    @InjectView(R.id.send)
    Button mSend;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_contact_us;
    }

    @OnClick(R.id.send)
    void send(View v) {
        KeyboardUtils.hideKeyboard(v);
        final String title = mTitle.getText().toString();
        final String text = mText.getText().toString();
    }

}
