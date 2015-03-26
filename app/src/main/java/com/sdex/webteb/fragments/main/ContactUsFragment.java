package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sdex.webteb.R;
import com.sdex.webteb.internal.events.SelectMenuItemEvent;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.ContactUsRequest;
import com.sdex.webteb.utils.KeyboardUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.client.Response;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_contact_us);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTitle.setError(null);
            }
        });
        mText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mText.setError(null);
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_contact_us;
    }

    @OnClick(R.id.send)
    void send(View v) {
        if (isValidData()) {
            KeyboardUtils.hideKeyboard(v);
            final String title = mTitle.getText().toString();
            final String text = mText.getText().toString();
            ContactUsRequest request = new ContactUsRequest();
            request.setTitle(title);
            request.setMessage(text);
            mSend.setEnabled(false);
            RestClient.getApiService().contactUs(request, new RestCallback<String>() {
                @Override
                public void failure(RestError restError) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        mSend.setEnabled(true);
                        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                        alertDialog.setMessage(getString(R.string.error_sending_message));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }

                @Override
                public void success(String s, Response response) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        mSend.setEnabled(true);
                        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                        alertDialog.setMessage(getString(R.string.your_message_was_sent));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        SelectMenuItemEvent event = new SelectMenuItemEvent();
                                        event.setPosition(0);
                                        EventBus.getDefault().post(event);
                                    }
                                });
                        alertDialog.show();
                    }
                }
            });
        }
    }

    private boolean isValidData() {
        boolean isValid = true;
        if (mTitle.getText().length() == 0) {
            isValid = false;
            mTitle.setError(getString(R.string.please_enter_title));
        } else {
            mTitle.setError(null);
        }
        if (mText.getText().length() == 0) {
            isValid = false;
            mText.setError(getString(R.string.please_enter_message));
        } else {
            mText.setError(null);
        }
        return isValid;
    }

}
