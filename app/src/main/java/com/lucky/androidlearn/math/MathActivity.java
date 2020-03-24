package com.lucky.androidlearn.math;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.math.BigDecimal;

import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/1/31.
 */

public class MathActivity extends AppCompatActivity {
    private static final String TAG = "MathActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        ButterKnife.bind(this);
        //doSomethings();
        // 110 100*1.1
        boolean result = compareData(100*1.2, 110);
        Log.e(TAG, "onCreate: "+result);
    }

    public void doSomethings() {
        double f = 4.025;
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(f1);
    }

    public boolean compareData(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        double f1 = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        double f2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Log.e(TAG, "compareData: f1 "+f1+" f2 "+f2);
        b1.compareTo(b2);
        return f1 > f2;
    }


}
