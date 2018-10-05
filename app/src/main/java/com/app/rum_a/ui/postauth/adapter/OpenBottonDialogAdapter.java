package com.app.rum_a.ui.postauth.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.rum_a.R;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.appinterface.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenBottonDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String[] nameList;
    int[] iconsList;
    OnItemClickListener listener;

    public OpenBottonDialogAdapter(String[] nameList, int[] iconsList, OnItemClickListener listener) {
        this.iconsList = iconsList;
        this.nameList = nameList;
        this.listener = listener;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_bottom_dialog, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommonViewHolder viewHolder = (CommonViewHolder) holder;
        if (iconsList.length == 0) {
            viewHolder.setData(nameList[position]);
        } else {
            viewHolder.setData(nameList[position], iconsList[position]);
        }

    }

    @Override
    public int getItemCount() {
        return nameList.length;
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.icon)
        ImageView icon;

        public CommonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /*@OnClick({R.id.one})
        public void onLCick(View v) {
            switch (v.getId()) {
                case R.id.one:
                    listener.onItemClick(v, getAdapterPosition(), Config.APAPTER_BOTTOM_DIALOG_CLICK, nameList[getAdapterPosition()]);
                    break;
            }
        }*/

        public void setData(String name, int icon_path) {
            txt_name.setText(name);
            icon.setBackgroundResource(icon_path);

        }

        public void setData(String name) {
            txt_name.setText(name);
            icon.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition(), AppConstants.RequestCode.APAPTER_BOTTOM_DIALOG_CLICK, nameList[getAdapterPosition()]);
        }
    }
}