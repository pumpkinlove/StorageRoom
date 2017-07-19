package com.miaxis.storageroom.view.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.adapter.TaskAdapter;
import com.miaxis.storageroom.bean.Task;
import com.miaxis.storageroom.event.DownTaskFinishEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.TaskDao;
import com.miaxis.storageroom.service.DownTaskService;
import com.miaxis.storageroom.util.DateUtil;
import com.miaxis.storageroom.view.custom.SwipeRefreshView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.lv_task)
    ListView lvTask;
    @BindView(R.id.srv_task)
    SwipeRefreshView srvTask;
    Unbinder unbinder;
    @BindView(R.id.tv_sel_taskdate)
    TextView tvSelTaskdate;

    private List<Task> taskList;
    private String taskDate;
    private TaskAdapter adapter;
    private TaskDao taskDao;


    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initData();
        initView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        try {
            taskDao = GreenDaoManager.getInstance(getActivity()).getTaskDao();
            taskList = taskDao.queryBuilder().where(TaskDao.Properties.Taskdate.eq(DateUtil.toMonthDay(new Date()))).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new TaskAdapter(taskList, getActivity());

    }

    private void initView() {
        lvTask.setAdapter(adapter);
        srvTask.setOnRefreshListener(this);
        tvSelTaskdate.setText(DateUtil.toMonthDay(new Date()));
    }

    @Override
    public void onRefresh() {
        DownTaskService.startActionDownTask(getActivity(), tvSelTaskdate.getText().toString());
    }

    @OnClick(R.id.tv_sel_taskdate)
    void onSelTaskDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                String str = String.format("%04d-%02d-%02d", year,
                        monthOfYear + 1, dayOfMonth);
                tvSelTaskdate.setText(str);
                String taskdate = tvSelTaskdate.getText().toString().trim();
                onRefresh();
            }
        }, year, month, day).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownTaskDone(DownTaskFinishEvent e) {
        srvTask.setRefreshing(false);
        if (e.isSuccess()) {
            try {
                List<Task> tList = taskDao.queryBuilder().where(TaskDao.Properties.Taskdate.eq(tvSelTaskdate.getText().toString())).list();
                taskList.clear();
                taskList.addAll(tList);
                adapter.notifyDataSetChanged();
            } catch (Exception ex) {
            }
        } else {
            Toast.makeText(getActivity(), "下载任务失败", Toast.LENGTH_SHORT).show();
        }
    }

}
