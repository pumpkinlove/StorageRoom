package com.miaxis.storageroom.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.adapter.EscortAdapter;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.event.DownEscortEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.EscortDao;
import com.miaxis.storageroom.service.DownInfoService;
import com.miaxis.storageroom.view.activity.EscortManageActivity;
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
public class EscortFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshView.OnLoadMoreListener {

    private static final String TAG = "EscortFragment";
    Unbinder unbinder;
    @BindView(R.id.lv_escort)
    ListView lvEscort;
    @BindView(R.id.srv_escort)
    SwipeRefreshView srvEscort;

    private EscortAdapter escortAdapter;
    private List<Escort> escortList;
    private EscortDao escortDao;
    private int curPage = 0;
    private int totalPage;
    private int pageSize = 10;

    public EscortFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_escort, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initData();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initData() {
        GreenDaoManager manager = GreenDaoManager.getInstance(getContext());
        try {
            escortDao = manager.getEscortDao();
            escortList = escortDao.queryBuilder().offset(curPage * pageSize).limit(pageSize).list();
            escortAdapter = new EscortAdapter(escortList, getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        lvEscort.setAdapter(escortAdapter);
        srvEscort.setOnRefreshListener(this);
        srvEscort.setOnLoadMoreListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        DownInfoService.startActionDownEscort(getActivity());
    }

    @Override
    public void onLoadMore() {
        Log.e(TAG, "onLoadMore");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownEscortEvent(DownEscortEvent e) {
        srvEscort.setRefreshing(false);
        if (e.getResult() != 0) {
            Toast.makeText(getActivity(), "刷新押运员失败", Toast.LENGTH_SHORT).show();
        }
        reLoadEscort();
    }

    private void reLoadEscort() {
        List<Escort> esList = escortDao.queryBuilder().offset(curPage * pageSize).limit(pageSize).list();
        escortList.clear();
        escortList.addAll(esList);
        escortAdapter.notifyDataSetChanged();
    }

}
