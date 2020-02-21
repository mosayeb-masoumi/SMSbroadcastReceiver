package com.example.smsbroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class MyReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";

    String msg,phoneNo = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "intentReceive: " + intent.getAction());

        if (intent.getAction()==SMS_RECEIVED){

            Bundle dataBundle = intent.getExtras();

            if(dataBundle!=null){

                Object[] mypdu = (Object[]) dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];

                for (int i = 0; i <mypdu.length ; i++) {


                    // for build versions  >= API level 23
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                        String format = dataBundle.getString("format");
                        //from PDU we will get all objects and SmsMessage Object using following line of code
                        message[i]  = SmsMessage.createFromPdu((byte[])mypdu[i],format);
                    }
                    else{
                         //<API level 23
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }

                    msg = message[i].getMessageBody();
                    phoneNo = message[i].getOriginatingAddress();

                    // to set sms in editText
                    String sms_received = msg;
                    EventBus.getDefault().postSticky(sms_received);
                    
                }

                Toast.makeText(context, "Message : "+msg + "\nNumber: "+phoneNo, Toast.LENGTH_LONG).show();

            }

        }

    }
}
