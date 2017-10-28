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

        mVersion = MyUtils.getVersion(getApplicationContext());
        mTvVersion = (TextView) findViewById(R.id.tv_splash_version);
        mTvVersion.setText("版本号："+mVersion);
        final VersionUpdateUtils versionUpdateUtils = new VersionUpdateUtils(mVersion,SplashActivity.this);
       /* new Thread(){
            @Override
            public void run(){
                super.run();
                versionUpdateUtils.getCloudVersion();
            }
        }.start();*/
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
        //创建一个信使intent，将要启动的activity传进去，启动activity，然后finish掉自己的activity。
        //也可以在mainfest.xml文件中的<intent-filter>
                //<action android:name="android.intent.action.MAIN"/>
                //<category android:name="android.intent.category.LAUNCHER"/>
            //</intent-filter>
        //将这部分代码打进 <activity android:name=".m1home.HomeActivity">之间<activity/>，此方法较为简单
    }
}
