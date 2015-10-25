package com.jxthelp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * Created by idisfkj on 15/10/21.
 * Email : idisfkj@qq.com.
 */
public class CardsAnimationAdapter extends AnimationAdapter {
    private float mTranslationY=400;
    private float mRotationX=450;
    private long mDuration=500;
    public CardsAnimationAdapter(BaseAdapter baseAdapter) {
        super(baseAdapter);
    }

    @Override
    protected long getAnimationDelayMillis() {
        return 300;
    }

    @Override
    protected long getAnimationDurationMillis() {
        return mDuration;
    }

    @Override
    public Animator[] getAnimators(ViewGroup viewGroup, View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view,"translationY",mTranslationY,0),
                ObjectAnimator.ofFloat(view,"rotationX",mRotationX,0),

        };
    }
}
