package com.akash.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    private static final String sms_received = "android.provider.Telephony.SMS_RECEIVED";
    private static final String tag = "SmsBroadcastReceiver";
    String msg, phoneNo = "";


    @Override
    public void onReceive(Context context, Intent intent) {
        // retrieves the general action to be performed and displays
        Log.i(tag, "Intent Received : " + intent.getAction());

        if(intent.getAction()==sms_received)
        {
            Bundle dataBundle = intent.getExtras();
            if(dataBundle!=null)
            {
                Object[] mypdu = (Object[])dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];

                for (int i = 0; i < mypdu.length; i++)
                {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    {
                        String format = dataBundle.getString("format");
                        message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], format);
                    }
                    else
                    {
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }
                    msg = message[i].getMessageBody();
                    phoneNo = message[i].getOriginatingAddress();
                }



                Intent i = new Intent("android.intent.action.SmsReceiver").putExtra("incomingPhoneNumber", phoneNo);
                i.putExtra("message",msg);
                context.sendBroadcast(i);
                phoneNo=null;


            }
        }
    }
}