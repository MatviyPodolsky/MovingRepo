package com.sdex.webteb.dialogs;

/**
 * Created by Yuriy Mysochenko on 18.03.2015.
 */
public interface DialogCallback {

    void confirm();

    void cancel();

    public static class EmptyCallback implements DialogCallback {

        @Override
        public void confirm() {

        }

        @Override
        public void cancel() {

        }
    }

}