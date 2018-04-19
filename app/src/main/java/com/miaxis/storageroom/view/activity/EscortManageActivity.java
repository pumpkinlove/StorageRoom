package com.miaxis.storageroom.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.app.Storage_App;
import com.miaxis.storageroom.bean.Config;
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.comm.BaseComm;
import com.miaxis.storageroom.comm.DeleteEscortComm;
import com.miaxis.storageroom.comm.DownStoreEscortComm;
import com.miaxis.storageroom.comm.UpdateEscortComm;
import com.miaxis.storageroom.event.CommExecEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.ConfigDao;
import com.miaxis.storageroom.service.DownInfoService;
import com.miaxis.storageroom.service.EscortManageService;
import com.miaxis.storageroom.util.Constants;
import com.miaxis.storageroom.util.DateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class EscortManageActivity extends BaseActivity {

    @BindView(R.id.toolbar_add_escort)
    Toolbar toolbarAddEscort;
    @BindView(R.id.et_escort_code)
    EditText etEscortCode;
    @BindView(R.id.et_escort_name)
    EditText etEscortName;
    @BindView(R.id.btn_finger0)
    Button btnFinger0;
    @BindView(R.id.btn_finger1)
    Button btnFinger1;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private Escort mEscort;
    private ProgressDialog pdManageEscort;
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_escort);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar();
        initData();
        initView();
    }

    private void initData() {
        mEscort = (Escort) getIntent().getSerializableExtra("escort");
        GreenDaoManager manager = GreenDaoManager.getInstance(this);
        ConfigDao configDao = null;
        try {
            configDao = manager.getConfigDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        config = configDao.load(1L);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_menu, menu);
        return true;
    }

    private void initView() {
        pdManageEscort = new ProgressDialog(this);
        pdManageEscort.setCancelable(false);

        if (mEscort != null) {
            etEscortName.setText(mEscort.getName());
            etEscortCode.setText(mEscort.getCode().split("-")[1]);
        }
    }

    private void initToolBar() {
        toolbarAddEscort.setTitle(R.string.action_update_escort);
        setSupportActionBar(toolbarAddEscort);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                deleteEscort(mEscort.getCode());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_finger0)
    void onFinger0Clicked() {
        Intent i = new Intent(this, GetFingerActivity.class);
        startActivityForResult(i, 1);
    }

    @OnClick(R.id.btn_finger1)
    void onFinger1Clicked() {
        Intent i = new Intent(this, GetFingerActivity.class);
        startActivityForResult(i, 2);
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClicked(View view) {
        if (etEscortCode.getText().length() == 0) {
            Toast.makeText(this, "编号不能为空，且不能重复", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etEscortName.getText().length() == 0) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (TextUtils.isEmpty(mEscort.getFinger0())) {
//            Toast.makeText(this, "指纹一为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(mEscort.getFinger1())) {
//            Toast.makeText(this, "指纹二为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
        hideSoftInput(view);
        mEscort.setName(etEscortName.getText().toString());
        mEscort.setCode(Constants.SYS_CODE + "-" + etEscortCode.getText());
        mEscort.setOpDate(DateUtil.toAll(new Date()));
        Worker curWorker = ((Storage_App)getApplicationContext()).getCurWorker();
        if (curWorker == null) {
            Toast.makeText(this, "登入操作员为空，请重新登入", Toast.LENGTH_SHORT).show();
            return;
        }
        mEscort.setOpUserCode(curWorker.getCode());
        mEscort.setOpUserName(curWorker.getName());
        updateEscort(mEscort);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data == null)
            return;
        if (requestCode == 1) {
            mEscort.setFinger(data.getStringExtra("finger"), 0);
            btnFinger0.setText("指纹一（已采集）");
            btnFinger0.setTextColor(getResources().getColor(R.color.white));
            btnFinger0.setBackgroundResource(R.drawable.green_btn_bg);
        } else if (requestCode == 2) {
            mEscort.setFinger(data.getStringExtra("finger"), 1);
            btnFinger1.setText("指纹二（已采集）");
            btnFinger1.setTextColor(getResources().getColor(R.color.white));
            btnFinger1.setBackgroundResource(R.drawable.green_btn_bg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommExecEvent(CommExecEvent e) {
        if (e.getCommCode() == CommExecEvent.COMM_UPDATE_ESCORT) {
            switch (e.getResult()) {
                case CommExecEvent.RESULT_SUCCESS:
                    pdManageEscort.dismiss();
                    break;
                case CommExecEvent.RESULT_EXCEPTION:
                    pdManageEscort.setMessage("更新押运员信息异常!");
                    pdManageEscort.setCancelable(true);
                    break;
                case CommExecEvent.RESULT_SOCKET_NULL:
                    pdManageEscort.setMessage("网络连接失败，请检查ip地址、端口号!");
                    pdManageEscort.setCancelable(true);
                    break;
                default:
                    pdManageEscort.setMessage("添加失败!");
                    pdManageEscort.setCancelable(true);
            }
        }
    }

    private void deleteEscort(String esCode) {
        Observable
                .just(esCode)
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String esCode) throws Exception {
                        return doDelEscortComm(esCode);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer result) throws Exception {
                        if (result == 0) {
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "删除失败 " + result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private int doDelEscortComm(String esCode) {
        StringBuilder msgSb = new StringBuilder();
        Socket socket = BaseComm.connect(config.getIp(), config.getPort(), 10000, msgSb);
        if (socket == null) {
            return -1;
        }
        DeleteEscortComm comm = new DeleteEscortComm(socket, esCode);
        return comm.executeComm();
    }

    private void updateEscort(Escort escort) {
        Observable
                .just(escort)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Escort>() {
                    @Override
                    public void accept(Escort escort) throws Exception {
                        pdManageEscort.setMessage("正在更新" + escort.getName() + "的信息");
                        pdManageEscort.show();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<Escort, Integer>() {
                    @Override
                    public Integer apply(Escort escort) throws Exception {
                        return doUpdateEscortComm(escort);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer result) throws Exception {
                        pdManageEscort.setCancelable(true);
                        if (result == 0) {
                            pdManageEscort.setMessage("更新完成");
                            DownInfoService.startActionDownEscort(getApplicationContext());
                            pdManageEscort.dismiss();
                            finish();
                        } else {
                            pdManageEscort.setMessage("更新失败");
                        }
                    }
                });
    }

    private int doUpdateEscortComm(Escort escort) {
        StringBuilder msgSb = new StringBuilder();
        Socket socket = BaseComm.connect(config.getIp(), config.getPort(), 10000, msgSb);
        if (socket == null) {
            return -1;
        }
        UpdateEscortComm comm = new UpdateEscortComm(socket, escort);
        return comm.executeComm();
    }

}
