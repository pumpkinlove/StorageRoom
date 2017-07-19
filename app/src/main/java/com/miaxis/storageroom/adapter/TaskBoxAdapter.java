package com.miaxis.storageroom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.bean.TaskBox;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xu.nan on 2017/7/13.
 */

public class TaskBoxAdapter extends RecyclerView.Adapter<TaskBoxAdapter.TaskBoxViewHolder> implements View.OnClickListener {

    private List<TaskBox> taskBoxList;
    private Context context;
    private TaskExecAdapter.OnItemClickListener mOnItemClickListener = null;

    public TaskBoxAdapter(List<TaskBox> taskBoxList, Context context) {
        this.taskBoxList = taskBoxList;
        this.context = context;
    }

    @Override
    public TaskBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_task_box, parent, false);
        itemView.setOnClickListener(this);
        return new TaskBoxViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskBoxViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (taskBoxList != null) {
            TaskBox box = taskBoxList.get(position);
            if (TextUtils.isEmpty(box.getBoxCode())) {
                holder.tvBoxCode.setText(box.getBoxRfid());
            } else {
                holder.tvBoxCode.setText(box.getBoxCode());
            }
            switch (box.getStatus()) {
                case TaskBox.VERIFIED:
                    holder.tvBoxStatus.setTextColor(context.getResources().getColor(R.color.green_dark));
                    holder.tvBoxStatus.setText("已验证");
                    break;
                case TaskBox.NOT_VERIFIED:
                    holder.tvBoxStatus.setTextColor(context.getResources().getColor(R.color.dark));
                    holder.tvBoxStatus.setText("未验证");
                    break;
                case TaskBox.REST:
                    holder.tvBoxStatus.setTextColor(context.getResources().getColor(R.color.red));
                    holder.tvBoxStatus.setText("多余的");
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (taskBoxList == null) {
            return 0;
        }
        return taskBoxList.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null)
            mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
    }

    public TaskExecAdapter.OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(TaskExecAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    static class TaskBoxViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_box_code)
        TextView tvBoxCode;
        @BindView(R.id.tv_box_status)
        TextView tvBoxStatus;
        @BindView(R.id.ll_task_box)
        LinearLayout llTaskBox;

        TaskBoxViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
