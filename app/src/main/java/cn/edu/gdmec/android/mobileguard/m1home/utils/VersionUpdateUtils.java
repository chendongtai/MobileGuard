package cn.edu.gdmec.android.mobileguard.m1home.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Switch;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.PublicSuffixDomainFilter;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m1home.entity.VersionEntity;

/**
 * Created by Administrator on 2017/9/24.
 */
//获取版本号 对比版本号 下载更新
public class VersionUpdateUtils {
    private static final int MESSAGE_IO_ERROR = 102;//网络错误代号
    private static final int MESSAGE_JSON_ERROR = 103;//JSON错误代号
    private static final int MESSAGE_SHOW_ERROR = 104;//SHOW错误代号
    private static final int MESSAGE_ENTERHOME = 105;//HOME错误代号
    private String mVersion;
    private Activity context;
    private VersionEntity versionEntity;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MESSAGE_IO_ERROR:
                    Toast.makeText(context,"IO错误",Toast.LENGTH_LONG).show();
                    //测试后，网络错误也进入主界面
                    Intent intent1 = new Intent(context,HomeActivity.class);
                    context.startActivity(intent1);
                    context.finish();
                    break;
                case MESSAGE_JSON_ERROR:
                    Toast.makeText(context,"JSON解析错误",Toast.LENGTH_LONG).show();
                    break;
                case MESSAGE_SHOW_ERROR:
                   //显示升级对话框
                   showUpdateDialog(versionEntity);
                    break;
                case MESSAGE_ENTERHOME:
                    Intent intent =  new Intent(context,HomeActivity.class);
                    context.startActivity(intent);
                    context.finish();

            }
        }

    };

    public VersionUpdateUtils(String mVersion,Activity context){
        this.mVersion = mVersion;
        this.context = context;
    }
    public void getCloudVersion(){
        try {
            HttpClient httpclient = new DefaultHttpClient();
            //设置超时
            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),5000);
            HttpConnectionParams.setSoTimeout(httpclient.getParams(),5000);
            //请求链接
            HttpGet httpGet = new HttpGet("http://android2017.duapp.com/updateinfo.html");
            //执行
            HttpResponse execute = httpclient.execute(httpGet);
            //比对返回码200位成功
            if (execute.getStatusLine().getStatusCode()==200){
                //获取服务器返回的内容并处理
                HttpEntity httpEntity = execute.getEntity();
                String result = EntityUtils.toString(httpEntity,"utf-8");
                JSONObject jsonObject = new JSONObject(result);
                versionEntity = new VersionEntity();
                versionEntity.versioncode = jsonObject.getString("code");
                versionEntity.description = jsonObject.getString("des");
                versionEntity.apkurl = jsonObject.getString("apkurl");
                if (!mVersion.equals(versionEntity.versioncode)){
                    //版本不同，需要升级
//                    System.out.println(versionEntity.description);
//                    DownloadUtils downloadUtils = new DownloadUtils();
//                    downloadUtils.downloadApk(versionEntity.apkurl,"mobileguard.apk",context);
                    handler.sendEmptyMessage(MESSAGE_SHOW_ERROR);
                }else{
                    enterHome();
                }
            }
        }catch (IOException e){
            //io异常
            e.printStackTrace();
        }catch (JSONException e){
            //json异常
            e.printStackTrace();
        }
    }
    private void showUpdateDialog(final VersionEntity versionEntity){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("检查到有新版本："+versionEntity.versioncode);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setPositiveButton("立即升级",(dialog）
    }
}
