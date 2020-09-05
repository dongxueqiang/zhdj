package com.zhdj.zhdj.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zhdj.zhdj.base.BaseViewModel;
import com.zhdj.zhdj.retrofit.RetrofitUtils;
import com.zhdj.zhdj.rxjava.CommonSchedulers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * @ClassName UploadViewModel
 * @Description TODO
 * @Author dongxueqiang
 * @Date 2020/9/5 1:38 PM
 */
public class UploadViewModel extends BaseViewModel {

    public UploadViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void downloadFile(String url, String name) {
        RetrofitUtils.getApiService()
                .downFile(url)
                .compose(CommonSchedulers.observableIo2Main())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        byte[] bys;
                        Bitmap bitmap;
                        try {
                            bys = responseBody.bytes(); //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程

                            bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);
                            saveFile(bitmap, name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 保存图片到SD卡
     *
     * @param bm
     * @param name
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String name) throws IOException {
        //系统相册路径
        String dirName = getFileDirName();
        //随机生成不同的名字
        String fileName = name;
        //新创的文件夹的名字
        File jia = new File(dirName);
        //判断文件夹是否存在，不存在则创建
        if (!jia.exists()) {
            jia.mkdirs();
        }
        File file = new File(jia, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        bos.flush();
        bos.close();

        //其次把文件插入到系统图库
        //此处参考 https://blog.csdn.net/myjie0527/article/details/83096146
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getApplication().sendBroadcast(mediaScanIntent);

    }

    public static String getFileDirName() {
        return Environment.getExternalStorageDirectory() + File.separator + "zhdj";
    }
}
