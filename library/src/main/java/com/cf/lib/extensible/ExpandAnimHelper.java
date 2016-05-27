package com.cf.lib.extensible;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 扩展动画辅助类
 * Created by cabe on 16/2/18.
 */
public class ExpandAnimHelper {
    public static int EXPAND_ANIM_DELTA_TIME = 16;

    public int expandIndex = 0;
    public int preExpandIndex = 0;

    /** 是否支持展开动画  */
    private boolean flagExpandAnimable = true;
    /** 是否可以关闭当前展开的视图  */
    private boolean flagExpandCloseable = false;
    /** 是否正在动画  */
    private boolean flagExpendViewAnim;

    public void setExpandAnimable(boolean able) {
        flagExpandAnimable = able;
    }
    public void setExpandCloseable(boolean able) {
        flagExpandCloseable = able;
    }

    private void showExpandView(final View view, final int viewHeight, boolean show) {
        long duration = 400;
        if(show) {
            if(viewHeight == 0) {
                expandIndex = -1;
                return;
            }

            flagExpendViewAnim = true;
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (int) ExpandAnimHelper.pix2Dip(view.getContext(), 0);
            view.setLayoutParams(params);

            ValueAnimator anim = ObjectAnimator.ofInt(0, viewHeight);
            anim.setDuration(duration);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.height = (int) animation.getAnimatedValue();
                    view.setLayoutParams(params);

                    if(isAnimEnd(animation)) {
                        preExpandIndex = expandIndex;
                        flagExpendViewAnim = false;
                    }
                }
            });
            anim.start();
        }
        else {
            ValueAnimator anim = ObjectAnimator.ofInt(viewHeight, 0);
            anim.setDuration(duration);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    params.height = (int) animation.getAnimatedValue();
                    view.setLayoutParams(params);

                    if (isAnimEnd(animation)) {
                        view.setVisibility(View.GONE);

                        params.height = viewHeight;
                        view.setLayoutParams(params);
                        flagExpendViewAnim = false;
                        if (expandIndex == -1) {
                            preExpandIndex = -1;
                        }
                    }
                }
            });
            anim.start();
        }
    }

    private boolean isAnimEnd(ValueAnimator anim) {
        return Math.abs(anim.getCurrentPlayTime() - anim.getDuration()) < EXPAND_ANIM_DELTA_TIME;
    }

    public void expandAnim(View expandView, int expandHeight, int position) {
        if(position == expandIndex) {
            if(flagExpandAnimable) {
                //防止重复动画
                if(expandIndex != preExpandIndex && !flagExpendViewAnim) {
                    showExpandView(expandView, expandHeight, true);
                }
            }
        } else {
            if(position == preExpandIndex) {
                expandView.setVisibility(View.VISIBLE);
                showExpandView(expandView, expandHeight, false);
            }
        }
    }

    public boolean setExpandPosition(int position) {
        boolean able = false;
        if(flagExpandAnimable && !flagExpendViewAnim) {
            if(expandIndex != position) {
                expandIndex = position;
                able = true;
            } else {
                if(flagExpandCloseable) {
                    expandIndex = -1;
                    able = true;
                }
            }
        }
        return able;
    }

    public static float pix2Dip(Context context, int pix) {
        return context.getResources().getDisplayMetrics().density * pix;
    }
}
