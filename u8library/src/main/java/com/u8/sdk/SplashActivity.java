package com.u8.sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class SplashActivity
        extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutID = getResources().getIdentifier("u8_splash", "layout", getPackageName());
        setContentView(layoutID);
        appendAnimation();
    }

    private void appendAnimation() {
        AlphaAnimation ani = new AlphaAnimation(0.0F, 1.0F);
        ani.setRepeatMode(2);
        ani.setRepeatCount(0);
        ani.setDuration(2000L);
        ImageView image = (ImageView) findViewById(getResources().getIdentifier("u8_splash_img", "id", getPackageName()));
        if (image == null) {
            int defaultID = getResources().getIdentifier("u8_splash_layout", "id", getPackageName());
            RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(defaultID, null);
            image = (ImageView) layout.getChildAt(0);
        }
        image.setAnimation(ani);
        ani.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }


            public void onAnimationRepeat(Animation animation) {
            }


            public void onAnimationEnd(Animation animation) {
                SplashActivity.this.startGameActivity();
            }
        });
    }


    private void startGameActivity() {
        try {
            Class<?> mainClass = Class.forName("{U8SDK_Game_Activity}");
            Intent intent = new Intent(this, mainClass);
            startActivity(intent);
            finish();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}


