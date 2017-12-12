package com.miaxis.storageroom.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.event.CommExecEvent;
import com.miaxis.storageroom.service.AddWorkerService;
import com.miaxis.storageroom.util.DateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddWorkerActivity extends BaseActivity {

    @BindView(R.id.toolbar_add_worker)
    Toolbar toolbar;
    @BindView(R.id.et_worker_code)
    EditText etWorkerCode;
    @BindView(R.id.et_worker_name)
    EditText etWorkerName;
    @BindView(R.id.btn_finger0)
    Button btnFinger0;
    @BindView(R.id.btn_finger1)
    Button btnFinger1;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private Worker worker;
    private ProgressDialog pdAddWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar();
        worker = new Worker();
        pdAddWorker = new ProgressDialog(this);
        pdAddWorker.setMessage("正在上传操作员...");
        pdAddWorker.setCancelable(false);
    }

    private void initToolBar() {
        toolbar.setTitle(R.string.action_add_worker);
        setSupportActionBar(toolbar);
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
    void onSubmitClicked() {
        if (etWorkerCode.getText().length() == 0) {
            Toast.makeText(this, "编号不能为空，且不能重复", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etWorkerName.getText().length() == 0) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(worker.getFinger0())) {
            Toast.makeText(this, "指纹一为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(worker.getFinger1())) {
            Toast.makeText(this, "指纹二为空", Toast.LENGTH_SHORT).show();
            return;
        }
        pdAddWorker.show();
        worker.setName(etWorkerName.getText().toString());
        worker.setCode("xd-" + etWorkerCode.getText());
        worker.setOpDate(DateUtil.toAll(new Date()));
        worker.setIdCard("");
        worker.setOpUser("");
        worker.setOpUserName("");
        AddWorkerService.startActionAddWorker(this, worker);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data == null)
            return;
        if (requestCode == 1) {
            worker.setFinger(data.getStringExtra("finger"), 0);
            btnFinger0.setText("指纹一（已采集）");
            btnFinger0.setTextColor(getResources().getColor(R.color.white));
            btnFinger0.setBackgroundResource(R.drawable.green_btn_bg);
        } else if (requestCode == 2) {
            worker.setFinger(data.getStringExtra("finger"), 1);
            btnFinger1.setText("指纹二（已采集）");
            btnFinger1.setTextColor(getResources().getColor(R.color.white));
            btnFinger1.setBackgroundResource(R.drawable.green_btn_bg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommExecEvent(CommExecEvent e) {
        if (e.getCommCode() == CommExecEvent.COMM_ADD_WORKER) {
            switch (e.getResult()) {
                case CommExecEvent.RESULT_SUCCESS:
                    pdAddWorker.dismiss();
                    break;
                case CommExecEvent.RESULT_EXCEPTION:
                    pdAddWorker.setMessage("添加员工异常!");
                    pdAddWorker.setCancelable(true);
                    break;
                case CommExecEvent.RESULT_SOCKET_NULL:
                    pdAddWorker.setMessage("网络连接失败，请检查ip地址、端口号!");
                    pdAddWorker.setCancelable(true);
                    break;
                default:
                    pdAddWorker.setMessage("添加失败!");
                    pdAddWorker.setCancelable(true);
                    // TODO: 2017/12/11
            }
        }
    }

}
