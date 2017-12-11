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
import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.service.EscortManageService;
import com.miaxis.storageroom.util.DateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EscortManageActivity extends BaseActivity {

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

    private Escort mEscort;
    private ProgressDialog pdAddEscort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_escort);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar();
        mEscort = new Escort();
        pdAddEscort = new ProgressDialog(this);
        pdAddEscort.setMessage("正在上传押运员...");
        pdAddEscort.setCancelable(false);
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
        if (TextUtils.isEmpty(mEscort.getFinger0())) {
            Toast.makeText(this, "指纹一为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mEscort.getFinger1())) {
            Toast.makeText(this, "指纹二为空", Toast.LENGTH_SHORT).show();
            return;
        }
        pdAddEscort.show();
        mEscort.setName(etWorkerName.getText().toString());
        mEscort.setCode("xd-" + etWorkerCode.getText());
        mEscort.setOpDate(DateUtil.toAll(new Date()));
        EscortManageService.startActionUpdateEscort(this, mEscort);
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
    public void onAddEscortEvent(AddWorkerEvent e) {
        if (e.getResult() == AddWorkerEvent.SUCCESS) {
            pdAddEscort.dismiss();
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            pdAddEscort.setMessage("添加失败!");
            pdAddEscort.setCancelable(true);
        }
    }

}
