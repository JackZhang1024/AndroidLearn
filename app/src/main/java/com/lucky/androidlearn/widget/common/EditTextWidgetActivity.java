package com.lucky.androidlearn.widget.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import com.lucky.androidlearn.R;

/**
 * Created by zfz on 2017/11/22.
 */

public class EditTextWidgetActivity extends AppCompatActivity {
    private EditText mEditText = null;
    private TextView mTextView = null;
    private EditText mEditText2 = null;
    private TextView mTexView2 = null;
    private static final int MAX_COUNT = 10;
    private final int maxLen = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editext);
        mEditText = (EditText) findViewById(R.id.content);
        mEditText.addTextChangedListener(mTextWatcher);
        mEditText.setSelection(mEditText.length()); // 将光标移动最后一个字符后面
        mTextView = (TextView) findViewById(R.id.count);
        setLeftCount();
        mEditText2 = (EditText) findViewById(R.id.content2);
        mEditText2.setFilters(new InputFilter[]{filter});
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = mEditText.getSelectionStart();
            editEnd = mEditText.getSelectionEnd();
            // 先去掉监听器，否则会出现栈溢出
            mEditText.removeTextChangedListener(mTextWatcher);
            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength2(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            mEditText.setText(s);
            mEditText.setSelection(editStart);
            // 恢复监听器
            mEditText.addTextChangedListener(mTextWatcher);
            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }
    };

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点
     * 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     * @param c
     * @return
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    private long calculateLength2(CharSequence c){
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
        mTextView.setText(String.valueOf((MAX_COUNT - getInputCount())));
    }

    /**
     * 获取用户输入的分享内容字数
     * @return
     */
    private long getInputCount() {
        //return calculateLength(mEditText.getText().toString());
        return calculateLength2(mEditText.getText().toString());
    }

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
            int dindex = 0;
            int count = 0;
            while (count <= maxLen && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }
            if (count > maxLen) {
                return dest.subSequence(0, dindex - 1);
            }
            int sindex = 0;
            while (count <= maxLen && sindex < src.length()) {
                char c = src.charAt(sindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }
            if (count > maxLen) {
                sindex--;
            }
            return src.subSequence(0, sindex);
        }
    };

}
