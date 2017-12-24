package com.lucky.androidlearn.widget.common.helper;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucky.androidlearn.R;


/**
 * Created by zfz on 2017/12/24.
 */

public class BottomDialogHelper {

    private Dialog mBottomDialog;

    public BottomDialogHelper(Context context, OnBottomDialogItemClick onBottomDialogItemClick) {
        initBottomDialog(context, onBottomDialogItemClick);
    }

    private void initBottomDialog(Context context, final OnBottomDialogItemClick onBottomDialogItemClick) {
        mBottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_picture_selector, null);
        mBottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        mBottomDialog.setCanceledOnTouchOutside(true);
        contentView.findViewById(R.id.picture_selector_take_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBottomDialogItemClick != null) {
                    onBottomDialogItemClick.onTakePhotoClick();
                }
            }
        });
        contentView.findViewById(R.id.picture_selector_pick_picture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBottomDialogItemClick != null) {
                    onBottomDialogItemClick.onPickPhotoClick();
                }
            }
        });
        contentView.findViewById(R.id.picture_selector_pick_system_picture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBottomDialogItemClick != null) {
                    onBottomDialogItemClick.onPickPreSetPhotoClick();
                }
            }
        });
        contentView.findViewById(R.id.picture_selector_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBottomDialogItemClick != null) {
                    onBottomDialogItemClick.onCancelClick();
                }
            }
        });
    }

    public void show() {
        if (mBottomDialog != null && !mBottomDialog.isShowing()) {
            mBottomDialog.show();
        }
    }

    public void dismiss() {
        if (mBottomDialog != null && mBottomDialog.isShowing()) {
            mBottomDialog.dismiss();
        }
    }

    public interface OnBottomDialogItemClick {
        void onTakePhotoClick();

        void onPickPhotoClick();

        void onPickPreSetPhotoClick();

        void onCancelClick();
    }

}
