package com.miaxis.storageroom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.bean.Escort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class StoreEscortAdapter extends BaseAdapter {

    private List<Escort> escortList;
    private LayoutInflater mInflater;
    private Context mContext;

    public StoreEscortAdapter(List<Escort> escortList, Context context) {
        this.escortList = escortList;
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (escortList == null) {
            return 0;
        }
        return escortList.size();
    }

    @Override
    public Object getItem(int position) {
        if (escortList == null) {
            return null;
        }
        return escortList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EscortViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_escort, null);
            holder = new EscortViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (EscortViewHolder)convertView.getTag();
        }
        Escort escort = escortList.get(position);
        holder.tvEscortCode.setText(escort.getCode());
        holder.tvEscortName.setText(escort.getName());
        byte[] photoBytes = escort.getPhoto();
        if (photoBytes != null && photoBytes.length > 0) {
            Bitmap bmp = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
            holder.ivEscort.setImageBitmap(bmp);
        } else  {
            holder.ivEscort.setImageResource(R.mipmap.ic_launcher);
        }
        if (escort.isCollected()) {
            holder.tvIsCollected.setText("已采集");
            holder.tvIsCollected.setTextColor(mContext.getResources().getColor(R.color.green_dark));
        } else {
            holder.tvIsCollected.setText("未采集");
            holder.tvIsCollected.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        return convertView;
    }


    static class EscortViewHolder {
        @BindView(R.id.iv_escort)
        ImageView ivEscort;
        @BindView(R.id.tv_escort_code)
        TextView tvEscortCode;
        @BindView(R.id.tv_escort_name)
        TextView tvEscortName;
        @BindView(R.id.tv_isCollected)
        TextView tvIsCollected;

        EscortViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }


}
