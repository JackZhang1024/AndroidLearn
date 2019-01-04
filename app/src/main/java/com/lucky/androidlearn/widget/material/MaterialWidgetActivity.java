package com.lucky.androidlearn.widget.material;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingewenku.abrahamcaijin.commonutil.PicassoUtils;
import com.lucky.androidlearn.R;

/**
 * Created by zfz on 2017/8/31.
 */

public class MaterialWidgetActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_widget);
//        LinearLayout llRootView = (LinearLayout) findViewById(R.id.root_view);
//        CardView cardView = new CardView(this);
//        cardView.setRadius(10);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
//        TextView textView = new TextView(this);
//        textView.setText("Hello World!");
//        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(400, 100);
//        cardView.addView(textView, layoutParams1);
//        llRootView.addView(cardView, layoutParams);
        loadImageView();
    }

    private void loadImageView() {
        String url = "http://pic14.nipic.com/20110605/1369025_165540642000_2.jpg";
        ImageView imageView = (ImageView) findViewById(R.id.img_boat);
        PicassoUtils.getinstance().LoadImage(this, url, imageView,R.drawable.yanzi, R.drawable.yanzi,
                PicassoUtils.PICASSO_BITMAP_SHOW_NORMAL_TYPE, 20.0f);
    }


}
