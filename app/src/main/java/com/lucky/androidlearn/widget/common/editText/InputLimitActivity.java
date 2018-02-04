package com.lucky.androidlearn.widget.common.editText;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 输入字符限制
 * Created by zfz on 2018/1/3.
 */

public class InputLimitActivity extends AppCompatActivity {

    @BindView(R.id.input_content)
    EditText inputContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_limit);
        ButterKnife.bind(this);
        TexasEditTextWatcher editTextWatcher = new TexasEditTextWatcher(inputContent, null, 10, "");
        editTextWatcher.setOverMaxLengthNotice("最多不超过5个汉字");
        inputContent.addTextChangedListener(editTextWatcher);

    }
}
