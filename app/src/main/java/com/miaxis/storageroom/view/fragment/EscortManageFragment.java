package com.miaxis.storageroom.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.adapter.EscortAdapter;
import com.miaxis.storageroom.adapter.StoreEscortAdapter;
import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.bean.TimeStamp;
import com.miaxis.storageroom.comm.BaseComm;
import com.miaxis.storageroom.comm.DownEscortComm;
import com.miaxis.storageroom.comm.DownStoreEscortComm;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.ConfigDao;
import com.miaxis.storageroom.greendao.gen.EscortDao;
import com.miaxis.storageroom.greendao.gen.TimeStampDao;
import com.miaxis.storageroom.view.activity.EscortManageActivity;
import com.miaxis.storageroom.view.custom.SwipeRefreshView;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class EscortManageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshView.OnLoadMoreListener, AdapterView.OnItemClickListener {


    @BindView(R.id.lv_store_escort)
    ListView lvStoreEscort;
    @BindView(R.id.srv_store_escort)
    SwipeRefreshView srvStoreEscort;
    Unbinder unbinder;

    private StoreEscortAdapter escortAdapter;
    private List<Escort> escortList;
    private int curPage = 0;
    private int totalPage;
    private final static int PAGE_SIZE = 10;
    private Config config;


    public EscortManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_escort_manage, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        GreenDaoManager manager = GreenDaoManager.getInstance(getContext());
        ConfigDao configDao = null;
        try {
            configDao = manager.getConfigDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        config = configDao.load(1L);
        escortList = new ArrayList<>();
        escortAdapter = new StoreEscortAdapter(escortList, getContext());
    }

    private void initView() {
        lvStoreEscort.addFooterView(View.inflate(getContext(), R.layout.view_footer, null));
        lvStoreEscort.setAdapter(escortAdapter);
        lvStoreEscort.setOnItemClickListener(this);
        srvStoreEscort.setOnRefreshListener(this);
        srvStoreEscort.setOnLoadMoreListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        escortList.clear();
        curPage = 1;
        Observable
                .just(curPage)
                .map(new Function<Integer, List<Escort>>() {
                    @Override
                    public List<Escort> apply(Integer integer) throws Exception {
                        return doDownEscortComm(integer, PAGE_SIZE);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Escort>>() {
                    @Override
                    public void accept(List<Escort> escorts) throws Exception {
                        escortList.addAll(escorts);
                        escortAdapter.notifyDataSetChanged();
                        srvStoreEscort.setRefreshing(false);
                        curPage ++;
                    }
                });
    }

    @Override
    public void onLoadMore() {
        Log.e("eeee", "loadmore");
        Observable
                .just(curPage)
                .map(new Function<Integer, List<Escort>>() {
                    @Override
                    public List<Escort> apply(Integer integer) throws Exception {
                        return doDownEscortComm(integer, PAGE_SIZE);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Escort>>() {
                    @Override
                    public void accept(List<Escort> escorts) throws Exception {
                        escortList.addAll(escorts);
                        escortAdapter.notifyDataSetChanged();
                        srvStoreEscort.setRefreshing(false);
                        srvStoreEscort.setLoading(false);
                        curPage ++;
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Escort escort = escortList.get(position);
        Intent i = new Intent(getActivity(), EscortManageActivity.class);
        i.putExtra("escort", escort);
        startActivity(i);
    }

    private List<Escort> doDownEscortComm(int pageNum, int pageSize) throws Exception {
        StringBuilder msgSb = new StringBuilder();
        Socket socket = BaseComm.connect(config.getIp(), config.getPort(), 10000, msgSb);
        if (socket == null) {
            return new ArrayList<>();
        }
        int result;
        DownStoreEscortComm comm = new DownStoreEscortComm(socket, config.getOrgCode(), pageNum+"", pageSize+"");
        result = comm.executeComm();
        if (result == 0) {
            return comm.getEscortList();
        } else {
            return new ArrayList<>();
        }
    }


}
