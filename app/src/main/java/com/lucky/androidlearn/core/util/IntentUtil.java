package com.lucky.androidlearn.core.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by zfz on 2017/12/24.
 */

public class IntentUtil {

    private void sendEmail(Context context) {
        Uri uri = Uri.parse("mailto:zhangfengzhou@aragoncs.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        context.startActivity(intent);
    }

    private void sendEmails(Context context) {
        Intent returnIt = new Intent(Intent.ACTION_SEND);
        String[] tos = {"shenrenkui@gmail.com"};
        String[] ccs = {"shenrenkui@gmail.com"};
        returnIt.putExtra(Intent.EXTRA_EMAIL, tos);
        returnIt.putExtra(Intent.EXTRA_CC, ccs);
        returnIt.putExtra(Intent.EXTRA_TEXT, "body");
        returnIt.putExtra(Intent.EXTRA_SUBJECT, "subject");
        returnIt.setType("message/rfc882");
        Intent.createChooser(returnIt, "Choose Email Client");
        context.startActivity(returnIt);
    }

    protected void installApk(Context context, File file) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
