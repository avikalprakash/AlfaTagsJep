package com.organization.jeptagsalpha.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

public class GlowAnimation {
    private Animation anim;
    private final View glowView;

    public GlowAnimation(View warningView) {
        this.glowView = warningView;
    }

    public void start() {
        this.glowView.setVisibility(View.VISIBLE);
        this.glowView.startAnimation(getAnimation());
    }

    public void stop() {
        this.glowView.clearAnimation();
        this.glowView.setVisibility(View.INVISIBLE);
    }

    public Animation getAnimation() {
        if (this.anim == null) {
            this.anim = new AlphaAnimation(0.05f, 0.5f);
            this.anim.setDuration(500);
            this.anim.setInterpolator(new DecelerateInterpolator());
            this.anim.setRepeatMode(2);
            this.anim.setRepeatCount(-1);
            this.anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return this.anim;
    }
}
