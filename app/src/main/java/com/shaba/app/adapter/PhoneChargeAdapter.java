package com.shaba.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shaba.app.R;

import java.util.List;

public class PhoneChargeAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private int selectItem = -1;
    private List<String> amountList;

    public PhoneChargeAdapter(Activity activity,
                              List<String> amountList) {
        this.activity = activity;
        this.amountList = amountList;
    }

    @Override
    public int getCount() {
        return amountList.size();
    }

    @Override
    public Object getItem(int location) {
        try {
            return amountList.get(location);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<String> getamountList() {
        return amountList;
    }

    public void setamountList(List<String> amountList) {
        this.amountList = amountList;
    }

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_recharge_amount, null);
            holder = new ViewHolder();
            holder.amountText = (TextView) convertView.findViewById(R.id.item_amount);
            holder.priceText = (TextView) convertView.findViewById(R.id.item_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String obj = amountList.get(position);

        holder.amountText.setText(obj);

        if (position == selectItem) {
            holder.amountText.setTextColor(Color.WHITE);
            holder.amountText.setBackgroundResource(R.drawable.textview_shap_2);
        } else {
            holder.amountText.setTextColor(activity.getResources().getColor(R.color.amount_color));
            holder.amountText.setBackgroundResource(R.drawable.textview_shap_1);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView amountText;
        TextView priceText;
    }
}
