package cn.edu.gdmec.android.mobileguard.m1home;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m1home.adapter.HomeAdapter;
import cn.edu.gdmec.android.mobileguard.m2theftguard.LostFindActivity;
import cn.edu.gdmec.android.mobileguard.m2theftguard.dialog.InterPasswordDialog;
import cn.edu.gdmec.android.mobileguard.m2theftguard.dialog.SetupPasswordDialog;
import cn.edu.gdmec.android.mobileguard.m2theftguard.receiver.MyDeviceAdminReceiver;
import cn.edu.gdmec.android.mobileguard.m2theftguard.utils.MD5Utils;

public class HomeActivity extends AppCompatActivity {
    private GridView gv_home;
    private long mExitTime;
    //储存手机防盗密码的sp
    private SharedPreferences msharedPreferences;
    //设备管理员
    private DevicePolicyManager policyManager;
    //申请权限
    private ComponentName componentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gv_home = (GridView) findViewById(R.id.gv_home);
        msharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0://点击手机防盗
                        if (isSetUpPassword()){
                            //弹出输入密码对话框
                            showInterPswdDialog();
                        }else{
                            //弹出设置密码对话框
                            showSetUpPswDialog();
                        }
                        break;
                }
            }
        });
        //获取设备管理员
        policyManager = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        //本行代码需要“手机防盗模块”完成后才能启用
        //2.申请权限，MyDeviceAdminReceiver继承自DeviceAdminReceiver
        componentName = new ComponentName(this, MyDeviceAdminReceiver.class
        );
        //3.判断呢，如果没有权限则申请权限
        boolean active = policyManager.isAdminActive(componentName);
        if (!active){
            ///没有管理员的权限，则获取管理员的权限
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"获取超级管理员的权限，用于远程锁屏和清除数据");
            startActivity(intent);
        }
    }
    public void startActivity(Class<?> cls){
        Intent intent = new Intent(HomeActivity.this,cls);
        startActivity(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if ((System.currentTimeMillis()-mExitTime)<2000){
                System.exit(0);
            }else{
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_LONG).show();
                mExitTime = System.currentTimeMillis();
            }return true;
        }return super.onKeyDown(keyCode,event);
    }
    //弹出设置密码对话框  本方法需要完成“手机防盗模块”之后才能启用
    private void showSetUpPswDialog(){
        final SetupPasswordDialog setUpPasswordDialog = new SetupPasswordDialog(HomeActivity.this);
        setUpPasswordDialog.setCallBack(new SetupPasswordDialog.MyCallBack(){
            @Override
            public void ok() {
                String firstPswd = setUpPasswordDialog.mFirstPWDET.getText().toString().trim();
                String affirmPswd = setUpPasswordDialog.mAffirmET.getText().toString().trim();
                if (!TextUtils.isEmpty(firstPswd)&&!TextUtils.isEmpty(affirmPswd)){
                    if (firstPswd.equals(affirmPswd)){
                        //两次密码一致，储存密码
                        savePswd(affirmPswd);
                        setUpPasswordDialog.dismiss();
                        //显示输入密码对话框
                       /* showInterPswd(affirmPswd);
                        setUpPasswordDialog.dismiss();*/
                        showInterPswdDialog();
                    }else{
                        Toast.makeText(HomeActivity.this,"两次密码不一致！",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(HomeActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void cancel(){
                setUpPasswordDialog.dismiss();
            }
        });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();
    }
    //弹出输入密码对话框 本方法需要完成“手机防盗模块”之后才能启用
    private void showInterPswdDialog(){
        final String password = getPassword();
        final InterPasswordDialog mInPswdDialog = new InterPasswordDialog
                (HomeActivity.this);
        mInPswdDialog.setCallBack(new InterPasswordDialog.MyCallBack(){
            @Override
            public void confirm() {
                if (TextUtils.isEmpty(mInPswdDialog.getPassword())){
                    Toast.makeText(HomeActivity.this,"密码不能为空！",Toast.LENGTH_LONG).show();
                }else if (password.equals(MD5Utils.encode(mInPswdDialog.getPassword()))){
                    mInPswdDialog.dismiss();
                    //进入防盗主界面
                    mInPswdDialog.dismiss();
                    startActivity(LostFindActivity.class);
//                    Toast.makeText(HomeActivity.this,"可以进入手机防盗模块",Toast.LENGTH_LONG).show();

                }else{
                    //对话框消失，弹出Toast
                    mInPswdDialog.dismiss();
                    Toast.makeText(HomeActivity.this,"密码有误，请重新输入！",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void cancel() {
                mInPswdDialog.dismiss();
            }
        });
        mInPswdDialog.setCancelable(true);
        //让对话框显示
        mInPswdDialog.show();
    }
    //保存密码 本方法需要完成“手机防盗模块”之后才能启用
    //@param affirmPswd
    private void savePswd(String affirmPswd){
        SharedPreferences.Editor edit = msharedPreferences.edit();
        //为了防止用户隐私被泄露，因此需要加密密码
        edit.putString("PhoneAntiTheftPWD",MD5Utils.encode(affirmPswd));
        edit.commit();
    }
    //获取密码
    //@return sp存储的密码
    private String getPassword(){
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if (TextUtils.isEmpty(password)){
            return "";
        }
        return password;
    }
    ///判断用户是否设置手机防盗密码
    private boolean isSetUpPassword(){
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }
}
