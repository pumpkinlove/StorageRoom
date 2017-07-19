package com.miaxis.storageroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.bean.Task;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xu.nan on 2017/7/14.
 */

public class TaskAdapter extends BaseAdapter {

    private List<Task> taskList;
    private LayoutInflater mInflater;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (taskList != null) {
            return taskList.size();
        }
        return 0;
    }

    @Override
    public Task getItem(int i) {
        if (taskList != null) {
            return taskList.get(i);
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
        TaskViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_task, null);
            holder = new TaskViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (TaskViewHolder) convertView.getTag();
        }
        Task task = taskList.get(position);
        holder.tvTaskOpdate.setText(task.getOpdate());
        if ("1".equals(task.getTasktype())) {
            holder.tvTaskType.setText("出库");
        } else  if ("2".equals(task.getTasktype())) {
            holder.tvTaskType.setText("入库");
        }
        holder.tvTaskOpuser.setText(task.getOpusername());
        holder.tvTaskStatus.setText(task.getStatusName());
        return convertView;
    }

    static class TaskViewHolder {
        @BindView(R.id.tv_task_type)
        TextView tvTaskType;
        @BindView(R.id.tv_task_status)
        TextView tvTaskStatus;
        @BindView(R.id.tv_task_opdate)
        TextView tvTaskOpdate;
        @BindView(R.id.tv_task_opuser)
        TextView tvTaskOpuser;

        TaskViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
