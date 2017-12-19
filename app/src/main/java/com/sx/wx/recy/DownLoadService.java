package com.sx.wx.recy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

public class DownLoadService extends Service {
    private DownLoadTask mDownLoadTask;
    private String mDownLoadUrl;

    public DownLoadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private DownLoadListener mListener = new DownLoadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            mDownLoadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("download success", -1));
            Toast.makeText(DownLoadService.this, "Download success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            mDownLoadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("DownLoad failed", -1));
            Toast.makeText(DownLoadService.this, "DownLoad Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            mDownLoadTask = null;
            Toast.makeText(DownLoadService.this, "Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            mDownLoadTask = null;
            stopForeground(true);
            Toast.makeText(DownLoadService.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    private NotificationManager getNotificationManager() {

        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

    class DownloadBinder extends Binder {
        public void startDownLoad(String url) {
            if (mDownLoadTask == null) {
                mDownLoadUrl = url;
                mDownLoadTask = new DownLoadTask(mListener);
                mDownLoadTask.execute(mDownLoadUrl);
                startForeground(1,getNotification("DownLoading...",0));
                Toast.makeText(DownLoadService.this, "DownLoading...", Toast.LENGTH_SHORT).show();
            }
        }
        public void pauseDownLoad(){
            if (mDownLoadTask!= null) {
                mDownLoadTask.pauseDownLoad();
            }
        }
        public void cancelDownLoad(){
            if (mDownLoadTask!=null) {
                mDownLoadTask.cancelDownLoad();
            }
            if (mDownLoadUrl!=null) {
                String fileName = mDownLoadUrl.substring(mDownLoadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(DownLoadService.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
