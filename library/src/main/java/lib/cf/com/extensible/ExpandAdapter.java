package lib.cf.com.extensible;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 *
 * Created by cabe on 16/2/15.
 */
public abstract class ExpandAdapter extends BaseAdapter implements IExpandListener {
    protected final static String TAG = "ExpandAdapter";
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

    protected abstract View getRealView(int position, View convertView, ViewGroup parent);

    @Override
    public final View getView(final int position, View convertView, ViewGroup parent) {
        View view = getRealView(position, convertView, parent);
        if(view == null) return null;

        Object obj = view.getTag();
        if(obj instanceof  ExpandHolder) {
            ExpandHolder holder = (ExpandHolder) obj;
            Object o = holder.expandView.getTag();
            int height = o == null ? 0 : (int) o;
            expandHelper.expandAnim(holder.expandView, height, position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(expandHelper.isExpandClickable(position)) {
                        notifyDataSetChanged();
                    }
                }
            });
        }

        return view;
    }

    public static class ExpandHolder {
        public View expandView;
    }
}
