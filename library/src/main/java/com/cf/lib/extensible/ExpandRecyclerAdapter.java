package com.cf.lib.extensible;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * Created by cabe on 16/2/16.
 */
public abstract class ExpandRecyclerAdapter<EV extends ExpandRecyclerAdapter.ExpandHolder> extends RecyclerView.Adapter<EV> implements IExpandListener {
    private ExpandAnimHelper expandHelper = new ExpandAnimHelper();

    @Override
    public int getExpandIndex() {
        return expandHelper.expandIndex;
    }

    @Override
    public void setExpandAnimable(boolean able) {
        expandHelper.setExpandAnimable(able);
    }

    @Override
    public void setFlagExpandCloseable(boolean able) {
        expandHelper.setExpandCloseable(able);
    }

    protected abstract void onBindRealViewHolder(EV viewHolder, int i);

    @Override
    public void onBindViewHolder(EV viewHolder, int viewType) {
        final int position = viewHolder.getAdapterPosition();
        onBindRealViewHolder(viewHolder, position);

        Object obj = viewHolder.expandView == null ? null : viewHolder.expandView;
        int height = Utils.getTagInteger(obj);
        expandHelper.expandAnim(viewHolder.expandView, height, position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandHelper.setExpandPosition(position)) {
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
