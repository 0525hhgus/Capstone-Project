package org.techtown.gwangjubus;

import android.view.View;

public interface OnBusLineClickListener {
    public void onItemClick(BusLineAdapter.LineViewHolder holder, View view, int position);
}
