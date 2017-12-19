package com.sx.wx.recy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProviderActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.tv_photo)
    TextView mTvPhoto;
    private StorageManager mStorageManager;
    /**
     * 拍摄照片请求码
     **/
    private static final int REQUEST_CODE = 0x1;
    private static final int RESULT_CAPTURE_IMAGE = REQUEST_CODE + 1;
    private static final int REQUEST_CODE_GRAINT_URI = REQUEST_CODE + 2;
    /**
     * 读取sdcard请求码
     **/
    private static final int REQUEST_CODE_PERMISSION_READ_OR_WRITE = 0x111;

    private static final int REQUEST_CODE_PERMISSION = 0x110;
//    // 所需的全部权限
//    static final String[] PERMISSIONS = new String[]{
//            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        ButterKnife.bind(this);
//        if (Build.VERSION.SDK_INT > 23) {
//            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_PERMISSION);
//        }
    }

    @OnClick(R.id.tv_photo)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_photo:
                if (Build.VERSION.SDK_INT > 23) {
                    initPermissionForCamera();
                }
            break;
            case R.id.tv_install:
                initPermissionForReadOrWrite();

                break;
            default:
                break;
        }

    }

    private void initPermissionForReadOrWrite() {
        int flag = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_GRANTED != flag) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_OR_WRITE);
        } else {

//            if (mFileRoot != null)
//                installApk(mFileRoot);
//            else
//                installApk(new File(mPath));
        }
    }

    private void initPermissionForCamera() {
        int flag = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (PackageManager.PERMISSION_GRANTED != flag) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Build.VERSION.SDK_INT > 23) {
                /**Android 7.0以上的方式**/
                mStorageManager = this.getSystemService(StorageManager.class);
                StorageVolume storageVolume = mStorageManager.getPrimaryStorageVolume();
                Intent intent = storageVolume.createAccessIntent(Environment.DIRECTORY_DOWNLOADS);
                startActivityForResult(intent, REQUEST_CODE_GRAINT_URI);
            }
        } else {
            Toast.makeText(this, "sdcard不存在", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_PERMISSION == requestCode) {
            switch (grantResults[0]) {
                case PackageManager.PERMISSION_DENIED:
                    boolean isSecondRequest = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
                    if (isSecondRequest)
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
                    else {
                        Toast.makeText(this, "拍照权限被禁用，请在权限管理修改", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PackageManager.PERMISSION_GRANTED:
                    takePhoto();
                    break;
            }
        } else if (REQUEST_CODE_PERMISSION_READ_OR_WRITE == requestCode) {
            switch (grantResults[0]) {
                case PackageManager.PERMISSION_DENIED:
                    boolean isSecondRequest = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (isSecondRequest)
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_OR_WRITE);
                    else
                        Toast.makeText(this, "数据写入应用权限被禁用，请在权限管理修改", Toast.LENGTH_SHORT).show();
                    break;
                case PackageManager.PERMISSION_GRANTED:
//                    if (mFileRoot != null)
//                        installApk(mFileRoot);
//                    else
//                        installApk(new File(mPath));
                    break;
            }
        }
    }
}
