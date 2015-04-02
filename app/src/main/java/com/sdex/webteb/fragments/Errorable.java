package com.sdex.webteb.fragments;

/**
 * Author: Yuriy Mysochenko
 * Date: 02.04.2015
 */
public interface Errorable {
    void showProgress();

    void hideProgress();

    void showError();

    void hideError();

    void showData();
}
