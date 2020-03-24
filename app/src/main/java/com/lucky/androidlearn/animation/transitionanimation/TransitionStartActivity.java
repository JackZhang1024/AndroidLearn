package com.lucky.androidlearn.animation.transitionanimation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.lucky.androidlearn.R;

/**
 * Created by zfz on 2017/1/4.
 */

public class TransitionStartActivity extends AppCompatActivity {

    public static String SEARCH_X_POSITION="SEARCH_X_POSITION";
    public static String SEARCH_Y_POSITION="SEARCH_Y_POSITION";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final CardView cardView=(CardView) findViewById(R.id.cardview_search);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TransitionStartActivity.this,TransitionTargetActivity.class);
                int[] locations=new int[2];
                cardView.getLocationOnScreen(locations);
                float getCardViewYPosition=cardView.getY();
                float getCardViewYPositionOnScreen=locations[1];
                System.out.println(" YPosition0 "+getCardViewYPosition+" YPositionOnScreen "+getCardViewYPositionOnScreen);
                intent.putExtra(SEARCH_X_POSITION,locations[0]);
                intent.putExtra(SEARCH_Y_POSITION,locations[1]);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }
}
