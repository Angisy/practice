package com.sx.wx.recy;

/**
 * Created by wx on 2017/11/9.
 */

public interface DownLoadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}
