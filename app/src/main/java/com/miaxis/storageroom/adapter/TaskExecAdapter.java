package com.miaxis.storageroom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.bean.Task;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xu.nan on 2017/6/30.
 */

public class TaskExecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<Task> taskList;
    private Context context;
    private OnItemClickListener mOnItemClickListener = null;

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public TaskExecAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_task_exec, parent, false);
        itemView.setOnClickListener(this);
        return new TaskExecViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (holder instanceof TaskExecViewHolder) {
            TaskExecViewHolder itemViewHolder = (TaskExecViewHolder) holder;
            if (taskList != null) {
                Task task = taskList.get(position);
                if (task != null) {
                    itemViewHolder.tvTaskPlateNo.setText(task.getPlateno());
                    if ("1".equals(task.getTasktype())) {
                        itemViewHolder.tvTaskType.setText("出库");
                    } else if ("2".equals(task.getTasktype())) {
                        itemViewHolder.tvTaskType.setText("入库");
                    } else if ("3".equals(task.getTasktype())) {
                        itemViewHolder.tvTaskType.setText("现金调款");
                    } else if ("4".equals(task.getTasktype())) {
                        itemViewHolder.tvTaskType.setText("现金缴款");
                    } else if ("5".equals(task.getTasktype())) {
                        itemViewHolder.tvTaskType.setText("入库");
                    } else if ("6".equals(task.getTasktype())) {
                        itemViewHolder.tvTaskType.setText("出库");
                    }
                }
            }
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (taskList == null) {
            return 0;
        }
        return taskList.size();
    }

    public void resetList(List<Task> items) {
        taskList.clear();
        taskList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null)
            mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
    }

    static class TaskExecViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_task_type)
        TextView tvTaskType;

        @BindView(R.id.tv_task_plateno)
        TextView tvTaskPlateNo;

        TaskExecViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
}
