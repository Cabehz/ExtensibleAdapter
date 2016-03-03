package lib.cf.com.extensible;

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
        if(show) {
            if(viewHeight == 0) return;

            flagExpendViewAnim = true;
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (int) ExpandAnimHelper.pix2Dip(view.getContext(), 0);
            view.setLayoutParams(params);

            ValueAnimator anim = ObjectAnimator.ofInt(0, viewHeight);
            anim.setDuration(400);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    int animVal = (int) animation.getAnimatedValue();
                    params.height = animVal;
                    view.setLayoutParams(params);

                    if(animVal == viewHeight) {
                        preExpandIndex = expandIndex;
                        flagExpendViewAnim = false;
                    }
                }
            });
            anim.start();
        }
        else {
            ValueAnimator anim = ObjectAnimator.ofInt(viewHeight, 0);
            anim.setDuration(400);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams params = view.getLayoutParams();
                    int animVal = (int) animation.getAnimatedValue();
                    params.height = animVal;
                    view.setLayoutParams(params);

                    if (animVal == 0) {
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

    public boolean isExpandClickable(int position) {
        boolean able = false;
        if(flagExpandAnimable && !flagExpendViewAnim) {
            if(expandIndex != position) {
                expandIndex = position;
            } else {
                if(flagExpandCloseable) {
                    expandIndex = -1;
                }
            }
            able = true;
        }
        return able;
    }

    public static float pix2Dip(Context context, int pix) {
        return context.getResources().getDisplayMetrics().density * pix;
    }
}
