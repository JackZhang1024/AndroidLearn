package com.lucky.androidlearn.fragmentation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentation);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_viewpager_fragmentation)
    public void viewPagerFragmentsClick() {
        startActivity(new Intent(this, FragmentationViewPagerActivity.class));
    }

    @OnClick(R.id.btn_multi_fragments)
    public void multiFragmentsClick() {
        startActivity(new Intent(this, FragmentationMultiFragmentInActivity.class));
    }


}
