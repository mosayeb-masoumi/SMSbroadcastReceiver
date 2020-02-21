package com.example.smsbroadcastreceiver;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {


      MyReceiver myReceiver = new MyReceiver();

      EditText edtSms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSms = findViewById(R.id.edtSmsCode);

       // check permission granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            askSMSreceivedPermission();

        }else{
//            Toast.makeText(this, "has SMS received permission", Toast.LENGTH_SHORT).show();
        }
    }


   // to get sms from receiver and set in editText
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(String sms_received) {
       edtSms.setText(sms_received);
    }




    private void askSMSreceivedPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.RECEIVE_SMS}, 1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1){
            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "need permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    // we register broadcast dynamically below here because just when our app is open we will received sms
    // but if we register it in manifest we will received message while our app is closed too

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter  = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(myReceiver,filter);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(myReceiver);
        EventBus.getDefault().unregister(this);
    }
}
