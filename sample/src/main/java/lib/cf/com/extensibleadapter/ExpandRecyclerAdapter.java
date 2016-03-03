package lib.cf.com.extensibleadapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Created by cabe on 16/2/16.
 */
public abstract class ExpandRecyclerAdapter<EV extends ExpandRecyclerAdapter.ExpandHolder> extends RecyclerView.Adapter<EV> {
    protected final static String TAG = "ExpandRecyclerAdapter";

    protected int expandIndex;
    protected int preExpandIndex;

    /** 是否支持展开动画  */
    private boolean flagExpandAnimable = true;
    /** 是否可以关闭当前展开的视图  */
    private boolean flagExpandCloseable = false;
    /** 是否正在动画  */
    private boolean flagExpendViewAnim;

    private void showExpandView(final View view, final int viewHeight, boolean show) {
        if(show) {
            if(viewHeight == 0) return;

            flagExpendViewAnim = true;
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = viewHeight;
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

                        Log.w(TAG, "show expand view:" + viewHeight + "-->" + view.getHeight());
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
                        Log.w(TAG, "hide expand view:" + viewHeight + "-->" + view.getHeight());
                    }
                }
            });
            anim.start();
        }
    }

    protected void setExpandAnimable(boolean able) {
        flagExpandAnimable = able;
    }

    protected void setExpandCloseable(boolean able) {
        flagExpandCloseable = able;
    }

    protected abstract void onBindRealViewHolder(EV viewHolder, int i);

    @Override
    public void onBindViewHolder(EV viewHolder, final int i) {
        onBindRealViewHolder(viewHolder, i);

        if(i == expandIndex) {
            if(flagExpandAnimable) {
                //防止重复动画
                if(expandIndex != preExpandIndex || !flagExpendViewAnim) {
                    int height = (int) viewHolder.expandView.getTag();
                    showExpandView(viewHolder.expandView, height, true);
                }
            }
        } else {
            if(i == preExpandIndex) {
                viewHolder.expandView.setVisibility(View.VISIBLE);
                int height = (int) viewHolder.expandView.getTag();
                showExpandView(viewHolder.expandView, height, false);
            }
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagExpandAnimable && !flagExpendViewAnim) {
                    if (expandIndex != i) {
                        expandIndex = i;
                    } else {
                        if (flagExpandCloseable) {
                            expandIndex = -1;
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    public static abstract class ExpandHolder extends RecyclerView.ViewHolder {
        public View expandView;
        public ExpandHolder(View itemView) {
            super(itemView);
        }
    }
}
