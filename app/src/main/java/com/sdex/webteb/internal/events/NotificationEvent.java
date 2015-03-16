package com.sdex.webteb.internal.events;

import android.os.Bundle;

/**
 * Created by Yuriy Mysochenko on 12.03.2015.
 */
public class NotificationEvent {

    private Bundle extras;

    public NotificationEvent(Bundle extras) {
        this.extras = extras;
    }

    public Bundle getExtras() {
        return extras;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

}
