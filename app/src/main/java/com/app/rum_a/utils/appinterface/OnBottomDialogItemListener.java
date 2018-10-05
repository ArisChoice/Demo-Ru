package com.app.rum_a.utils.appinterface;

import android.view.View;

/**
 * Created by Harish on 29/8/17.
 */

public interface OnBottomDialogItemListener<T> {

    public void onItemClick(View view, int position, int type, Object t);

}
