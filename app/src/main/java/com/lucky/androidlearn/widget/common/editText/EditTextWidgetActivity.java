package com.lucky.androidlearn.widget.common.editText;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/11/22.
 */

public class EditTextWidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editext);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_count_method_1)
    public void onCountCharacterMethod1Click(){
        startActivity(new Intent(this, CountInputCharacterOneActivity.class));
    }

    @OnClick(R.id.btn_count_method_2)
    public void onCountCharacterMethod2Click(){
        startActivity(new Intent(this, CountInputCharacterTwoActivity.class));
    }

    @OnClick(R.id.btn_input_limit)
    public void onInputLimitClick(){
        startActivity(new Intent(this, InputLimitActivity.class));
    }

    @OnClick(R.id.btn_keyboard)
    public void onKeyBoardClick(){
        startActivity(new Intent(this, EditTextKeyBoardChangeActivity.class));
    }

    @OnClick(R.id.btn_keyboard_change)
    public void onKeyBoardChangeClick(){
        startActivity(new Intent(this, keyBoardChangeActivity.class));
    }

}
