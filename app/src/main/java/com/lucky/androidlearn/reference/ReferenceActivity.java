package com.lucky.androidlearn.reference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zfz on 2017/12/30.
 */

public class ReferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);
        Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
        Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        SoftReference<Bitmap> value = new SoftReference<Bitmap>(bitMap);
        imageCache.put("yongyuanzaiyiqi", value);
        //使用imageCache
        SoftReference<Bitmap> soft = imageCache.get("yongyuanzaiyiqi");
        Bitmap bitMap2 = soft.get();
        ImageView imageView = (ImageView) findViewById(R.id.imageview);
        imageView.setImageBitmap(bitMap2);
    }


}
