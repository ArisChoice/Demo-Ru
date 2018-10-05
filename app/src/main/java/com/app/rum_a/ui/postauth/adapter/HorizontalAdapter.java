package com.app.rum_a.ui.postauth.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rum_a.R;
import com.app.rum_a.model.modelutils.ImagePickerModel;
import com.app.rum_a.ui.postauth.activity.AddPropertyActivity;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.appinterface.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder> {

    AddPropertyActivity addPropertyActivity;
    ArrayList<ImagePickerModel> imageList;
    OnItemClickListener<ImagePickerModel> onItemClickListener;

    public HorizontalAdapter(AddPropertyActivity addPropertyActivity, ArrayList<ImagePickerModel> imageList, OnItemClickListener<ImagePickerModel> onItemClickListener) {
        this.addPropertyActivity = addPropertyActivity;
        this.imageList = imageList;
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_image_view, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder holder, int position) {
        holder.imageSelected.setImageURI(Uri.parse(imageList.get(position).getImagePath()));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void update(ArrayList<ImagePickerModel> item) {
        this.imageList.addAll(item);
        notifyDataSetChanged();
    }

    public List<ImagePickerModel> getImageList() {
        return imageList;
    }

    public void removeItems() {
        imageList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        imageList.remove(position);
        notifyDataSetChanged();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_selected)
        CircleImageView imageSelected;
        @BindView(R.id.image_remove)
        ImageView imageRemove;

        public HorizontalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, getAdapterPosition(), 1, imageList.get(getAdapterPosition()));

                }
            });
        }
    }
}