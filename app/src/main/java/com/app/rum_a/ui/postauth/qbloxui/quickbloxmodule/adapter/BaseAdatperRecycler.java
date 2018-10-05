package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 3/3/2017.
 */

public abstract class BaseAdatperRecycler<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    protected LayoutInflater inflater;
    protected Context context;
    protected List<T> objectsList;

    public BaseAdatperRecycler(Context context) {
        this(context, new ArrayList<T>());
    }

    public BaseAdatperRecycler(Context context, List<T> objectsList) {
        this.context = context;
        this.objectsList = objectsList;
        this.inflater = LayoutInflater.from(context);
    }




    public T getItem(int position) {
        return objectsList.get(position);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        onBindMyViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return objectsList == null || objectsList.size() == 0 ? 0 : objectsList.size();
    }

    public void updateList(List<T> newData, boolean callupdate) {
        objectsList=new ArrayList<>();
        objectsList.addAll(newData);
        notifyDataSetChanged();
        if (callupdate) {
            upDateList(objectsList);
        }
    }

    public void add(T item) {
        objectsList.add(item);
        notifyDataSetChanged();
    }

    public void addList(List<T> items) {
        objectsList.addAll(0, items);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return objectsList;
    }

    public void remove(T item) {
        objectsList.remove(item);
        notifyDataSetChanged();
    }

//    public abstract int setViewholder();

    public abstract void onBindMyViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract void upDateList(List<T> updatedList);

}
