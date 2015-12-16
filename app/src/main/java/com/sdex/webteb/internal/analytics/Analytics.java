package com.sdex.webteb.internal.analytics;

import android.app.Application;
import android.support.annotation.StringRes;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sdex.webteb.WTApp;
import com.sdex.webteb.utils.AnalyticsUtils;

/**
 * Author: Yuriy Mysochenko
 * Date: 16.04.2015
 */
public class Analytics {

    private WTApp application;

    public Analytics(Application application) {
        this.application = (WTApp) application;
    }

    public void sendAnalyticsScreenName(int nameRes) {
        sendAnalyticsScreenName(application.getString(nameRes));
    }

    public void sendAnalyticsScreenName(String name) {
        Tracker tracker = application.getTracker();
        tracker.setScreenName(name);
        tracker.send(new HitBuilders.ScreenViewBuilder()
//                .setNewSession()
                .build());
        AnalyticsUtils.sendScreenName(name);
    }

    public void sendAnalyticsDimension(@StringRes int screenName, int index, String dimension) {
        sendAnalyticsDimension(application.getString(screenName), index, dimension);
    }

    public void sendAnalyticsDimension(String screenName, int index, String dimension) {
        Tracker tracker = application.getTracker();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder()
                .setCustomDimension(index, dimension)
                .build());
    }

    public void sendAnalyticsEvent(String category, String action) {
        sendAnalyticsEvent(category, action, null);
    }

    public void sendAnalyticsEvent(String category, String action, String label) {
        Tracker t = application.getTracker();
        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder();
        eventBuilder.setCategory(category);
        eventBuilder.setAction(action);
        if (label != null) {
            eventBuilder.setLabel(label);
        }
        t.send(eventBuilder.build());
        AnalyticsUtils.sendEvent(category, action, label, null);
    }

    public void sendAnalyticsTiming(@StringRes int screenName, String category, long value) {
        sendAnalyticsTiming(application.getString(screenName), category, value);
    }

    public void sendAnalyticsTiming(String screenName, String category, long value) {
        Tracker tracker = application.getTracker();
//        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.TimingBuilder()
                .setCategory(category)
                .setValue(value)
                .setVariable(screenName + ".timing")
                .setLabel(screenName + "Speed")
                .build());
    }

}
