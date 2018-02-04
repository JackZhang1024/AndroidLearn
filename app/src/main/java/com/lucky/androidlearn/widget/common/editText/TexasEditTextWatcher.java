package com.lucky.androidlearn.widget.common.editText;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 限制输入字符数 汉字算两个字符 英文数字算一个字符
 * Created by zfz on 2017/11/22.
 */

public class TexasEditTextWatcher implements TextWatcher {
    private static final String TAG = "TexasEditTextWatcher";
    private EditText mEditText;
    private TextView mTvCount;
    private int mMaxLength = 0; //是按照英文字符个数算的
    private String mNotice;
    private String mFormat;
    private String mBeforeText;

    public TexasEditTextWatcher(EditText editText, TextView tvCount, int maxLength, String format) {
        this.mEditText = editText;
        this.mTvCount = tvCount;
        this.mMaxLength = maxLength;
        this.mFormat = format;
        setLeftCount();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mBeforeText = s.toString();
        Log.e(TAG, "beforeTextChanged: " + mBeforeText);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.e(TAG, "onTextChanged: " + s.toString());
        if (!TextUtils.isEmpty(s) && !isValid(s)) {
            AppToastMgr.ToastShortCenter(mEditText.getContext(), "不允许输入除了英文，数字，汉字之外的字符");
            return;
        }
        if (calculateLength(s) > mMaxLength) {
            if (!TextUtils.isEmpty(mNotice)) {
                AppToastMgr.ToastShortCenter(mEditText.getContext(), mNotice);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.e(TAG, "afterTextChanged: " + s.toString());
        String afterText = s.toString();
        // 先去掉监听器，否则会出现栈溢出
        mEditText.removeTextChangedListener(this);
        afterText = filterIllegalCharacter(afterText);
        int editStart = mEditText.getSelectionStart();
        int editEnd = mEditText.getSelectionEnd();
        Log.e(TAG, "afterTextChanged: editStart " + editStart + " editEnd " + editEnd);
        while (calculateLength(afterText) > mMaxLength) {
            afterText = afterText.substring(0, editStart - 1);
            Log.e(TAG, "afterTextChanged: subString " + afterText);
            editStart--;
            editEnd--;
        }
        int length = afterText.length();
        Log.e(TAG, "afterTextChanged: length " + length + " editStart " + editStart + " afterText " + afterText);
        if (!TextUtils.isEmpty(afterText)) {
            mEditText.setText(afterText);
            mEditText.setSelection(length);
        } else {
            mEditText.setText(afterText);
            mEditText.setSelection(0);
        }
        // 限制输入字符长度
        // 恢复监听器
        mEditText.addTextChangedListener(this);
        setLeftCount();
    }

    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 1;
            } else {
                len += 2;
            }
        }
        return Math.round(len);
    }

    /**
     * 刷新剩余输入字数,最大值新浪微博是140个字，人人网是200个字
     */
    private void setLeftCount() {
        if (mTvCount != null && !TextUtils.isEmpty(mFormat)) {
            String result = String.format(mFormat, getInputCount(), mMaxLength);
            //mTvCount.setText(String.valueOf((mMaxLength - getInputCount())));
            mTvCount.setText(result);
        }
    }

    /**
     * 获取用户输入的分享内容字数
     * @return
     */
    private long getInputCount() {
        return calculateLength(mEditText.getText().toString());
    }

    public void setOverMaxLengthNotice(String notice) {
        this.mNotice = notice;
    }

    private String filterIllegalCharacter(String str) {
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    private boolean isValid(CharSequence str) {
        return str.toString().matches("[a-zA-Z0-9\u4E00-\u9FA5]+");
    }
}
