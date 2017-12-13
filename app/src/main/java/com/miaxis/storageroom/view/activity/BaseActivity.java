package com.miaxis.storageroom.view.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.miaxis.storageroom.greendao.gen.ConfigDao;

/**
 * Created by xu.nan on 2017/7/10.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
