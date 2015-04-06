package com.sdex.webteb.adapters;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

/**
 * Author: Yuriy Mysochenko
 * Date: 06.04.2015
 */
public abstract class TimeNavigationAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH> {

    public abstract void setSelectedItem(int position);
    public abstract void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener);

    public void hideLabels() {

    }

    public void showLabels() {

    }

}
