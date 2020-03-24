package com.lucky.androidlearn.widget.common.helper;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by zfz on 2017/12/24.
 */

public class AlertDialogHelper {

    public void showAlert(Context context, String title, String message) {
        showAlert(context, title, message, "确定", "取消", null);
    }

    public void showAlert(Context context, String title, String message, AlertDialogHelper.AlertDialogInterface onButtonClickListener) {
        showAlert(context, title, message, "确定", "取消", onButtonClickListener);
    }

    public void showAlert(Context context, String title, String message, String positiveButtonText, String negativeButtonText,
                          AlertDialogHelper.AlertDialogInterface onButtonClickListener) {
        showDialog(context, title, message, positiveButtonText, negativeButtonText, onButtonClickListener);
    }

    private void showDialog(Context context, String title, String message, String positiveButtonText,
                            String negativeButtonText, final AlertDialogHelper.AlertDialogInterface onButtonClickListener) {
        AlertDialog.Builder v7DialogBuilder = new AlertDialog.Builder(context);
        v7DialogBuilder.setTitle(title);
        v7DialogBuilder.setMessage(message);
        v7DialogBuilder.setCancelable(true);
        v7DialogBuilder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onPositiveButtonClick();
                }
            }
        });
        v7DialogBuilder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onNegativeButtonClick();
                }
            }
        });
        v7DialogBuilder.show();
    }

    private void showOldDialog(Context context, String title, String message) {
        android.app.AlertDialog.Builder v7DialogBuilder = new android.app.AlertDialog.Builder(context);
        v7DialogBuilder.setTitle(title);
        v7DialogBuilder.setMessage(message);
        v7DialogBuilder.setCancelable(true);
        v7DialogBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        v7DialogBuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        v7DialogBuilder.show();
    }

    public interface AlertDialogInterface {
        void onPositiveButtonClick();

        void onNegativeButtonClick();
    }
}
