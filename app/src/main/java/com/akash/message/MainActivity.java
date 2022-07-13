package com.akash.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText p_txt,m_txt;
    TextView ms;
    LinearLayout container;
    String phoneNumber;
    String recieved_message;
    private static final int my_permission_request = 0;
    boolean check = false;
    boolean check1 = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p_txt = (EditText) findViewById(R.id.phoneno);
        m_txt = (EditText) findViewById(R.id.message);
        container = (LinearLayout)findViewById(R.id.container);
        //     ms = (TextView) findViewById(R.id.view);


        //for sms receiver permission
        //check if permission granted or not__________________
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS))
            {
                //do nothing as the user has denied the permission
            }
            else
            {
                //a pop-up message will appear asking permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, my_permission_request);

            }
        }
        //_______________________________________________________

    }

    private MyReceiver mServiceReceiver = new MyReceiver(){
        @Override
        public void onReceive(Context context, Intent intent)
        {

            String newsms="";
            phoneNumber=intent.getStringExtra("incomingPhoneNumber");
            recieved_message=intent.getStringExtra("message");
            String smsr= recieved_message;
            p_txt.setText(phoneNumber);
            if(recieved_message.equals("Message Delivered.....!"))
            {
                Toast.makeText(context, "Message Delivered.....!"+phoneNo, Toast.LENGTH_LONG).show();

            }
            else if(newsms!=smsr) {
                displaysms(smsr,phoneNumber+" : ");
                    SmsManager smsrr = SmsManager.getDefault();
                    smsrr.sendTextMessage(phoneNumber, null, "Message Delivered.....!", null, null);
            }
        }


    };
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if(mServiceReceiver != null){
                unregisterReceiver(mServiceReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SmsReceiver");
        registerReceiver(mServiceReceiver , filter);
    }

    public void btn_send(View view) {

        int check = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(check == PackageManager.PERMISSION_GRANTED)
        {
            MyMessage();
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);
        }

    }

    private void MyMessage() {

        String ph_no = p_txt.getText().toString().trim();
        String mess = m_txt.getText().toString().trim();

        if(!p_txt.getText().toString().equals("")||!m_txt.getText().toString().equals(""))
        {
            SmsManager sms = SmsManager.getDefault();

            sms.sendTextMessage(ph_no,null,mess,null,null);
            //       ms.setText(mess);
            m_txt.setText("");
            Toast.makeText(this,"SMS Sent",Toast.LENGTH_SHORT).show();
            displaysms(mess,"Me : ");

        }
        else
        {
            Toast.makeText(this,"Enter Valid Detail",Toast.LENGTH_SHORT).show();
        }

    }

    private void displaysms(String mess, String sq) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addview = inflater.inflate(R.layout.row, null);
        TextView txtvalue = (TextView) addview.findViewById(R.id.textcontent);
        TextView txtteci = (TextView) addview.findViewById(R.id.textcontentreci);
        txtvalue.setText(mess);
        txtteci.setText(sq);
        container.addView(addview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 0:
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    MyMessage();
                }
                else
                {
                    Toast.makeText(this,"You Don't Have Required Permission",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

