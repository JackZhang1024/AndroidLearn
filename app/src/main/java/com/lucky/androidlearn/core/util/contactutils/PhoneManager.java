package com.lucky.androidlearn.core.util.contactutils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PhoneManager {

    public static void callPhone(Context context, String phoneNumber) {
        try {
            Uri uri = Uri.parse(String.format("tel:%s", phoneNumber));
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            context.startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
