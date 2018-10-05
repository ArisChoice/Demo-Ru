package com.app.rum_a.ui.postauth.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rum_a.R;
import com.app.rum_a.model.resp.PropertyListResponseModel;
import com.app.rum_a.utils.AppConstants;
import com.app.rum_a.utils.AppConstants.Clickerations;
import com.app.rum_a.utils.CommonUtils;
import com.app.rum_a.utils.ImageUtility;
import com.app.rum_a.utils.appinterface.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.ViewHolder> {

    private ArrayList<PropertyListResponseModel.ResultBean> listModels;
    private Context context;
    String TAG = "ServiceProviderListAdapter";
    OnItemClickListener<PropertyListResponseModel.ResultBean> onItemClickListener;
    boolean showOptions;

    public PropertyListAdapter(Context context, boolean showOptions, ArrayList<PropertyListResponseModel.ResultBean> listModels,
                               OnItemClickListener<PropertyListResponseModel.ResultBean> onItemClickListener) {
        this.listModels = listModels;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.showOptions = showOptions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_property_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void updateAdapter(List<PropertyListResponseModel.ResultBean> propertyList) {
        listModels.clear();
        listModels.addAll(propertyList);
        notifyDataSetChanged();
    }

    public void removeProperty(int position) {
        listModels.remove(position);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.propertyName)
        TextView propertyNameTxt;
        @BindView(R.id.propertyPrice)
        TextView propertyPriceTxt;
        @BindView(R.id.propertyImage)
        ImageView propertyImage;
        @BindView(R.id.optionBtn)
        ImageView optionBtn;
        @BindView(R.id.propertyType)
        TextView proprtySellingType;
        @BindView(R.id.propertyAddress)
        TextView propertAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            optionBtn.setOnClickListener(this);
            propertyImage.setOnClickListener(this);
            if (showOptions) {
                optionBtn.setVisibility(View.VISIBLE);
            } else {
                optionBtn.setVisibility(View.VISIBLE);
                optionBtn.setImageResource(R.drawable.remove);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.optionBtn:
                    if (showOptions)
                        onItemClickListener.onItemClick(v, getAdapterPosition(), Clickerations.OPTION_VIEW_CLICK, listModels.get(getAdapterPosition()));
                    else
                        onItemClickListener.onItemClick(v, getAdapterPosition(), Clickerations.REMOVE_VIEW_CLICK, listModels.get(getAdapterPosition()));
                    break;
                case R.id.propertyImage:
                    onItemClickListener.onItemClick(v, getAdapterPosition(), Clickerations.IMAGE_VIEW_CLICK, listModels.get(getAdapterPosition()));
                    break;
            }

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PropertyListResponseModel.ResultBean itemData = listModels.get(position);
        setViewData(holder, itemData);
    }

    private void setViewData(ViewHolder holder, PropertyListResponseModel.ResultBean itemData) {
        holder.propertyNameTxt.setText(itemData.getName());
        holder.propertyPriceTxt.setText(CommonUtils.getCurrencySymbol(itemData.getCurrency()) + itemData.getPrice());

        holder.propertAddress.setText(itemData.getAddress());
        try {
            new ImageUtility(context).LoadImage(CommonUtils.getValidUrl(itemData.getPropertyImageList().get(0).getImageURL()), holder.propertyImage);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        if (showOptions) {
            holder.proprtySellingType.setText(getSellingType(itemData.getSellingType()));
        } else {
            if (itemData.getForRentOrBuy() + "" != null) {
                if (itemData.getForRentOrBuy() == AppConstants.ForRentOrBuy.Rent)
                    holder.proprtySellingType.setText(R.string.str_rent);
                else holder.proprtySellingType.setText(R.string.str_buy);
            } else holder.proprtySellingType.setText(R.string.str_rent);
        }

    }

    private String getSellingType(int itemtype) {
        String type = null;
        switch (itemtype) {
            case AppConstants.LookingTypes.Buy:
                type = context.getString(R.string.str_buy);
                break;
            case AppConstants.LookingTypes.Lease:
                type = context.getString(R.string.str_lease);
                break;
            case AppConstants.LookingTypes.Sell:
                type = context.getString(R.string.str_sell);
                break;
            case AppConstants.LookingTypes.Rent:
                type = context.getString(R.string.str_rent);
                break;
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }


}
