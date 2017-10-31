package cn.edu.gdmec.android.mobileguard;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cn.edu.gdmec.android.mobileguard.m1home.HomeActivity;
import cn.edu.gdmec.android.mobileguard.m1home.utils.MyUtils;
import cn.edu.gdmec.android.mobileguard.m1home.utils.VersionUpdateUtils;

public class SplashActivity extends AppCompatActivity {
    private TextView mTvVersion;
    private String mVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       //读xml分析，把xml的布局 对象new出来，自动把东西自动创建完。各个xx都变成对象
        mVersion = MyUtils.getVersion(getApplicationContext());
        mTvVersion = (TextView) findViewById(R.id.tv_splash_version);
        //把tv_splash_version取出来之后，实例化
        mTvVersion.setText("版本号："+mVersion);
        final VersionUpdateUtils versionUpdateUtils = new VersionUpdateUtils(mVersion,SplashActivity.this);
//        new Thread(){
//         //用户ui主线程不允许网络访问，造成app挂起，造成app卡死。例如不能打电话之类的
//            @Override
//            public void run(){
//                super.run();
//                versionUpdateUtils.getCloudVersion();
//                //versionUpdateUtils类，处在线程的位置
//            }
//        }.start();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
       /* protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivityAndFinishSelf(HomeActivity.class);
        }
        public void startActivityAndFinishSelf(Class<?> cls) {
            Intent intent = new Intent(this, cls);
            startActivity(intent);
            finish();
            还有另一种方法，在maifest中修改homeactvity为主界面
        }*/
    }
}
