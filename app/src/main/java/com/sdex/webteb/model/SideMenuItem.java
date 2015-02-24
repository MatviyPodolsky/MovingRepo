package com.sdex.webteb.model;

/**
 * Created by Yuriy Mysochenko on 24.02.2015.
 */
public class SideMenuItem {

    private final String title;
    private final int iconRes;

    public SideMenuItem(String title, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public int getIconRes() {
        return iconRes;
    }

}
