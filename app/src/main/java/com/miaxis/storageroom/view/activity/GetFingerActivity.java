package com.miaxis.storageroom.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.device.Device;
import com.miaxis.storageroom.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GetFingerActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.action_get_finger);
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

    private MessageHandler msgHandler = new MessageHandler(this);
    Thread regFingerThread = null;
    static ArrayList<String> fimg0 = null;
    static ArrayList<String> fimg1 = null;
    String idcard;
    String requestcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_get_finger);
        ButterKnife.bind(this);
        initToolBar();
        TextView view = (TextView) findViewById(R.id.info);
        fimg0=new ArrayList<String>();
        fimg1=new ArrayList<String>();
        view.setText("      注册一个指纹需按四次手指。");
        Intent intent = getIntent();
        idcard = intent.getStringExtra("idcard");
        requestcode= intent.getStringExtra("requestcode");
        regFingerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                RegFinger();
            }
        });
        regFingerThread.start();
    }

    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showImage(byte[] image) {
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        ImageView imgView = (ImageView) findViewById(R.id.imgView);
        imgView.setImageBitmap(bmp);
    }

    private void showTip(String tip) {
        TextView view = (TextView) findViewById(R.id.tip);
        view.setText(tip);
    }

    public void showThreadMessage(String message) {
        Message msg = new Message();
        msg.what = 0;
        msg.obj = message;
        msgHandler.sendMessage(msg);
    }

    public void showThreadImage(byte[] image) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = image;
        msgHandler.sendMessage(msg);
    }

    public void showThreadTip(String tip) {
        Message msg = new Message();
        msg.what = 2;
        msg.obj = tip;
        msgHandler.sendMessage(msg);
    }

    private String newGBKString(byte[] bytes) {
        try {
            return new String(bytes, "GBK");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public void closeThread(String finger) {
        Message msg = new Message();
        msg.what = 3;
        msg.obj = finger;
        msgHandler.sendMessage(msg);
    }

    private void RegFinger() {
        byte[] image = new byte[2000 + 152 * 200];
        byte[] message = new byte[100];
        byte[][] tz = new byte[3][256];
        byte[] mb = new byte[256];
//        Device.openFinger();
//        Device.openRfid();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        for (int i = 0; i < 3; i++) {
            showThreadTip("    采集指纹,请按手指(第" + (i + 1) + "次)...");
            int r = com.device.Device.getImage(10000, image, message);
            if (r != 0) {
                String str = newGBKString(message);
                showThreadMessage(str);
                showThreadTip(str);
                return;
            }
            showThreadImage(image);
            r = com.device.Device.ImageToFeature(image, tz[i], message);
            if (r != 0) {
                String str = newGBKString(message);
                showThreadMessage(str);
                showThreadTip(str);
                return;
            }
        }

        int r = Device.FeatureToTemp(tz[0], tz[1], tz[2], mb,
                message);
        if (r != 0) {
            String str = newGBKString(message);
            showThreadMessage(str);
            showThreadTip(str);
            return;
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        showThreadTip("    校验指纹,请按手指...");
        r = com.device.Device.getImage(10000, image, message);
        if (r != 0) {
            String str = newGBKString(message);
            showThreadMessage(str);
            showThreadTip(str);
            return;
        }
        showThreadImage(image);
        r = com.device.Device.ImageToFeature(image, tz[0], message);
        if (r != 0) {
            String str = newGBKString(message);
            showThreadMessage(str);
            showThreadTip(str);
            return;
        }

        r = com.device.Device.verifyBinFinger(mb, tz[0], 3);
        if (r != 0) {
            String str = "  指纹校验未通过。";
            showThreadMessage(str);
            showThreadTip(str);
            return;
        }
        String str = new String(Base64.encode(mb, Base64.NO_WRAP));
        showThreadTip("采集指纹成功!");
//        Device.closeFinger();
//        Device.closeRfid();
        closeThread(str);
    }

    public String graph(byte[] image,int i) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return "";
        }
        String name ="";
        if("0".equals(requestcode)){
            name = idcard + "f0" + i + ".jpg";
        }
        else if("1".equals(requestcode)){
            name = idcard + "f1" + i + ".jpg";
        }

        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        FileOutputStream b = null;
        File file = new File("/sdcard/jkpt/finger/");
        file.mkdirs();// 创建文件夹
        String photopath = "/sdcard/jkpt/finger/" + name;
        try {
            b = new FileOutputStream(photopath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            return photopath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btn_cancel)
    void OnCancelClick() {
        if (regFingerThread.isAlive()) {
            com.device.Device.cancel();
        } else {
            this.setResult(RESULT_CANCELED);
            this.finish();
        }
    }

    private static class MessageHandler extends Handler {
        WeakReference<GetFingerActivity> activityRef;

        public MessageHandler(GetFingerActivity activity) {
            activityRef = new WeakReference<GetFingerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                activityRef.get().showMessage((String) msg.obj);
            } else if (msg.what == 1) {
                activityRef.get().showImage((byte[]) msg.obj);
            } else if (msg.what == 3) {
                Intent intent = new Intent();
                intent.putExtra("finger", (String) msg.obj);
                intent.putStringArrayListExtra("fimg0", fimg0);
                intent.putStringArrayListExtra("fimg1", fimg1);
                activityRef.get().setResult(RESULT_OK, intent);
                activityRef.get().finish();
            } else {
                activityRef.get().showTip((String) msg.obj);
            }
        }
    }
}
