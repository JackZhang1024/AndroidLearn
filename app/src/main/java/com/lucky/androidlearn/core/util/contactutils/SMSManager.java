package com.lucky.androidlearn.core.util.contactutils;

import android.telephony.SmsManager;

import java.util.ArrayList;


/**
 * 发送短信给指定的手机号码
 *
 * @author administor
 */
public class SMSManager {
    /**
     * @param msgcontent 发送短信的内容
     */
    public static void sendMessage(String phoneNumber, String msgcontent) {
        SmsManager smsmanger = SmsManager.getDefault();
        ArrayList<String> msgs = smsmanger.divideMessage(msgcontent);
        for (String msg : msgs) {
            smsmanger.sendTextMessage(phoneNumber, null, msg, null, null);
        }
    }

}
