package com.miaxis.storageroom.view.fragment;


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
import android.widget.ListView;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.adapter.EscortAdapter;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.EscortDao;
import com.miaxis.storageroom.view.custom.SwipeRefreshView;

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

    public EscortFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_escort, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i=0; i<20; i++) {
            preData();
        }
        escortAdapter.notifyDataSetChanged();
    }

    private void initData() {
        GreenDaoManager manager = GreenDaoManager.getInstance(getContext());
        try {
            EscortDao dao = manager.getEscortDao();
            escortList = dao.loadAll();
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
    }

    @Override
    public void onRefresh() {
        Log.e(TAG, "onRefresh");
    }

    @Override
    public void onLoadMore() {
        Log.e(TAG, "onLoadMore");
    }

    void preData() {
        Escort e = new Escort();
        e.setCode("xd-0000");
        e.setName("sss");
        escortList.add(e);
    }
}
