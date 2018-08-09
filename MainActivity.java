package com.wei.tqgetuinpwd;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tq.recorder.AIDLUserInfo;

public class MainActivity extends AppCompatActivity {

    private AIDLUserInfo aidlUserInfo;
    private ServiceConnection serviceConnection;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv);
    }

    @Override
    protected void onResume() {
        super.onResume();

        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(serviceConnection);
    }

    private void bindService(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                aidlUserInfo = AIDLUserInfo.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                aidlUserInfo = null;
            }
        };
        Intent intent=new Intent();
        intent.setAction("com.tq.recorder.GetUserInfoAIDLService");
        intent.setPackage("com.tq.recorder");
        this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void getUinPwd(View view) {
        try {
            if(aidlUserInfo!=null){
                textView.setText(aidlUserInfo.getUinPwd());
                Log.d("MainActivity", aidlUserInfo.getUinPwd());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
