package cn.edu.gdmec.android.mobileguard.m1home.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import static android.content.Context.DOWNLOAD_SERVICE;
//下载apk的方法
public class DownloadUtils {
    public void downloadApk(String url,String targetFile,Context context){
        //通过url获得系统下载管理 downloadManager.Request用来请求一个下载
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //setAllowedOverRoaming用于设置漫游状态下是否可以下载
        request.setAllowedOverRoaming(false);
        //设置文件类型，可以在下载结束后自动打开该文件
        //MimeTypeMap作用是告诉Android系统本Activity可以处理的文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);//设置下载文件类型因为下载管理UI中点击某个已下载完成文件及下载完成点击通知栏提示都会根据
        //mimeType去打开文件，所以我们利用这个属性，用于设置下载时在状态栏显示通知信息。
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
        //用于设置下载的存放路径
        request.setDestinationInExternalPublicDir("/download",targetFile);
        //获得系统的下载服务 request.setDestinationInExternalFileDir,可以自己制定下载路径
        DownloadManager downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //通过该id可取消任务，重启任务等
        Long mTaskId = downloadManager.enqueue(request);
    }
}
