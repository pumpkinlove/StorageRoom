package com.miaxis.storageroom.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.adapter.WorkerAdapter;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.event.CommExecEvent;
import com.miaxis.storageroom.event.ToastEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.WorkerDao;
import com.miaxis.storageroom.service.DownInfoService;
import com.miaxis.storageroom.view.custom.SwipeRefreshView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkerListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "WorkerListFragment";
    @BindView(R.id.lv_worker)
    ListView lvWorker;
    @BindView(R.id.srv_worker)
    SwipeRefreshView srvWorker;
    Unbinder unbinder;

    private WorkerAdapter workerAdapter;
    private List<Worker> workerList;
    private WorkerDao workerDao;

    public WorkerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_worker_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        onRefresh();
        return view;
    }

    private void initData() {
        GreenDaoManager manager = GreenDaoManager.getInstance(getActivity());
        try {
            workerDao = manager.getWorkerDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        workerList = workerDao.loadAll();
        workerAdapter = new WorkerAdapter(workerList, getActivity());
    }

    private void initView() {
        lvWorker.setAdapter(workerAdapter);
        srvWorker.setOnRefreshListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        DownInfoService.startActionDownWorker(getActivity());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommExecEvent(CommExecEvent e) {
        if (e.getCommCode() == CommExecEvent.COMM_ADD_WORKER) {
            switch (e.getResult()) {
                case CommExecEvent.RESULT_SUCCESS:
                    onRefresh();
                    break;
            }
        } else if (e.getCommCode() == CommExecEvent.COMM_DOWN_WORKER) {
            switch (e.getResult()) {
                case CommExecEvent.RESULT_SUCCESS:
                    srvWorker.setRefreshing(false);
                    break;
                case CommExecEvent.RESULT_EXCEPTION:
                    Toast.makeText(getActivity(), "下载操作员列表异常", Toast.LENGTH_SHORT).show();
                    break;
                case CommExecEvent.RESULT_SOCKET_NULL:
                    Toast.makeText(getActivity(), "网络连接失败，请检查ip地址、端口号", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getActivity(), "下载操作员列表失败", Toast.LENGTH_SHORT).show();
            }
            reLoadList();
        }
    }

    private void reLoadList() {
        List<Worker> wList = workerDao.loadAll();
        workerList.clear();
        workerList.addAll(wList);
        workerAdapter.notifyDataSetChanged();
    }

}
