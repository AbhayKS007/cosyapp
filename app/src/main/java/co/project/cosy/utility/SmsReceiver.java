package co.project.cosy.utility;

/**
 * Created by Saurabh Saini on 7/22/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

    public static final String ACTION_UNREAD_SMS = "com.in.mikuapp.miku.ACTION_UNREAD_SMS";
    public static final String EXTRA_SMS_BODY = "com.in.mikuapp.miku.EXTRA_SMS_BODY";
    public static final String EXTRA_SMS_ADDRESS = "com.in.mikuapp.miku.EXTRA_SMS_ADDRESS";
    public static final String SMS_BUNDLE = "pdus";
    public static final String PRE_ADDRESS = "03590000004";
    String smsBody;
    String address;

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage
                        .createFromPdu((byte[]) sms[i]);

                smsBody = smsMessage.getMessageBody().toString();
                address = smsMessage.getOriginatingAddress();
                if (PRE_ADDRESS.equals(address)) {

                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody + "\n";
                }
            }

            // Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
            Intent smsIntent = new Intent(ACTION_UNREAD_SMS);
            smsIntent.putExtra(EXTRA_SMS_BODY, smsBody);
            smsIntent.putExtra(EXTRA_SMS_ADDRESS, address);
            context.sendBroadcast(smsIntent);
        }
    }
}

