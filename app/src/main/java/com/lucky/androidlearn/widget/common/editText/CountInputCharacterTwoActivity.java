package com.lucky.androidlearn.widget.common.editText;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/1/3.
 */

public class CountInputCharacterTwoActivity extends AppCompatActivity {
    private final int maxLen = 10;
    @BindView(R.id.count)
    TextView textView;
    @BindView(R.id.content)
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_character_two);
        ButterKnife.bind(this);
        editText.setFilters(new InputFilter[]{filter});
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
