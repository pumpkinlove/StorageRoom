package com.miaxis.storageroom.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miaxis.storageroom.R;
import com.miaxis.storageroom.app.Storage_App;
import com.miaxis.storageroom.bean.Worker;
import com.miaxis.storageroom.event.CommExecEvent;
import com.miaxis.storageroom.greendao.GreenDaoManager;
import com.miaxis.storageroom.greendao.gen.WorkerDao;
import com.miaxis.storageroom.view.custom.SimpleDialog;
import com.miaxis.storageroom.view.fragment.EscortManageFragment;
import com.miaxis.storageroom.view.fragment.TaskExecFragment;
import com.miaxis.storageroom.view.fragment.TaskListFragment;
import com.miaxis.storageroom.view.fragment.WorkerListFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainPlateActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_plate);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_task_exec);
        toolbar.setTitle(R.string.task_exec);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new TaskExecFragment()).commit();

        initHeadView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final SimpleDialog sd = new SimpleDialog();
            sd.setMessage("您确定要退出吗？");
            sd.setConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            sd.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sd.dismiss();
                }
            });
            sd.show(getFragmentManager(), "logOut");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_plate, menu);
        return true;
    }

    private static final int REQUEST_CODE_CONFIG = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(this, SettingActivity.class), REQUEST_CODE_CONFIG);
            return true;
        } else if (id == R.id.action_add_worker) {
            startActivity(new Intent(this, AddWorkerActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_task_exec:
                toolbar.setTitle(R.string.task_exec);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new TaskExecFragment()).commit();
                break;
            case R.id.nav_worker_list:
                toolbar.setTitle(R.string.worker_list);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new WorkerListFragment()).commit();
                break;
//            case R.id.nav_escort_list:
//                toolbar.setTitle(R.string.escort_list);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new EscortFragment()).commit();
//                break;
            case R.id.nav_store_escort_list:
                toolbar.setTitle(R.string.store_escort_manage);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new EscortManageFragment()).commit();
                break;
            case R.id.nav_task_list:
                toolbar.setTitle(R.string.task_list);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new TaskListFragment()).commit();
                break;
//            case R.id.nav_box_list:
//                toolbar.setTitle(R.string.box_list);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new BoxListFragment()).commit();
//                break;
            case R.id.nav_about:
                break;
            case R.id.nav_sign_out:
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initHeadView() {
        Storage_App app = (Storage_App) getApplication();
        Worker curWorker = app.getCurWorker();
        if (curWorker == null) {
            Toast.makeText(this, "登入库管员为空，请重新登入", Toast.LENGTH_SHORT).show();
        } else {
            TextView tvCurWorkerName = (TextView) navView.getHeaderView(0).findViewById(R.id.tv_curWorker_name);
            TextView tvCurWorkerCode = (TextView) navView.getHeaderView(0).findViewById(R.id.tv_curWorker_code);
            tvCurWorkerName.setText(curWorker.getName());
            tvCurWorkerCode.setText(curWorker.getCode());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommExecEvent(CommExecEvent e) {
        Storage_App app = (Storage_App) getApplication();
        Worker curWorker = app.getCurWorker();
        if (curWorker == null && e.getCommCode() == CommExecEvent.COMM_ADD_WORKER) {
            if (e.getResult() == CommExecEvent.RESULT_SUCCESS) {
                GreenDaoManager manager = GreenDaoManager.getInstance(this);
                try {
                    WorkerDao workerDao = manager.getWorkerDao();
                    curWorker = workerDao.loadAll().get(0);
                    app.setCurWorker(curWorker);
                    TextView tvCurWorkerName = (TextView) navView.getHeaderView(0).findViewById(R.id.tv_curWorker_name);
                    TextView tvCurWorkerCode = (TextView) navView.getHeaderView(0).findViewById(R.id.tv_curWorker_code);
                    tvCurWorkerName.setText(curWorker.getName());
                    tvCurWorkerCode.setText(curWorker.getCode());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_CONFIG == requestCode) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
