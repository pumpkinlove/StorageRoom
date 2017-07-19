package com.miaxis.storageroom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.bean.Worker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xu.nan on 2017/7/14.
 */

public class WorkerAdapter extends BaseAdapter {

    private List<Worker> workerList;
    private LayoutInflater mInflater;

    public WorkerAdapter(List<Worker> workerList, Context context) {
        this.workerList = workerList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (workerList != null) {
            return workerList.size();
        }
        return 0;
    }

    @Override
    public Worker getItem(int i) {
        if (workerList != null) {
            return workerList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        System.out.println("getView " + position + " " + convertView);
        WorekrViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_worker, null);
            holder = new WorekrViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (WorekrViewHolder) convertView.getTag();
        }
        Worker worker = workerList.get(position);
        holder.tvWorkerCode.setText(worker.getCode());
        holder.tvWorkerName.setText(worker.getName());
        return convertView;
    }

    static class WorekrViewHolder {
        @BindView(R.id.tv_worker_code)
        TextView tvWorkerCode;
        @BindView(R.id.tv_worker_name)
        TextView tvWorkerName;

        WorekrViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
