package com.sdex.webteb.internal.model;

import org.parceler.Parcel;

/**
 * Created by Yuriy Mysochenko on 06.03.2015.
 */
@Parcel
public class Settings {

    private boolean isWeeklyTipNotification;
    private boolean isNewsletter;
    private int testsReminder;

    public Settings() {
    }

    public boolean isWeeklyTipNotification() {
        return isWeeklyTipNotification;
    }

    public void setWeeklyTipNotification(boolean isWeeklyTipNotification) {
        this.isWeeklyTipNotification = isWeeklyTipNotification;
    }

    public boolean isNewsletter() {
        return isNewsletter;
    }

    public void setNewsletter(boolean isNewsletter) {
        this.isNewsletter = isNewsletter;
    }

    public int getTestsReminder() {
        return testsReminder;
    }

    public void setTestsReminder(int testsReminder) {
        this.testsReminder = testsReminder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Settings settings = (Settings) o;

        if (isNewsletter != settings.isNewsletter) return false;
        if (isWeeklyTipNotification != settings.isWeeklyTipNotification) return false;
        if (testsReminder != settings.testsReminder) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isWeeklyTipNotification ? 1 : 0);
        result = 31 * result + (isNewsletter ? 1 : 0);
        result = 31 * result + testsReminder;
        return result;
    }
}
