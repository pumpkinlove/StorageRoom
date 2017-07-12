package com.miaxis.storageroom.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.service.AddWorkerService;
import com.miaxis.storageroom.util.DateUtil;

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

    private Worker worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);
        ButterKnife.bind(this);
        initToolBar();
        worker = new Worker();
    }

    private void initToolBar() {
        toolbar.setTitle(R.string.action_add_worker);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

}
