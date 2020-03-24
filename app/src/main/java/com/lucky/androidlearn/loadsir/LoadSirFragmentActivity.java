package com.lucky.androidlearn.loadsir;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

public class LoadSirFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadsire_fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, new LoadSirFragment()).commit();
    }


}
