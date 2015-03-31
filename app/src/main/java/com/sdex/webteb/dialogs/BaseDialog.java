package com.sdex.webteb.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Yuriy Mysochenko on 16.03.2015.
 */
public abstract class BaseDialog extends DialogFragment {

    protected Callback callback;

    public interface Callback {
        void confirm();
        void cancel();

        class EmptyCallback implements Callback {

            @Override
            public void confirm() {

            }

            @Override
            public void cancel() {

            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public abstract int getLayoutResource();

}
