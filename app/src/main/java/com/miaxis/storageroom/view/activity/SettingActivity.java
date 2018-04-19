package com.miaxis.storageroom.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.view.fragment.ConfigFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity implements ConfigFragment.OnConfigClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initToolBar();
    }

    private void initToolBar() {
        toolbar.setTitle(R.string.action_settings);
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

    @Override
    public void onConfigSave() {
        finish();
    }

    @Override
    public void onConfigCancel() {
        finish();
    }

    @Override
    public void onInit() {
        setResult(RESULT_OK);
        finish();
    }
}
